package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadDetailBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.view.adapters.recycler.adapter.LeadDetailActivityAdapter
import com.finance.app.viewModel.LeadDataViewModel
import motobeans.architecture.appDelegates.ViewModelType
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LeadDetailActivity : BaseAppCompatActivity() {

    private val binding: ActivityLeadDetailBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_lead_detail)
    private val leadDataViewModel: LeadDataViewModel by motobeans.architecture.appDelegates.viewModelProvider(this, ViewModelType.WITH_DAO)
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private var bundle: Bundle? = null
    private var lead = AllLeadMaster()
    private var leadContact: Long = 0

    companion object {
        private const val KEY_LEAD = "leadApplicant"
        fun start(context: Context, lead: AllLeadMaster) {
            val intent = Intent(context, LeadDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(KEY_LEAD, lead)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun init() {
//        showLeadOptionsMenu()
        ArchitectureApp.instance.component.inject(this)
        hideToolbar()
        hideSecondaryToolbar()
        getLead()
    }

    private fun getLead() {
        bundle = intent.extras
        bundle?.let {
            lead = bundle!!.getSerializable(KEY_LEAD) as AllLeadMaster
            fillDataOnScreen(lead)
            sharedPreferences.saveLeadDetail(lead)
            leadDataViewModel.getLeadData(lead.leadID.toString())
        }
    }

    private fun fillDataOnScreen(lead: AllLeadMaster?) {
        binding.tvLeadName
        binding.tvEmail.text = lead?.applicantEmail
        val leadName = lead?.applicantFirstName + " " + lead?.applicantLastName
        setLeadNum(lead?.leadNumber!!)
        binding.tvLeadName.text = leadName
        binding.header.tvLeadNumber.text = lead.leadNumber
        binding.tvLocation.text = lead.applicantAddress
        binding.tvPhone.text = lead.applicantContactNumber
        binding.tvTypeOfLoan.text = lead.loanProductName
        binding.tvLeadStatus.text = lead.status
        leadContact = lead.applicantContactNumber!!.toLong()
        setUpRecyclerView()
        setClickListeners()
        fillColor(lead)
    }

    private fun fillColor(lead: AllLeadMaster) {
        when (lead.status) {
            AppEnums.LEAD_TYPE.PENDING.type -> binding.tvLeadStatus.setTextColor(resources.getColor(R.color.lead_status_pending))
            AppEnums.LEAD_TYPE.SUBMITTED.type -> binding.tvLeadStatus.setTextColor(resources.getColor(R.color.lead_status_submitted))
            AppEnums.LEAD_TYPE.REJECTED.type -> binding.tvLeadStatus.setTextColor(resources.getColor(R.color.lead_status_rejected))
            else -> binding.tvLeadStatus.setTextColor(resources.getColor(R.color.lead_status_new))
        }
    }

    private fun setClickListeners() {
        binding.header.lytBack.setOnClickListener { onBackPressed() }

        binding.llLeadDetail.setOnClickListener {
            LoanApplicationActivity.start(this)
        }

        binding.ivCall.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel: +91${leadContact}")
            startActivity(callIntent)
        }

        binding.btnUpdateCall.setOnClickListener {
            UpdateCallActivity.start(this, lead)
        }

        binding.btnAddTask.setOnClickListener {
            AddTaskActivity.start(this)
        }
    }

    private fun setUpRecyclerView() {
        binding.rcActivities.layoutManager = LinearLayoutManager(this)
        binding.rcActivities.adapter = LeadDetailActivityAdapter(this)
    }

}
