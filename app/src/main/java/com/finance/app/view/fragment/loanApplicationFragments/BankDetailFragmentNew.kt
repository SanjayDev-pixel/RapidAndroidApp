package com.finance.app.view.fragment.loanApplicationFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.BankDetailAdapter
import com.finance.app.view.dialogs.BankDetailDialogFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.delete_dialog.view.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject


/**
 * Created by Ajay on 28/1/2020.
 */
class BankDetailFragmentNew : BaseFragment(), BankDetailDialogFragment.OnBankDetailDialogCallback, BankDetailAdapter.BankDetailClickListener, TabLayout.OnTabSelectedListener {
    @Inject
    lateinit var dataBase: DataBaseUtil

    private lateinit var mContext: Context

    private var bankAdapter: BankDetailAdapter? = null
    private var allMasterDropDown: AllMasterDropDown? = null

    private var selectedApplicant: PersonalApplicantsModel? = null
    private var selectedBankDetailPosition = -1

    private var leadDetails: AllLeadMaster? = null

    companion object {
        fun newInstance(): BankDetailFragmentNew {
            return BankDetailFragmentNew()
        }
    }

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    private lateinit var binding: FragmentBankDetailBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail)
        binding.lifecycleOwner = this

        initViews()
        setOnClickListeners()

        return view
    }

    override fun init() {
    }

    private fun initViews() {
    }

    private fun setOnClickListeners() {
        binding.tabLead.addOnTabSelectedListener(this)
        binding.vwAdd.setOnClickListener { showBankDetailFormDialog(BankDetailDialogFragment.Action.NEW) }
        binding.btnNext.setOnClickListener {
            leadDetails?.let { it.bankData.applicantBankDetails?.let { bankDetails -> LeadMetaData().saveBankData(bankDetails) } }
            AppEvents.fireEventLoanAppChangeNavFragmentNext()
        }
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch data from where-ever you want....
        fetchLeadDetails()
        fetchSpinnersDataFromDB()
    }

    private fun setApplicantTabLayout(applicantList: ArrayList<PersonalApplicantsModel>) {
        applicantList.let { list ->
            list.forEachIndexed { index, personalApplicantsModel ->
                //Default Selected Tab
                if (personalApplicantsModel.isMainApplicant) selectedApplicant = personalApplicantsModel
                //Create tabs...
                binding.tabLead.addTab(binding.tabLead.newTab().setText(
                        if (personalApplicantsModel.isMainApplicant) "Applicant" else "Co-Applicant $index")
                        .setTag(personalApplicantsModel)) //Add tagging to get data on tab change....
            }
        }
    }

    private fun setBankDetailAdapter(bankDetailList: ArrayList<BankDetailBean>) {
        binding.rcBank.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        bankAdapter = BankDetailAdapter(mContext, bankDetailList)
        binding.rcBank.adapter = bankAdapter
        bankAdapter?.setOnBankDetailClickListener(this)
        binding.rcBank.visibility = View.VISIBLE
    }

    private fun fetchLeadDetails() {
        LeadMetaData.getLeadObservable().observe(this, Observer { leadDetails ->
            leadDetails?.let {
                this@BankDetailFragmentNew.leadDetails = it
                //Set Applicant Tabs View.
                setApplicantTabLayout(it.personalData.applicantDetails)
                //Set Bank Details List.
                setBankDetailAdapter(filterApplicantBankDetailsBySelectedApplicant(it.bankData.applicantBankDetails))
            }
        })
    }

    private fun fetchSpinnersDataFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                allMasterDropDown = it
            }
        })
    }

    private fun filterApplicantBankDetailsBySelectedApplicant(allApplicantsBankDetailList: ArrayList<BankDetailModel>?): ArrayList<BankDetailBean> {
        allApplicantsBankDetailList?.let { mainList ->
            val selectedApplicantBankDetailList = mainList.filter { it.leadApplicantNumber.equals(selectedApplicant?.leadApplicantNumber, true) }
            selectedApplicantBankDetailList.let { childList ->
                if (childList.isNotEmpty()) {
                    //Always have one item in the list after filtering, for now as developer knows....
                    return childList[0].applicantBankDetailsBean
                }
            }
        }
        //return empty array if there is no data found...
        return ArrayList()
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        selectedApplicant = tab?.tag as PersonalApplicantsModel
        setBankDetailAdapter(filterApplicantBankDetailsBySelectedApplicant(leadDetails?.bankData?.applicantBankDetails))
    }

    override fun onBankDetailDeleteClicked(position: Int) {
        selectedBankDetailPosition = position
        showBankDetailConfirmDeleteDialog()
    }

    override fun onBankDetailEditClicked(position: Int, bank: BankDetailBean) {
        selectedBankDetailPosition = position
        showBankDetailFormDialog(
                if (leadDetails?.status == AppEnums.LEAD_TYPE.SUBMITTED.type) BankDetailDialogFragment.Action.SUBMITTED
                else BankDetailDialogFragment.Action.EDIT, bank)
    }

    private fun showBankDetailFormDialog(action: BankDetailDialogFragment.Action, bankDetail: BankDetailBean? = null) {
        allMasterDropDown?.let { BankDetailDialogFragment.newInstance(action, this@BankDetailFragmentNew, it, bankDetail).show(fragmentManager, "Bank Detail") }
    }

    private fun showBankDetailConfirmDeleteDialog() {
        val deleteDialogView = LayoutInflater.from(activity).inflate(R.layout.delete_dialog, null)
        val mBuilder = AlertDialog.Builder(mContext)
                .setView(deleteDialogView)
                .setTitle("Delete Bank Detail")
        val deleteDialog = mBuilder.show()
        deleteDialogView.tvDeleteConfirm.setOnClickListener {
            onDeleteBankDetail()
            deleteDialog.dismiss()
        }
        deleteDialogView.tvDonotDelete.setOnClickListener { deleteDialog.dismiss() }
    }

    override fun onSaveBankDetail(bankDetail: BankDetailBean) {
        bankAdapter?.addItem(bankDetail = bankDetail)
    }

    override fun onEditBankDetail(bankDetail: BankDetailBean) {
        bankAdapter?.updateItem(selectedBankDetailPosition, bankDetail)
    }

    private fun onDeleteBankDetail() {
        bankAdapter?.deleteItem(selectedBankDetailPosition)
    }

}
