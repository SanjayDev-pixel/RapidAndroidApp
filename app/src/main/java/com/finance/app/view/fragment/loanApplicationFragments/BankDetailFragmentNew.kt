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
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.BankDetailBean
import com.finance.app.persistence.model.BankDetailModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadMaster
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
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
    private var applicantsBankDetailList: ArrayList<BankDetailModel>? = ArrayList()

    private var selectedApplicant: PersonalApplicantsModel? = null
    private var selectedBankDetailPosition = -1


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
        //Set Applicant Tabs View.
        setApplicantTabView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch data from where-ever you want....
        fetchSpinnersDataFromDB()
        fetchBankDetail()
    }

    private fun setApplicantTabView() {
        leadMaster?.personalData?.applicantDetails?.let { applicantList ->
            //Set Applicant Tab Adapter...
            setApplicantTabLayout(applicantList)
        }
    }

    private fun setApplicantTabLayout(applicantList: ArrayList<PersonalApplicantsModel>) {
        applicantList.let { list ->
            list.forEachIndexed { index, personalApplicantsModel ->
                //Default Selected Tab
                if (personalApplicantsModel.isMainApplicant) selectedApplicant = personalApplicantsModel
                //Create tabs...
                binding.tabLead.addTab(binding.tabLead.newTab().setText(
                        if (personalApplicantsModel.isMainApplicant) "Applicant" else "Co-Applicant$index")
                        .setTag(personalApplicantsModel)) //Add tagging to get data on tab change....
            }
        }
    }

    private fun setOnClickListeners() {
        binding.tabLead.addOnTabSelectedListener(this)
        binding.vwAdd.setOnClickListener { showBankDetailFormDialog(BankDetailDialogFragment.Action.NEW) }
        binding.btnNext.setOnClickListener { }
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
    }

    private fun setBankDetailAdapter(bankDetailList: ArrayList<BankDetailBean>) {
        binding.rcBank.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        bankAdapter = BankDetailAdapter(mContext, bankDetailList)
        binding.rcBank.adapter = bankAdapter
        bankAdapter?.setOnBankDetailClickListener(this)
        binding.pageIndicatorAsset.attachTo(binding.rcBank)
        binding.pageIndicatorAsset.visibility = View.VISIBLE
        binding.rcBank.visibility = View.VISIBLE
    }

    private fun fetchSpinnersDataFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                allMasterDropDown = it
            }
        })
    }

    private fun fetchBankDetail() {
        leadMaster?.bankData?.applicantBankDetails?.let { list ->
            applicantsBankDetailList = list
            refreshApplicantBankDetails(list)
        }
    }

    private fun refreshApplicantBankDetails(allApplicantsBankDetailList: ArrayList<BankDetailModel>?) {
        allApplicantsBankDetailList?.let { mainList ->
            val selectedApplicantBankDetailList = mainList.filter { it.leadApplicantNumber.equals(selectedApplicant?.leadApplicantNumber, true) }
            selectedApplicantBankDetailList?.let { childList ->
                if (childList.isNotEmpty()) {
                    //Always have one item in the list after filtering, for now as developer knows....
                    setBankDetailAdapter(childList[0].applicantBankDetailsBean)
                }
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        selectedApplicant = tab?.tag as PersonalApplicantsModel
        refreshApplicantBankDetails(applicantsBankDetailList)
    }

    override fun onBankDetailDeleteClicked(position: Int) {
        selectedBankDetailPosition = position
        showBankDetailConfirmDeleteDialog()
    }

    override fun onBankDetailEditClicked(position: Int, bank: BankDetailBean) {
        selectedBankDetailPosition = position
        showBankDetailFormDialog(
                if (leadMaster?.status == AppEnums.LEAD_TYPE.SUBMITTED.type) BankDetailDialogFragment.Action.SUBMITTED
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
