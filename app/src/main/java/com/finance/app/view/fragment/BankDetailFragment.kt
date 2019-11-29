package com.finance.app.view.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.utility.ClearBankForm
import com.finance.app.utility.RequestConversion
import com.finance.app.utility.ResponseConversion
import com.finance.app.utility.SetBankDetailMandatoryField
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class BankDetailFragment : BaseFragment(), LoanApplicationConnector.PostLoanApp,
        LoanApplicationConnector.GetLoanApp, ApplicantsAdapter.ItemClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentBankDetailBinding
    private lateinit var mContext: Context
    private lateinit var allMasterDropDown: AllMasterDropDown
    private var mLead: AllLeadMaster? = null
    private val loanAppGetPresenter = LoanAppGetPresenter(this)
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private var bankDetailMaster: BankDetailMaster = BankDetailMaster()
    private var bDraftData = BankDetailList()
    private var bApplicantsList: ArrayList<BankDetailModel>? = ArrayList()
    private var bankDetailBeanList: ArrayList<BankDetailBean> = ArrayList()
    private var currentApplicant: BankDetailModel = BankDetailModel()
    private var currentBean: BankDetailBean? = BankDetailBean()
    private var currentPosition = 0

    companion object {
        private lateinit var applicantTab: ArrayList<String>
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
        SetBankDetailMandatoryField(binding)
        checkIncomeConsideration()
        setClickListeners()
    }

    private fun checkIncomeConsideration() {
        /*val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            formValidation.disableBankDetailFields(binding)
        } else*/ getBankDetail()
    }

    private fun getBankDetail() {
        mLead = sharedPreferences.getLeadDetail()
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = bankDetailMaster.storageType

    override fun getLoanAppGetFailure(msg: String) = getDataFromDB()

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            bankDetailMaster = responseConversion.toBankDetailMaster(value.responseObj)
            bDraftData = bankDetailMaster.draftData!!
            bApplicantsList = bDraftData.applicantDetails
        }
        setCoApplicants(bApplicantsList)
        showData(bApplicantsList)
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
                    currentBean = if (bankDetailBeanList.size > 0) {
                        bankDetailBeanList[0]
                    } else BankDetailBean()
                }
            }
        }
        fillFormWithCurrentApplicant(currentBean)
        getDropDownsFromDB()
    }

    private fun fillFormWithCurrentApplicant(currentBean: BankDetailBean?) {
        binding.etAccountHolderName.setText(currentBean?.accountHolderName)
        binding.etSalaryCreditedInSixMonths.setText(currentBean?.numberOfCredit.toString())
        binding.etAccountNum.setText(currentBean?.accountNumber.toString())
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
        ClearBankForm(binding, mContext, allMasterDropDown)
        getParticularApplicantData(position)
    }

    private fun saveCurrentApplicant() {
        if (bApplicantsList!!.size > 0) {
            bApplicantsList!![currentPosition] = getCurrentApplicant()
        } else bApplicantsList!!.add(currentPosition, getCurrentApplicant())
    }

    private fun getParticularApplicantData(position: Int) {

    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().bankDetailDao().getBankDetail(leadId).observe(this, Observer { bankInfo ->
            bankInfo?.let {
                bankDetailMaster = bankInfo
                bDraftData = bankDetailMaster.draftData!!
                bApplicantsList = bDraftData.applicantDetails
                if (bApplicantsList!!.size < 0) {
                    bApplicantsList!!.add(BankDetailModel())
                }
            }
            setCoApplicants(bApplicantsList)
            showData(bApplicantsList)
        })
    }

    private fun setClickListeners() {
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateBankDetail(binding)) {
                saveCurrentBean()
                saveCurrentApplicant()
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            } else showToast(getString(R.string.validation_error))
        }
        binding.btnAddBankDetail.setOnClickListener {
            if (formValidation.validateBankDetail(binding)) {
                saveCurrentBean()
                ClearBankForm(binding, mContext, allMasterDropDown)
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            } else showToast(getString(R.string.validation_error))
        }
    }

    private fun saveCurrentBean() {
        if (bankDetailBeanList.size > 0) {
            bankDetailBeanList[currentPosition] = getCurrentBean()
        } else bankDetailBeanList.add(currentPosition, getCurrentBean())
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
        binding.spinnerBankName.adapter = MasterSpinnerAdapter(context!!, allMasterDropDown.BankName!!)
        binding.spinnerAccountType.adapter = MasterSpinnerAdapter(context!!, allMasterDropDown.AccountType!!)
        binding.spinnerSalaryCredit.adapter = MasterSpinnerAdapter(context!!, allMasterDropDown.SalaryCredit!!)
        fillValueInMasterDropDown()
    }

    private fun fillValueInMasterDropDown() {
        selectMasterDropdownValue(binding.spinnerAccountType, currentBean?.accountTypeDetailID)
        selectMasterDropdownValue(binding.spinnerBankName, currentBean?.bankDetailID)
        selectMasterDropdownValue(binding.spinnerSalaryCredit, currentBean?.salaryCreditTypeDetailID)
    }

    private fun selectMasterDropdownValue(spinner: Spinner, id: Int?) {
        for (index in 0 until spinner.count - 1) {
            val obj = spinner.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == id) {
                spinner.setSelection(index + 1)
                return
            }
        }
    }

    private fun getCurrentBean(): BankDetailBean {
        val currentBean = BankDetailBean()
        val bankName = binding.spinnerBankName.selectedItem as DropdownMaster?
        val accountType = binding.spinnerAccountType.selectedItem as DropdownMaster?

        currentBean.bankDetailID = bankName?.typeDetailID
        currentBean.accountTypeDetailID = accountType?.typeDetailID
        currentBean.accountHolderName = binding.etAccountHolderName.text.toString()
        currentBean.accountNumber = binding.etAccountNum.text.toString().toInt()
        currentBean.numberOfCredit = binding.etSalaryCreditedInSixMonths.text.toString().toInt()
        return currentBean
    }

    private fun getCurrentApplicant(): BankDetailModel {
        val currentApplicant = BankDetailModel()
        currentApplicant.isMainApplicant = currentPosition == 0
        currentApplicant.applicantBankDetailsBean = bankDetailBeanList
        return currentApplicant
    }

    private fun getBankDetailMaster(): BankDetailMaster {
        bDraftData.applicantDetails = bApplicantsList
        bankDetailMaster.draftData = bDraftData
        bankDetailMaster.leadID = leadId.toInt()
        return bankDetailMaster
    }

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.bankRequest(getBankDetailMaster())

    override fun getLoanAppPostFailure(msg: String) {
        saveDataToDB(getBankDetailMaster())
        showToast(msg)
    }

    override fun getLoanAppPostSuccess(value: Response.ResponseGetLoanApplication) {
        saveDataToDB(getBankDetailMaster())
        gotoNextFragment()
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, AssetLiabilityFragment())
        ft?.addToBackStack(null)
        ft?.commit()
    }

    private fun saveDataToDB(bankDetail: BankDetailMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().bankDetailDao().insertBankDetail(bankDetail)
        }
    }
}
