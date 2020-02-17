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
import com.finance.app.utility.LeadMetaData
import com.finance.app.utility.SetLoanInfoMandatoryField
import com.finance.app.view.customViews.CustomSpinnerView
import com.finance.app.view.customViews.interfaces.IspinnerMainView
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class LoanInfoFragmentNew : BaseFragment() {

    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: FragmentLoanInformationBinding
    private lateinit var appDataViewModel: AppDataViewModel
    private lateinit var interestType: CustomSpinnerView<DropdownMaster>
    private lateinit var loanScheme: CustomSpinnerView<DropdownMaster>
    private lateinit var loanProduct: CustomSpinnerView<LoanProductMaster>
    private lateinit var loanPurpose: CustomSpinnerView<LoanPurpose>
    private var spinnerDMList: ArrayList<CustomSpinnerView<DropdownMaster>> = ArrayList()
    private var leadDetail: AllLeadMaster? = null

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
        leadDetail = LeadMetaData.getLeadData()

        ArchitectureApp.instance.component.inject(this)
        val viewModelFactory: ViewModelProvider.Factory = Injection.provideViewModelFactory(activity!!)
        appDataViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AppDataViewModel::class.java)
        SetLoanInfoMandatoryField(binding)
        val loanInfo = leadDetail?.loanData
        setUpCustomViews(loanInfo)
        getDropDownsFromDB(loanInfo)
        setClickListeners()

    }

    private fun setUpCustomViews(loanInfo: LoanInfoModel?) {
        activity?.let {
            binding.viewChannelPartner.attachActivity(activity = activity!!, loanData = loanInfo)
        }
    }

    private fun setClickListeners() {
        CurrencyConversion().convertToCurrencyType(binding.etAmountRequest)
        binding.btnNext.setOnClickListener {
            val lProductDD = loanProduct.getSelectedValue()
            if (lProductDD != null) {
                if (formValidation.validateLoanInformation(binding, loanProduct, loanPurpose,
                                spinnerDMList, binding.viewChannelPartner)) {

                    checkPropertySelection()
                    LeadMetaData().saveLoanData(getLoanData())
                    AppEvents.fireEventLoanAppChangeNavFragmentNext()

                } else showToast(getString(R.string.validation_error))
            } else {
                showToast(getString(R.string.enter_mandatory))

            }
        }
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
        loanProduct = CustomSpinnerView(mContext = activity!!, isMandatory = true, dropDowns = products, label = "Loan Product *", iSpinnerMainView = object : IspinnerMainView<LoanProductMaster> {
            override fun getSelectedValue(value: LoanProductMaster) {
                setLoanPurposeDropdown(value, loanInfo)
            }
        })
        binding.layoutLoanProduct.addView(loanProduct)

        if (loanInfo != null) {
            loanProduct.setSelection(loanInfo.productID.toString())
        } else loanProduct.setSelection(leadDetail?.loanProductID.toString())
    }

    private fun setLoanPurposeDropdown(loan: LoanProductMaster?, loanInfo: LoanInfoModel?) {
        loan?.let {
            binding.layoutLoanPurpose.removeAllViews()
            loanPurpose = CustomSpinnerView(mContext = activity!!, isMandatory = true, dropDowns = loan.loanPurposeList, label = "Loan Purpose *")
            binding.layoutLoanPurpose.addView(loanPurpose)
        }

        loanInfo?.loanPurposeID?.let {
            loanPurpose.setSelection(loanInfo.loanPurposeID.toString())
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
    }

    private fun fillFormWithLoanData(loanInfo: LoanInfoModel) {
        binding.etAmountRequest.setText(loanInfo.loanAmountRequest.toString())
        binding.etEmi.setText(loanInfo.affordableEMI!!.toInt().toString())
        binding.etTenure.setText(loanInfo.tenure!!.toInt().toString())
        binding.cbPropertySelected.isChecked = loanInfo.isPropertySelected!!
        binding.etApplicationNumber.setText(loanInfo.applicationNumber)
    }

    private fun checkPropertySelection() {
        if (binding.cbPropertySelected.isChecked) {
            sharedPreferences.setPropertySelection("Yes")
        } else {
            sharedPreferences.setPropertySelection("No")
        }
    }

    private fun setCustomSpinner(allMasterDropDown: AllMasterDropDown) {
        interestType = CustomSpinnerView(mContext = activity!!, isMandatory = true, dropDowns = allMasterDropDown.LoanInformationInterestType!!, label = "Interest Type *")
        binding.layoutInterestType.addView(interestType)
        loanScheme = CustomSpinnerView(mContext = activity!!, isMandatory = true, dropDowns = allMasterDropDown.LoanScheme!!, label = "Loan Scheme *")
        binding.layoutLoanScheme.addView(loanScheme)

        spinnerDMList.add(interestType)
        spinnerDMList.add(loanScheme)
    }

    private fun checkSubmission() {
        if (leadDetail!!.status == AppEnums.LEAD_TYPE.SUBMITTED.type) {
            DisableLoanInfoForm(binding)
        }
    }

    private fun getLoanData(): LoanInfoModel {
        val loanInfoObj = LoanInfoModel()
        val sPartner = binding.viewChannelPartner.getSourcingPartner()
        val cPartnerName = binding.viewChannelPartner.getPartnerName()
        val lProductDD = loanProduct.getSelectedValue()
        val lPurposeDD = loanPurpose.getSelectedValue()
        val lScheme = loanScheme.getSelectedValue()
        val iType = interestType.getSelectedValue()
        val empId = sharedPreferences.getEmpId()

        loanInfoObj.leadID = leadDetail?.leadID
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
        loanInfoObj.logginUserEntityID = sharedPreferences.getUserId()!!.toInt()
        loanInfoObj.applicationNumber= "GG".plus(binding.etApplicationNumber.text.toString())
        return loanInfoObj
    }

}
