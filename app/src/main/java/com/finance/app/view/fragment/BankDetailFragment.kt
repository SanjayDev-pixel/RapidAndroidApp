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
import com.finance.app.utility.DisableBankForm
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.utility.RequestConversion
import com.finance.app.utility.ResponseConversion
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.BankDetailAdapter
import com.finance.app.view.adapters.recycler.spinner.MasterSpinnerAdapter
import com.finance.app.view.customViews.CustomSpinnerViewTest
import com.finance.app.view.dialogs.BankDetailDialogFragment
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
        BankDetailAdapter.BankDetailClickListener, BankDetailDialogFragment.OnBankDetailDialogCallback {

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
    private var applicantTabList: ArrayList<CoApplicantsList>? = ArrayList()
    private var bankDetailMaster: BankDetailMaster = BankDetailMaster()
    private var bankDetailDraft = BankDetailList()
    private var applicantsBankDetailList: ArrayList<BankDetailModel>? = ArrayList()

    private var bankDetailBeanList: ArrayList<BankDetailBean>? = ArrayList()

    private var currentApplicant: BankDetailModel = BankDetailModel()

    private var selectedApplicant: CoApplicantsList? = null
    private var selectedTabPosition = 0

    private lateinit var bankName: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var accountType: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var salaryCredit: CustomSpinnerViewTest<DropdownMaster>

    override val leadId: String
        get() = mLead!!.leadID.toString()

    override val storageType: String
        get() = bankDetailMaster.storageType

    override val loanAppRequestPost: LoanApplicationRequest
        get() = requestConversion.bankRequest(getBankDetailMaster())

    companion object {
        private const val SALARIED = 93
        private val leadAndLoanDetail = LeadAndLoanDetail()
        private val responseConversion = ResponseConversion()
        private val requestConversion = RequestConversion()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        ArchitectureApp.instance.component.inject(this)
        mLead = sharedPreferences.getLeadDetail()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail)

        initViews()
        setOnClickListeners()

        return binding.root
    }

    override fun init() {
    }

    private fun initViews() {
        //Set Applicant Tabs View.
        setApplicantTabView()
    }

    private fun setApplicantTabView() {
        dataBase.provideDataBaseSource().coApplicantsDao().getCoApplicants(leadId.toInt()).observe(viewLifecycleOwner, Observer { coApplicantsMaster ->
            coApplicantsMaster?.let {
                if (coApplicantsMaster.coApplicantsList!!.isEmpty()) {
                    applicantTabList?.add(leadAndLoanDetail.getDefaultApplicant(selectedTabPosition, mLead!!.leadNumber!!))
                } else {
                    applicantTabList = coApplicantsMaster.coApplicantsList
                }

                binding.rcApplicants.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                applicantAdapter = ApplicantsAdapter(mContext, applicantTabList!!)
                binding.rcApplicants.adapter = applicantAdapter
                applicantAdapter?.setOnItemClickListener(this)
                selectedApplicant = applicantTabList!![0]
            }
        })
    }

    private fun setOnClickListeners() {
        binding.vwAdd.setOnClickListener { showBankDetailFormDialog(BankDetailDialogFragment.Action.NEW) }
        binding.btnNext.setOnClickListener {
            bankDetailBeanList?.let {
                if (it.size > 0) {
//                    saveCurrentBean(getCurrentBean())
                    saveCurrentApplicant()
                    loanAppPostPresenter.callNetwork(ConstantsApi.CALL_POST_LOAN_APP)
                } else showToast(getString(R.string.validation_error))
            }
        }
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }

//        binding.btnAddBankDetail.setOnClickListener {
//            if (formValidation.validateBankDetail(binding)) {
//                saveCurrentBean(getCurrentBean())
//                ClearBankForm(binding, mContext, allMasterDropDown, bankName, accountType)
//            } else showToast(getString(R.string.validation_error))
//        }

