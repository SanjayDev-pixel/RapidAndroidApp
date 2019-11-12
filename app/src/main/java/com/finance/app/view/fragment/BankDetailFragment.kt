package com.finance.app.view.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.BankDetailGetPresenter
import com.finance.app.presenter.presenter.BankDetailPostPresenter
import com.finance.app.utility.ClearBankForm
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.Spinner.YesNoSpinnerAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class BankDetailFragment : BaseFragment(), LoanApplicationConnector.PostBankDetail,
        LoanApplicationConnector.GetBankDetail, ApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentBankDetailBinding
    private lateinit var mContext: Context
    private lateinit var allMasterDropDown: AllMasterDropDown
    private val bankDetailPostPresenter = BankDetailPostPresenter(this)
    private val bankDetailGetPresenter = BankDetailGetPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var mLeadId: String? = null
    private var empId: String? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

    companion object {
        private lateinit var applicantTab: ArrayList<String>
        private var bankDetailMaster: BankDetailMaster? = BankDetailMaster()
        private var bankDetailList: ArrayList<Response.BankDetail>? = null
        var bankDetailBeanList: ArrayList<Requests.ApplicantBankDetailsBean> = ArrayList()
        var bankDetailBean: Response.ApplicantBankDetailsBean? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        applicantTab = ArrayList()
        setCoApplicants()
        getBankDetail()
        getDropDownsFromDB()
        setClickListeners()
        checkIncomeConsideration()
    }

    private fun getBankDetail() {
        mLeadId = sharedPreferences.getLeadId()
        empId = sharedPreferences.getUserId()
//        bankDetailGetPresenter.callNetwork(ConstantsApi.CALL_BANK_DETAIL_GET)
    }

    override val leadId: String
        get() = mLeadId!!

    override fun getBankDetailGetSuccess(value: Response.ResponseGetBankDetail) {
        value.responseObj?.let {
            saveDataToDB(value.responseObj)
            bankDetailMaster = value.responseObj
//            loanInfo = bankDetailMaster?.loanApplicationObj
//            showData(loanInfo)
        }
    }

    private fun saveDataToDB(bankDetail: BankDetailMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().bankDetailDao().insertBankDetail(bankDetail)
        }
    }

    override fun getBankDetailGetFailure(msg: String) {
    }

    private fun setCoApplicants() {
        applicantTab.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab)
        binding.rcApplicants.adapter = applicantAdapter
        applicantAdapter!!.setOnItemClickListener(this)
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
        ClearBankForm(binding)
        getParticularApplicantData(position)
    }

    private fun saveCurrentApplicant() {
    }

    private fun getParticularApplicantData(position: Int) {

    }

    private fun checkIncomeConsideration() {
        val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            Toast.makeText(context, "Income not considered in Loan Information",
                    Toast.LENGTH_SHORT).show()
//            formValidation.disableBankDetailFields(binding)
        }
    }

    private fun setClickListeners() {
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateBankDetail(binding)) {
                bankDetailBeanList.add(bankDetailBean)
//                bankDetailList.add(bankDetail)
                gotoNextFragment()
                bankDetailPostPresenter.callNetwork(ConstantsApi.CALL_BANK_DETAIL_POST)
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

    override val bankDetailRequest: BankDetailMaster
        get() = bankDetailMaster!!

    override fun getBankDetailPostSuccess(value: Response.ResponseLoanApplication) = gotoNextFragment()

    override fun getBankDetailPostFailure(msg: String) = showToast(msg)

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, AssetLiabilityFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }
}
