package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.finance.app.R
import com.finance.app.databinding.FragmentLoanInformationBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.AppEnums
import com.finance.app.others.Injection
import com.finance.app.persistence.model.*
import com.finance.app.utility.CurrencyConversion
import com.finance.app.utility.DisableLoanInfoForm
import com.finance.app.utility.SetLoanInfoMandatoryField
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadMaster
import com.finance.app.view.customViews.CustomChannelPartnerView
import com.finance.app.view.customViews.CustomSpinnerViewTest
import com.finance.app.view.customViews.Interfaces.IspinnerMainView
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class LoanInfoFragmentNew : BaseFragment(){

    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: FragmentLoanInformationBinding
    private lateinit var appDataViewModel: AppDataViewModel
    private lateinit var interestType: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var loanScheme: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var loanProduct: CustomSpinnerViewTest<LoanProductMaster>
    private lateinit var loanPurpose: CustomSpinnerViewTest<LoanPurpose>

    companion object {
        fun newInstance(): LoanInfoFragmentNew = LoanInfoFragmentNew()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_loan_information)
        binding.lifecycleOwner = this
        init()
        return view
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        val viewModelFactory: ViewModelProvider.Factory = Injection.provideViewModelFactory(activity!!)
        appDataViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AppDataViewModel::class.java)
        setUpCustomViews()
        SetLoanInfoMandatoryField(binding)
        val loanInfo = leadMaster?.loanData
        getDropDownsFromDB(loanInfo)
        setClickListeners()
    }

    private fun setUpCustomViews() {
        activity?.let {
            binding.customChannelPartnerView.attachActivity(activity = activity!!)
        }
    }

    private fun setClickListeners() {
        binding.btnNext.setOnClickListener {
            if (formValidation.validateLoanInformation(binding, loanProduct.getSelectedValue())) {
                checkPropertySelection()
                leadMaster?.loanData = getLoanData()
                AppEvents.fireEventLoanAppChangeNavFragmentNext()
            } else showToast(getString(R.string.validation_error))
        }
        CurrencyConversion().convertToCurrencyType(binding.etAmountRequest)
    }

    private fun getDropDownsFromDB(loanInfo: LoanInfoModel?) {
        appDataViewModel.getLoanProductMaster().observe(viewLifecycleOwner, Observer { loanProductValue ->
            loanProductValue?.let {
                val arrayListOfLoanProducts = ArrayList<LoanProductMaster>()
                arrayListOfLoanProducts.addAll(loanProductValue)
                setLoanProductDropdown(arrayListOfLoanProducts, loanInfo)
            }
        })

        appDataViewModel.getAllMasterDropdown().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                setMasterDropDownValue(masterDrownDownValues, loanInfo)
            }
        })
    }

    private fun setLoanProductDropdown(products: ArrayList<LoanProductMaster>, loanInfo: LoanInfoModel?) {
        loanProduct = CustomSpinnerViewTest(context = activity!!, dropDowns = products, label = "Loan Product *", iSpinnerMainView = object : IspinnerMainView<LoanProductMaster> {
            override fun getSelectedValue(value: LoanProductMaster) {
                setLoanPurposeDropdown(value, loanInfo)
            }
        })
        binding.layoutLoanProduct.addView(loanProduct)

        if (loanInfo != null) {
            loanProduct.setSelection(loanInfo.productID.toString())
        } else loanProduct.setSelection(leadMaster?.loanProductID.toString())
    }

    private fun setLoanPurposeDropdown(loan: LoanProductMaster?, loanInfo: LoanInfoModel?) {
        loan?.let {
            binding.layoutLoanPurpose.removeAllViews()
            loanPurpose = CustomSpinnerViewTest(context = activity!!, dropDowns = loan.loanPurposeList, label = "Loan Purpose")
            binding.layoutLoanPurpose.addView(loanPurpose)
        }

        loanInfo?.loanPurposeID?.let {
            loanPurpose.setSelection(loanInfo.loanPurposeID.toString())
            loanInfo.loanPurposeID = null
        }
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown, loanInfo: LoanInfoModel?) {
        setCustomSpinner(allMasterDropDown)
        loanInfo?.let {
            selectSpinnerValue(loanInfo)
            fillFormWithLoanData(loanInfo)
            checkSubmission()
        }
    }

    private fun selectSpinnerValue(loanInfo: LoanInfoModel) {
        interestType.setSelection(loanInfo.interestTypeTypeDetailID?.toString())
        loanScheme.setSelection(loanInfo.loanSchemeTypeDetailID?.toString())
        selectChannelPartner(binding.customChannelPartnerView, loanInfo)
    }

    private fun fillFormWithLoanData(loanInfo: LoanInfoModel) {
        binding.etAmountRequest.setText(loanInfo.loanAmountRequest.toString())
        binding.etEmi.setText(loanInfo.affordableEMI!!.toInt().toString())
        binding.etTenure.setText(loanInfo.tenure!!.toInt().toString())
        binding.cbPropertySelected.isChecked = loanInfo.isPropertySelected!!
    }

    private fun checkPropertySelection() {
        if (binding.cbPropertySelected.isChecked) {
            sharedPreferences.setPropertySelection("Yes")
        } else {
            sharedPreferences.setPropertySelection("No")
        }
    }

    private fun selectChannelPartner(customChannelPartnerView: CustomChannelPartnerView, loanInfo: LoanInfoModel) {
        customChannelPartnerView.selectSourcingChannelPartner(loanInfo)
    }

    private fun setCustomSpinner(allMasterDropDown: AllMasterDropDown) {
        interestType = CustomSpinnerViewTest(context = activity!!, dropDowns = allMasterDropDown.LoanInformationInterestType!!, label = "Interest Type")
        binding.layoutInterestType.addView(interestType)
        loanScheme = CustomSpinnerViewTest(context = activity!!, dropDowns = allMasterDropDown.LoanScheme!!, label = "Loan Scheme")
        binding.layoutLoanScheme.addView(loanScheme)
    }

    private fun checkSubmission() {
        if (leadMaster!!.status == AppEnums.LEAD_TYPE.SUBMITTED.type) {
            DisableLoanInfoForm(binding)
        }
    }

    private fun getLoanData(): LoanInfoModel {
        val loanInfoObj = LoanInfoModel()
        val sPartner = binding.customChannelPartnerView.getSourcingChannelPartner()
        val cPartnerName = binding.customChannelPartnerView.getPartnerName()
        val lProductDD = loanProduct.getSelectedValue()
        val lPurposeDD = loanPurpose.getSelectedValue()
        val lScheme = loanScheme.getSelectedValue()
        val iType = interestType.getSelectedValue()
        val empId = sharedPreferences.getEmpId()

        loanInfoObj.leadID = leadMaster!!.leadID!!.toInt()
        loanInfoObj.productID = lProductDD?.productID
        loanInfoObj.salesOfficerEmpID = empId!!.toInt()
        loanInfoObj.loanPurposeID = lPurposeDD?.loanPurposeID
        loanInfoObj.loanSchemeTypeDetailID = lScheme?.typeDetailID
        loanInfoObj.interestTypeTypeDetailID = iType?.typeDetailID
        loanInfoObj.sourcingChannelPartnerTypeDetailID = sPartner?.typeDetailID
        loanInfoObj.isPropertySelected = binding.cbPropertySelected.isChecked
        loanInfoObj.loanAmountRequest = CurrencyConversion().convertToNormalValue(binding.etAmountRequest.text.toString()).toInt()
        loanInfoObj.tenure = binding.etTenure.text.toString().toInt()
        loanInfoObj.channelPartnerDsaID = cPartnerName?.dsaID
        loanInfoObj.affordableEMI = binding.etEmi.text.toString().toDouble()
        return loanInfoObj
    }

}
