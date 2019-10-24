package com.finance.app.view.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.BankDetailPresenter
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.YesNoSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class BankDetailFragment : BaseFragment(), LoanApplicationConnector.BankDetail {

    private lateinit var binding: FragmentBankDetailBinding
    private lateinit var mContext: Context
    private lateinit var allMasterDropDown: AllMasterDropDown
    private val bankDetailPresenter = BankDetailPresenter(this)
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

    companion object {
        var bankDetailList: ArrayList<Requests.BankDetail> = ArrayList()
        var bankDetailBeanList: ArrayList<Requests.ApplicantBankDetailsBean> = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        getDropDownsFromDB()
        setClickListeners()
        checkIncomeConsideration()
    }

    private fun checkIncomeConsideration() {
        val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            Toast.makeText(context, "Income not considered in Loan Information",
                    Toast.LENGTH_SHORT).show()
            disableAllFields()
        }
    }

    private fun disableAllFields() {
        formValidation.disableBankDetailFields(binding)
    }

    private fun setClickListeners() {
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateBankDetail(binding)) {
                bankDetailBeanList.add(bankDetailBean)
                bankDetailList.add(bankDetail)
                bankDetailPresenter.callNetwork(ConstantsApi.CALL_BANK_DETAIL)
            }
        }
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun setMasterDropDownValue(allMasterDropDown: AllMasterDropDown) {
        val bankNameList = allMasterDropDown.BankName!!
        val accountType = allMasterDropDown.AccountType!!
        binding.spinnerBankName.adapter = MasterSpinnerAdapter(context!!, bankNameList)
        binding.spinnerAccountType.adapter = MasterSpinnerAdapter(context!!, accountType)
        binding.spinnerSalaryCredit.adapter = YesNoSpinnerAdapter(context!!)
    }

    override val bankDetailRequest: Requests.RequestBankDetail
        get() {
            val leadId = 5
            return Requests.RequestBankDetail(leadID = leadId, loanApplicationObj = bankDetailObj)
        }

    private val bankDetailObj: Requests.BankDetailObj
        get() {
            return Requests.BankDetailObj(bankDetailList)
        }

    private val bankDetail: Requests.BankDetail
        get() {
            return Requests.BankDetail(bankDetailBeanList, leadApplicantNumber = "1")
        }

    private val bankDetailBean: Requests.ApplicantBankDetailsBean
        get() {
            val bankName = binding.spinnerBankName.selectedItem as DropdownMaster
            val accountType = binding.spinnerAccountType.selectedItem as DropdownMaster
            return Requests.ApplicantBankDetailsBean(accountHolderName = binding.etAccountHolderName.text.toString(),
                    accountNumber = binding.etAccountNum.text.toString().toLong(), bankNameTypeDetailID = bankName.typeDetailID,
                    accountTypeDetailID = accountType.typeDetailID, salaryCreditTypeDetailID = binding.spinnerSalaryCredit.selectedItemPosition,
                    numberOfCredit = binding.etSalaryCreditedInSixMonths.text.toString().toInt()
            )
        }

    override fun getBankDetailSuccess(value: Response.ResponseBankDetail) = gotoNextFragment()

    override fun getBankDetailFailure(msg: String) = showToast(msg)

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, AssetLiabilityFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

}
