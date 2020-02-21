package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.LayoutKycFormBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.KYCDetail
import com.finance.app.utility.ConvertDate
import com.finance.app.utility.SelectDate
import com.finance.app.view.adapters.recycler.adapter.KycListAdapter
import com.finance.app.view.adapters.recycler.spinner.MasterSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.util.AppUtilExtensions
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class KycFormView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation

    private var lifecycleOwner: LifecycleOwner? = null
    private var allMasterDropDown: AllMasterDropDown? = null
    private var kycListAdapter: KycListAdapter? = null


    private var rootBinding: LayoutKycFormBinding = AppUtilExtensions.initCustomViewBinding(context = context, layoutId = R.layout.layout_kyc_form, container = this)

    fun bindApplicantKycDetails(owner: LifecycleOwner, kycDetailList: ArrayList<KYCDetail>) {
        ArchitectureApp.instance.component.inject(this)
        lifecycleOwner = owner

        setOnClickListener()
        //Set Kyc Adapter..
        setKycDetailListAdapter(kycDetailList)
        //fetch spinners data from db...
        fetchSpinnersDataFromDB()
        //Check whether kyc list has items or not....
        shouldDisplayKycListViews()
    }

    private fun setOnClickListener() {
        rootBinding.vwAdd.setOnClickListener { showKycForm(true) }
        rootBinding.btnAddKYC.setOnClickListener {
            if (isKycDetailsValid()) {
                addKycDetails();showKycForm(false)
            }
        }
        rootBinding.btnCancel.setOnClickListener { showKycForm(false) }
        rootBinding.btnUploadKyc.setOnClickListener { }
        rootBinding.etIssueDate.setOnClickListener { SelectDate(rootBinding.etIssueDate, context, maxDate = Date().time) }
        rootBinding.etExpiryDate.setOnClickListener { SelectDate(rootBinding.etExpiryDate, context, minDate = Date().time) }
    }

    private fun setKycDetailListAdapter(list: ArrayList<KYCDetail>) {
        kycListAdapter = KycListAdapter(context, list)
        rootBinding.rcKycList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rootBinding.rcKycList.adapter = kycListAdapter
        lifecycleOwner?.let {
            kycListAdapter?.getItemCountObserver()?.observe(it, Observer { listCount ->
                if (listCount > 0) {
                    showKycListView(true);showKycEmptyView(false)
                } else {
                    showKycListView(false);showKycEmptyView(true)
                }
            })
        }
    }

    private fun shouldDisplayKycListViews() {
        kycListAdapter?.let { adapter ->
            if (adapter.itemCount > 0) {
                showKycListView(true);showKycEmptyView(false)
            } else {
                showKycListView(false);showKycEmptyView(true)
            }
        }
    }

    private fun showKycForm(show: Boolean) {
        rootBinding.vwKycForm.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showKycListView(show: Boolean) {
        rootBinding.rcKycList.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showKycEmptyView(show: Boolean) {
        rootBinding.vwEmpty.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun fetchSpinnersDataFromDB() {
        lifecycleOwner?.let { lifecycleOwner ->
            dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(lifecycleOwner, Observer { masterDrownDownValues ->
                masterDrownDownValues?.let {
                    allMasterDropDown = it
                    setKycSpinner(it)
                }
            })
        }
    }

    private fun setKycSpinner(allMasterDropDown: AllMasterDropDown) {
        rootBinding.spinnerIdentificationType.adapter = MasterSpinnerAdapter(context, allMasterDropDown.IdentificationType!!)
        rootBinding.spinnerVerifiedStatus.adapter = MasterSpinnerAdapter(context, allMasterDropDown.VerifiedStatus!!)
    }

    private fun addKycDetails() {
        val kycDetail = KYCDetail()
        kycDetail.identificationTypeDetailID = (rootBinding.spinnerIdentificationType.selectedItem as DropdownMaster?)?.typeDetailID
        kycDetail.identificationNumber = rootBinding.etIdNum.text.toString()
        kycDetail.issueDate = ConvertDate().convertToApiFormat(rootBinding.etIssueDate.text.toString())
        kycDetail.expireDate = ConvertDate().convertToApiFormat(rootBinding.etExpiryDate.text.toString())
        kycDetail.verifiedStatusTypeDetailID = (rootBinding.spinnerVerifiedStatus.selectedItem as DropdownMaster?)?.typeDetailID

        kycListAdapter?.addItem(kycDetail)
    }

    private fun isKycDetailsValid() = formValidation.validateKycDetail(rootBinding)

    fun getKycDetailsList(): ArrayList<KYCDetail> = kycListAdapter?.getItemList() ?: ArrayList()

}