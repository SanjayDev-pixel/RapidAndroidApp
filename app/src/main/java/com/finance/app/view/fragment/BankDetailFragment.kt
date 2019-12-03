package com.finance.app.view.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.BankDetailAdapter
import kotlinx.android.synthetic.main.delete_dialog.view.*
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
        LoanApplicationConnector.GetLoanApp, ApplicantsAdapter.ItemClickListener,
        BankDetailAdapter.BankDetailClickListener {

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
    private lateinit var bankAdapter: BankDetailAdapter
    private var applicantTab: ArrayList<Response.CoApplicantsObj>? = ArrayList()
    private var bankDetailMaster: BankDetailMaster = BankDetailMaster()
    private var bDraftData = BankDetailList()
    private var bApplicantsList: ArrayList<BankDetailModel>? = ArrayList()
    private var bankDetailBeanList: ArrayList<BankDetailBean>? = ArrayList()
    private var currentApplicant: BankDetailModel = BankDetailModel()
    private var currentBean: BankDetailBean? = BankDetailBean()
    private var currentPosition = 0

    companion object {
        private val leadAndLoanDetail = LeadAndLoanDetail()
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
        } else*/
        getBankDetail()
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
        setCoApplicants()
        showData(bApplicantsList)
    }

    private fun setCoApplicants() {
        val applicantsList = sharedPreferences.getCoApplicantsList()
        if (applicantsList == null || applicantsList.size <= 0) {
            applicantTab?.add(getDefaultCoApplicant())
        }else applicantTab = applicantsList
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab!!)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    private fun getDefaultCoApplicant(): Response.CoApplicantsObj {
        return Response.CoApplicantsObj(firstName = "Applicant",
                isMainApplicant = true, leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1))
    }

    private fun showData(bankList: ArrayList<BankDetailModel>?) {
        if (bankList != null) {
            for (applicant in bankList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    setUpCurrentApplicantDetails(currentApplicant)
                }
            }
        }
        getDropDownsFromDB()
    }

    private fun setUpCurrentApplicantDetails(applicant: BankDetailModel) {
        bankDetailBeanList = applicant.applicantBankDetailsBean
        if (bankDetailBeanList != null && bankDetailBeanList!!.size > 0) {
            setUpBankDetailAdapter(bankDetailBeanList!!)
        }
    }

    private fun setUpBankDetailAdapter(bankDetails: java.util.ArrayList<BankDetailBean>) {
        binding.rcBank.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        bankAdapter = BankDetailAdapter(mContext, bankDetails)
        binding.rcBank.adapter = bankAdapter
        bankAdapter.setOnBankDetailClickListener(this)
        binding.pageIndicatorAsset.attachTo(binding.rcBank)
        binding.pageIndicatorAsset.visibility = View.VISIBLE
        binding.rcBank.visibility = View.VISIBLE
    }

    override fun onApplicantClick(position: Int, coApplicant: Response.CoApplicantsObj) {
        saveCurrentApplicant()
        ClearBankForm(binding, mContext, allMasterDropDown)
        getParticularApplicantData(position, coApplicant)
    }

    private fun saveCurrentApplicant() {
        if (bApplicantsList!!.size > 0) {
            bApplicantsList!![currentPosition] = getCurrentApplicant()
        } else bApplicantsList!!.add(currentPosition, getCurrentApplicant())
    }

    private fun getParticularApplicantData(position: Int, coApplicant: Response.CoApplicantsObj) {
        currentApplicant = if (position >= bApplicantsList!!.size) {
            BankDetailModel()
        } else {
            bApplicantsList!![position]
        }
        currentApplicant.isMainApplicant = coApplicant.isMainApplicant
        currentApplicant.leadApplicantNumber = coApplicant.leadApplicantNumber
        bankDetailBeanList = currentApplicant.applicantBankDetailsBean
    }

    private fun getDataFromDB() {
        dataBase.provideDataBaseSource().bankDetailDao().getBankDetail(leadId).observe(this, Observer { bankInfo ->
            bankInfo?.let {
                bankDetailMaster = bankInfo
                bDraftData = bankDetailMaster.draftData!!
                bApplicantsList = bDraftData.applicantDetails
                if (bApplicantsList!!.size <= 0) {
                    bApplicantsList!!.add(BankDetailModel())
                }
            }
            setCoApplicants()
            showData(bApplicantsList)
        })
    }

    private fun setClickListeners() {
        binding.btnAddBankDetail.setOnClickListener {
            if (formValidation.validateBankDetail(binding)) {
                saveCurrentBean()
                ClearBankForm(binding, mContext, allMasterDropDown)
            } else showToast(getString(R.string.validation_error))
        }
        binding.btnSaveAndContinue.setOnClickListener {
            if (formValidation.validateBankDetail(binding)) {
                saveCurrentBean()
                saveCurrentApplicant()
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            } else showToast(getString(R.string.validation_error))
        }
    }

    private fun saveCurrentBean() {
        if (bankDetailBeanList!!.size > 0) {
            bankDetailBeanList!![currentPosition] = getCurrentBean()
        } else bankDetailBeanList!!.add(currentPosition, getCurrentBean())
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
        currentBean.accountNumber = binding.etAccountNum.text.toString()
        currentBean.numberOfCredit = binding.etSalaryCreditedInSixMonths.text.toString().toInt()
        return currentBean
    }

    private fun getCurrentApplicant(): BankDetailModel {
        val currentApplicant = BankDetailModel()
        currentApplicant.isMainApplicant = currentPosition == 0
        currentApplicant.leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1)
        currentApplicant.applicantBankDetailsBean = bankDetailBeanList!!
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

    override fun onBankDetailDeleteClicked(position: Int) = showAlertDialog(position)

    override fun onBankDetailEditClicked(position: Int, bank: BankDetailBean) {
        currentBean = bank
        fillFormWithCurrentBean(bank)
    }

    private fun fillFormWithCurrentBean(bank: BankDetailBean) {
        binding.etAccountNum.setText(bank.accountNumber)
        binding.etAccountHolderName.setText(bank.accountHolderName)
        binding.etSalaryCreditedInSixMonths.setText(bank.numberOfCredit.toString())
        selectMasterDropdownValue(binding.spinnerSalaryCredit, bank.salaryCreditTypeDetailID)
        selectMasterDropdownValue(binding.spinnerBankName, bank.bankNameTypeDetailID)
        selectMasterDropdownValue(binding.spinnerAccountType, bank.accountTypeDetailID)
    }

    private fun showAlertDialog(position: Int) {
        val deleteDialogView = LayoutInflater.from(activity).inflate(R.layout.delete_dialog, null)
        val mBuilder = AlertDialog.Builder(mContext)
                .setView(deleteDialogView)
                .setTitle("Delete Bank Detail")
        val deleteDialog = mBuilder.show()
        deleteDialogView.tvDeleteConfirm.setOnClickListener { deleteBankDetail(position) }
        deleteDialogView.tvDonotDelete.setOnClickListener { deleteDialog.dismiss() }
    }

    private fun deleteBankDetail(position: Int) {
        bankDetailBeanList!!.removeAt(position)
        binding.rcBank.adapter!!.notifyItemRemoved(position)
        binding.rcBank.adapter!!.notifyItemRangeChanged(position, bankDetailBeanList!!.size)
    }
}
