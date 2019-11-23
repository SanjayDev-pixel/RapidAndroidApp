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
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.YesNoSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
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

class BankDetailFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, ApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentBankDetailBinding
    private lateinit var mContext: Context
    private lateinit var allMasterDropDown: AllMasterDropDown
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var mLeadId: String? = null
    private var empId: String? = null
    private val responseConversion = ResponseConversion()
    private val requestConversion = RequestConversion()
    private var mLead: AllLeadMaster? = null
    private var currentApplicant: BankDetailModel = BankDetailModel()
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

    companion object {
        private lateinit var applicantTab: ArrayList<String>
        private var bankDetailMaster: BankDetailMaster? = BankDetailMaster()
        private var bankDetail: BankDetail? = BankDetail()
        private var bankApplicantsList:ArrayList<BankDetailModel>?= ArrayList()
        private var bankDetailBeanList: ArrayList<BankDetailBean> = ArrayList()
        private var bankDetailBean: BankDetailBean? = BankDetailBean()
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
        SetBankDetailMandatoryField(binding)
        getBankDetail()
        getDropDownsFromDB()
        setClickListeners()
        checkIncomeConsideration()
    }

    private fun getBankDetail() {
        mLead = sharedPreferences.getLeadDetail()
        empId = sharedPreferences.getUserId()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = bankDetailMaster?.storageType!!

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            bankDetailMaster = responseConversion.toBankDetailMaster(value.responseObj)
            bankDetail = bankDetailMaster?.draftData!!
            bankApplicantsList = bankDetail?.applicantDetails
        }
        setCoApplicants(bankApplicantsList)
        showData(bankApplicantsList)
    }
    override fun getLoanAppGetFailure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setCoApplicants(applicants: ArrayList<BankDetailModel>?) {
        applicantTab = ArrayList()
        applicantTab.add("Applicant")
        if (applicants != null && applicants.size > 1) {
            for (position in 1 until applicants.size) {
                applicantTab.add("CoApplicant $position")
            }
        }
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    private fun showData(applicantList: ArrayList<BankDetailModel>?) {
        if (applicantList != null) {
            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    bankDetailBeanList = currentApplicant.applicantBankDetailsBean
                }
            }
        }
        fillFormWithCurrentApplicant(currentApplicant)
        getDropDownsFromDB()
    }

    private fun fillFormWithCurrentApplicant(currentApplicant: BankDetailModel) {

    }

    private fun saveDataToDB(bankDetail: BankDetailMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().bankDetailDao().insertBankDetail(bankDetail)
        }
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
//                bankDetailBeanList.add(bankDetailBean)
//                bankDetail.add(bankDetail)
                gotoNextFragment()
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
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

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.bankRequest(getBankDetailMaster())

    private fun getBankDetailMaster(): BankDetailMaster {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
    }

    override fun getLoanAppPostFailure(msg: String) = showToast(msg)

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, AssetLiabilityFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }
}
