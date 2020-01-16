package com.finance.app.view.fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.presenter.LoanAppGetPresenter
import com.finance.app.presenter.presenter.LoanAppPostPresenter
import com.finance.app.utility.*
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.BankDetailAdapter
import com.finance.app.view.customViews.CustomSpinnerViewTest
import fr.ganfra.materialspinner.MaterialSpinner
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
    private val loanAppGetPresenter = LoanAppGetPresenter(GetLoanApp = this)
    private val loanAppPostPresenter = LoanAppPostPresenter(this)
    private var applicantAdapter: ApplicantsAdapter? = null
    private lateinit var bankAdapter: BankDetailAdapter
    private var applicantTab: ArrayList<CoApplicantsList>? = ArrayList()
    private var bankDetailMaster: BankDetailMaster = BankDetailMaster()
    private var bDraftData = BankDetailList()
    private var bApplicantsList: ArrayList<BankDetailModel>? = ArrayList()
    private var bankDetailBeanList: ArrayList<BankDetailBean>? = ArrayList()
    private var currentApplicant: BankDetailModel = BankDetailModel()
    private var currentPosition = 0
    private lateinit var currentTab: CoApplicantsList
    private lateinit var bankName: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var accountType: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var salaryCredit: CustomSpinnerViewTest<DropdownMaster>

    companion object {
        private const val SALARIED = 93
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
        setClickListeners()
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
        dataBase.provideDataBaseSource().coApplicantsDao().getCoApplicants(mLead!!.leadID!!).observe(viewLifecycleOwner, Observer { coApplicantsMaster ->
            coApplicantsMaster.let {
                if (coApplicantsMaster.coApplicantsList!!.isEmpty()) {
                    applicantTab?.add(leadAndLoanDetail.getDefaultApplicant(currentPosition, mLead!!.leadNumber!!))
                } else {
                    applicantTab = coApplicantsMaster.coApplicantsList
                }

                binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false)
                applicantAdapter = ApplicantsAdapter(context!!, applicantTab!!)
                binding.rcApplicants.adapter = applicantAdapter
                applicantAdapter!!.setOnItemClickListener(this)
                currentTab = applicantTab!![0]
            }
        })
    }

    private fun showData(applicantList: ArrayList<BankDetailModel>?) {
        if (!applicantList.isNullOrEmpty()) {
            if (applicantList.size < applicantTab!!.size) {
                for (tab in applicantList.size..applicantTab!!.size) {
                    applicantList.add(BankDetailModel())
                }
            }

            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    currentApplicant.leadApplicantNumber = currentTab.leadApplicantNumber
                    currentApplicant.isMainApplicant = currentTab.isMainApplicant!!
                    bankDetailBeanList = currentApplicant.applicantBankDetailsBean
                }
            }
        }
        setUpBankDetailAdapter()
        getDropDownsFromDB()
    }

    private fun checkIncomeConsideration() {
        if (!currentTab.incomeConsidered!!) {
            DisableBankForm(binding)
        } else showToast(getString(R.string.error_income_not_considered))
    }

    private fun setUpBankDetailAdapter() {
        binding.rcBank.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        bankAdapter = BankDetailAdapter(mContext, bankDetailBeanList)
        binding.rcBank.adapter = bankAdapter
        bankAdapter.setOnBankDetailClickListener(this)
        binding.pageIndicatorAsset.attachTo(binding.rcBank)
        binding.pageIndicatorAsset.visibility = View.VISIBLE
        binding.rcBank.visibility = View.VISIBLE
    }

    override fun onApplicantClick(position: Int, coApplicant: CoApplicantsList) {
        if (bankDetailBeanList != null && bankDetailBeanList!!.size > 0) {
            saveCurrentApplicant()
            currentPosition = position
            currentTab = coApplicant
            ClearBankForm(binding, mContext, allMasterDropDown, bankName, accountType)
            getParticularApplicantData(position)
        } else showToast(getString(R.string.mandatory_field_missing))
    }

    private fun saveCurrentApplicant() {
        if (bApplicantsList!!.size > 0) {
            bApplicantsList!![currentPosition] = getCurrentApplicant()
        } else bApplicantsList!!.add(currentPosition, getCurrentApplicant())
    }

    private fun getParticularApplicantData(position: Int) {
        currentApplicant = if (position >= bApplicantsList!!.size) {
            BankDetailModel()
        } else bApplicantsList!![position]
        currentApplicant.isMainApplicant = currentTab.isMainApplicant!!
        currentApplicant.leadApplicantNumber = currentTab.leadApplicantNumber
        bankDetailBeanList = currentApplicant.applicantBankDetailsBean
        if (bankDetailBeanList != null || bankDetailBeanList!!.size > 0) {
            bankAdapter.notifyDataSetChanged()
        }
        applicantAdapter!!.notifyDataSetChanged()
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
                ClearBankForm(binding, mContext, allMasterDropDown, bankName, accountType)
            } else showToast(getString(R.string.validation_error))
        }
        binding.btnNext.setOnClickListener {
            if (bankDetailBeanList!!.size > 0 || formValidation.validateBankDetail(binding)) {
                saveCurrentBean()
                saveCurrentApplicant()
                loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
            } else showToast(getString(R.string.validation_error))
        }
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnUpdate.setOnClickListener { updateCurrentBean() }
    }

    private fun updateCurrentBean() {
        bankDetailBeanList!!.add(currentPosition, getCurrentBean())
        bankAdapter.notifyDataSetChanged()
        ClearBankForm(binding, mContext, allMasterDropDown, bankName, accountType)
        binding.btnAddBankDetail.visibility = View.VISIBLE
        binding.btnUpdate.visibility = View.GONE
    }

    private fun saveCurrentBean() {
        bankDetailBeanList?.add(getCurrentBean())
        bankAdapter.notifyDataSetChanged()
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
        setCustomSpinner(allMasterDropDown)
        setUpSalaryCreditDropDown(binding.spinnerSalaryCredit, allMasterDropDown)
    }

    private fun setCustomSpinner(allMasterDropDown: AllMasterDropDown) {
        bankName = CustomSpinnerViewTest(context = mContext, dropDowns = allMasterDropDown.BankName!!, label = "Bank Name")
        binding.layoutBankName.addView(bankName)
        accountType = CustomSpinnerViewTest(context = mContext, dropDowns = allMasterDropDown.AccountType!!, label = "Account Type")
        binding.layoutAccountType.addView(accountType)
    }

    private fun setUpSalaryCreditDropDown(spinner: MaterialSpinner, dropDown: AllMasterDropDown) {
        spinner.adapter = MasterSpinnerAdapter(mContext, dropDown.SalaryCredit!!)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val salaryCredit = parent.selectedItem as DropdownMaster
                    if (salaryCredit.typeDetailID == SALARIED) binding.inputLayoutSalaryCreditInSixMonth.visibility = View.VISIBLE
                    else binding.inputLayoutSalaryCreditInSixMonth.visibility = View.GONE
                }
            }
        }
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
        val bName = bankName.getSelectedValue()
        val aType = accountType.getSelectedValue()
        val salaryCredit = binding.spinnerSalaryCredit.selectedItem as DropdownMaster?

        currentBean.bankNameTypeDetailID = bName?.typeDetailID
        currentBean.accountTypeDetailID = aType?.typeDetailID
        currentBean.salaryCreditTypeDetailID = salaryCredit?.typeDetailID
        currentBean.accountHolderName = binding.etAccountHolderName.text.toString()
        currentBean.accountNumber = binding.etAccountNum.text.toString()
        currentBean.numberOfCredit = binding.etSalaryCreditedInSixMonths.text.toString()
        currentBean.salaryCreditTypeDetailID = salaryCredit?.typeDetailID
        return currentBean
    }

    private fun getCurrentApplicant(): BankDetailModel {
        val currentApplicant = BankDetailModel()
        currentApplicant.leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(currentPosition + 1, mLead!!.leadNumber!!)
        currentApplicant.isMainApplicant = currentPosition == 0
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
        AppEvents.fireEventLoanAppChangeNavFragmentNext()
    }

    private fun saveDataToDB(bankDetail: BankDetailMaster) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().bankDetailDao().insertBankDetail(bankDetail)
        }
    }

    override fun onBankDetailDeleteClicked(position: Int) = showAlertDialog(position)

    override fun onBankDetailEditClicked(position: Int, bank: BankDetailBean) {
        fillFormWithCurrentBean(bank)
        binding.btnAddBankDetail.visibility = View.GONE
        binding.btnUpdate.visibility = View.VISIBLE
    }

    private fun fillFormWithCurrentBean(bank: BankDetailBean) {
        selectMasterDropdownValue(binding.spinnerSalaryCredit, bank.salaryCreditTypeDetailID)
        bankName.setSelection(bank.bankNameTypeDetailID?.toString())
        accountType.setSelection(bank.accountTypeDetailID?.toString())
        binding.etAccountNum.setText(bank.accountNumber)
        binding.etAccountHolderName.setText(bank.accountHolderName)
        binding.etSalaryCreditedInSixMonths.setText(bank.numberOfCredit.toString())

        checkSubmission()
    }

    private fun checkSubmission() {
        if (mLead!!.status == AppEnums.LEAD_TYPE.SUBMITTED.type) {
            DisableBankForm(binding)
        }
    }

    private fun showAlertDialog(position: Int) {
        val deleteDialogView = LayoutInflater.from(activity).inflate(R.layout.delete_dialog, null)
        val mBuilder = AlertDialog.Builder(mContext)
                .setView(deleteDialogView)
                .setTitle("Delete Bank Detail")
        val deleteDialog = mBuilder.show()
        deleteDialogView.tvDeleteConfirm.setOnClickListener {
            deleteBankDetail(position)
            deleteDialog.dismiss()
        }
        deleteDialogView.tvDonotDelete.setOnClickListener { deleteDialog.dismiss() }
    }

    private fun deleteBankDetail(position: Int) {
        bankDetailBeanList!!.removeAt(position)
        binding.rcBank.adapter!!.notifyItemRemoved(position)
    }
}
