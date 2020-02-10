package com.finance.app.view.fragment.loanApplicationFragments.bank


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.FragmentBankDetailFormBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.BankDetailBean
import com.finance.app.persistence.model.BankDetailModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.BankDetailAdapter
import com.finance.app.view.dialogs.BankDetailDialogFragment
import kotlinx.android.synthetic.main.delete_dialog.view.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BankDetailFormFragment : BaseFragment(), BankDetailDialogFragment.OnBankDetailDialogCallback, BankDetailAdapter.ItemClickListener {


    private lateinit var mContext: Context

    @Inject
    lateinit var dataBase: DataBaseUtil

    private var bankAdapter: BankDetailAdapter? = null
    private var allMasterDropDown: AllMasterDropDown? = null

    private lateinit var selectedApplicant: PersonalApplicantsModel
    private var selectedBankDetailPosition = -1

    private lateinit var binding: FragmentBankDetailFormBinding

    companion object {
        fun newInstance(selectedApplicant: PersonalApplicantsModel): BankDetailFormFragment {
            val fragment = BankDetailFormFragment()
            fragment.selectedApplicant = selectedApplicant
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        ArchitectureApp.instance.component.inject(this)
    }

    override fun init() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_bank_detail_form)
        binding.lifecycleOwner = this

        initViews()
        setOnClickListeners()

        return view
    }

    private fun initViews() {
    }

    private fun setOnClickListeners() {
        binding.vwAdd.setOnClickListener { showBankDetailFormDialog(BankDetailDialogFragment.Action.NEW) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch data from where-ever you want....
        fetchLeadBankDetail()
        fetchSpinnersDataFromDB()
    }

    private fun setBankDetailAdapter(bankDetailList: ArrayList<BankDetailBean>) {
        binding.rcBank.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        bankAdapter = BankDetailAdapter(mContext, bankDetailList)
        binding.rcBank.adapter = bankAdapter
        bankAdapter?.setOnItemClickListener(this)
    }

    private fun fetchLeadBankDetail() {
        LeadMetaData.getLeadObservable().observe(this@BankDetailFormFragment, Observer {
            it?.let { leadDetails ->
                val selectedApplicantBankDetails = leadDetails.bankData.applicantBankDetails.filter { bankDetail -> bankDetail.leadApplicantNumber.equals(selectedApplicant.leadApplicantNumber, true) }
                if (selectedApplicantBankDetails.isNotEmpty()) //TODO need to solve this bug...
                    setBankDetailAdapter(selectedApplicantBankDetails[0].applicantBankDetailsBean)
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

    private fun showBankDetailFormDialog(action: BankDetailDialogFragment.Action, bankDetail: BankDetailBean? = null) {
        allMasterDropDown?.let { BankDetailDialogFragment.newInstance(action, this@BankDetailFormFragment, it, bankDetail).show(fragmentManager, "Bank Detail") }
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

    override fun onBankDetailDeleteClicked(position: Int) {
        selectedBankDetailPosition = position
        showBankDetailConfirmDeleteDialog()
    }

    override fun onBankDetailEditClicked(position: Int, bank: BankDetailBean) {
        selectedBankDetailPosition = position
        showBankDetailFormDialog(BankDetailDialogFragment.Action.EDIT, bank)
//        showBankDetailFormDialog(
//                if (leadDetails?.status == AppEnums.LEAD_TYPE.SUBMITTED.type) BankDetailDialogFragment.Action.SUBMITTED
//                else BankDetailDialogFragment.Action.EDIT, bank)
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

    fun getApplicantBankDetails(): BankDetailModel {
        val bankDetailModel = BankDetailModel()
        bankDetailModel.firstName = selectedApplicant.firstName
        bankDetailModel.leadApplicantNumber = selectedApplicant.leadApplicantNumber
        bankDetailModel.isMainApplicant = selectedApplicant.isMainApplicant
        bankDetailModel.applicantBankDetailsBean = bankAdapter?.getItemList() ?: ArrayList()

        return bankDetailModel
    }

}