//        binding.btnUpdate.setOnClickListener { updateCurrentBean(bankDetail) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch data from where-ever you want....
        fetchSpinnersDataFromDB()
        fetchBankDetail()
    }

    private fun fetchSpinnersDataFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun fetchBankDetail() {
        loanAppGetPresenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP)
    }

    private fun fetchBankDetailsFromDB() {
        dataBase.provideDataBaseSource().bankDetailDao().getBankDetail(leadId).observe(this, Observer { bankInfo ->
            bankInfo?.let {
                bankDetailMaster = bankInfo
                bankDetailDraft = bankDetailMaster.draftData!!
                applicantsBankDetailList = bankDetailDraft.applicantBankDetails
                //TODO have to implement for failure response....
//                if (applicantsBankDetailList!!.size <= 0) {
//                    applicantsBankDetailList!!.add(BankDetailModel())
//                }
            }
//            setApplicantTabView()
//            showData(applicantsBankDetailList)
        })
    }

    override fun getLoanAppGetSuccess(value: Response.ResponseGetLoanApplication) {
        value.responseObj?.let {
            bankDetailMaster = responseConversion.toBankDetailMaster(value.responseObj)
            bankDetailDraft = bankDetailMaster.draftData!!
            applicantsBankDetailList = bankDetailDraft.applicantBankDetails

            //Now inflate data to views...
            refreshApplicantBankDetails(applicantsBankDetailList)
        }
//        setApplicantTabView()
//        refreshApplicantBankDetails(selectedTabPosition)
//        showData(applicantsBankDetailList)
    }

    override fun getLoanAppGetFailure(msg: String) = fetchBankDetailsFromDB()

    private fun showData(applicantList: ArrayList<BankDetailModel>?) {
        if (!applicantList.isNullOrEmpty()) {
            if (applicantList.size < applicantTabList!!.size) {
                for (tab in applicantList.size..applicantTabList!!.size) {
                    applicantList.add(BankDetailModel())
                }
            }

            for (applicant in applicantList) {
                if (applicant.isMainApplicant) {
                    currentApplicant = applicant
                    currentApplicant.leadApplicantNumber = selectedApplicant?.leadApplicantNumber
                    currentApplicant.isMainApplicant = selectedApplicant?.isMainApplicant!!
                    bankDetailBeanList = currentApplicant.applicantBankDetailsBeanList
                }
            }
        }
//        setUpBankDetailAdapter()
    }

    private fun checkIncomeConsideration() {
//        if (!selectedApplicant.incomeConsidered!!) {
//            DisableBankForm(binding)
//        } else showToast(getString(R.string.error_income_not_considered))
    }

    private fun setBankDetailAdapter(bankDetailList: ArrayList<BankDetailBean>) {
        binding.rcBank.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
//        bankAdapter = BankDetailAdapter(mContext, bankDetailBeanList)
        bankAdapter = BankDetailAdapter(mContext, bankDetailList)
        binding.rcBank.adapter = bankAdapter
        bankAdapter.setOnBankDetailClickListener(this)
        binding.pageIndicatorAsset.attachTo(binding.rcBank)
        binding.pageIndicatorAsset.visibility = View.VISIBLE
        binding.rcBank.visibility = View.VISIBLE
    }

    private fun refreshApplicantBankDetails(allApplicantsBankDetailList: ArrayList<BankDetailModel>?) {
        allApplicantsBankDetailList?.let { mainList ->
            val selectedApplicantBankDetailList = mainList.filter { it.leadApplicantNumber.equals(selectedApplicant?.leadApplicantNumber, true) }
            selectedApplicantBankDetailList?.let { childList ->
                if (childList.isNotEmpty() && childList[0].applicantBankDetailsBeanList.isNotEmpty()) {
                    //Always have one item in the list after filtering, for now as developer knows....
                    setBankDetailAdapter(childList[0].applicantBankDetailsBeanList)
                }
            }
        }
    }


    override fun onApplicantClick(position: Int, coApplicant: CoApplicantsList) {
        selectedApplicant = coApplicant
        selectedTabPosition = position
        refreshApplicantBankDetails(applicantsBankDetailList)

//        if (bankDetailBeanList != null && bankDetailBeanList!!.size > 0) {
//            saveCurrentApplicant()
//            selectedTabPosition = position
//            selectedApplicant = coApplicant
////            ClearBankForm(binding, mContext, allMasterDropDown, bankName, accountType)
//            getParticularApplicantData(position)
//        }
    }


    private fun saveCurrentApplicant() {
        if (applicantsBankDetailList!!.size > 0) {
            applicantsBankDetailList!![selectedTabPosition] = getCurrentApplicant()
        } else applicantsBankDetailList!!.add(selectedTabPosition, getCurrentApplicant())
    }

    private fun getParticularApplicantData(position: Int) {
        currentApplicant = if (position >= applicantsBankDetailList!!.size) {
            BankDetailModel()
        } else applicantsBankDetailList!![position]
        currentApplicant.isMainApplicant = selectedApplicant?.isMainApplicant!!
        currentApplicant.leadApplicantNumber = selectedApplicant?.leadApplicantNumber
        bankDetailBeanList = currentApplicant.applicantBankDetailsBeanList
        if (bankDetailBeanList != null || bankDetailBeanList!!.size > 0) {
            bankAdapter.notifyDataSetChanged()
        }
        applicantAdapter!!.notifyDataSetChanged()
    }


    private fun updateCurrentBean(bankDetail: BankDetailBean) {
        bankDetailBeanList!!.add(selectedTabPosition, bankDetail)
        bankAdapter.notifyDataSetChanged()
//        ClearBankForm(binding, mContext, allMasterDropDown, bankName, accountType)
//        binding.btnAddBankDetail.visibility = View.VISIBLE
//        binding.btnUpdate.visibility = View.GONE
    }

    private fun saveCurrentBean(bankDetail: BankDetailBean) {
//        bankDetailBeanList?.add(getCurrentBean())
        bankDetailBeanList?.add(bankDetail)
        bankAdapter.notifyDataSetChanged()
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
        currentApplicant.leadApplicantNumber = leadAndLoanDetail.getLeadApplicantNum(selectedTabPosition + 1, mLead!!.leadNumber!!)
        currentApplicant.isMainApplicant = selectedTabPosition == 0
        currentApplicant.applicantBankDetailsBeanList = bankDetailBeanList!!
        return currentApplicant
    }

    private fun getBankDetailMaster(): BankDetailMaster {
        bankDetailDraft.applicantBankDetails = applicantsBankDetailList
        bankDetailMaster.draftData = bankDetailDraft
        bankDetailMaster.leadID = leadId.toInt()
        return bankDetailMaster
    }

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
//        fillFormWithCurrentBean(bank)
//        binding.btnAddBankDetail.visibility = View.GONE
//        binding.btnUpdate.visibility = View.VISIBLE
        showBankDetailFormDialog(
                if (mLead?.status == AppEnums.LEAD_TYPE.SUBMITTED.type) BankDetailDialogFragment.Action.SUBMITTED
                else BankDetailDialogFragment.Action.EDIT, bank)
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

    private fun showBankDetailFormDialog(action: BankDetailDialogFragment.Action, bankDetail: BankDetailBean? = null) {
        BankDetailDialogFragment.newInstance(action, this@BankDetailFragment, allMasterDropDown, bankDetail).show(fragmentManager, "Bank Detail")
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

    override fun onSaveBankDetail(bankDetail: BankDetailBean) {
        saveCurrentBean(bankDetail)
    }

    override fun onEditBankDetail(bankDetail: BankDetailBean) {
        updateCurrentBean(bankDetail)
    }

    override fun onDeleteBankDetail() {
    }

}
