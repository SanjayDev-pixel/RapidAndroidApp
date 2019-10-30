package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadDetailBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.adapters.recycler.adapter.LeadDetailActivityAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LeadDetailActivity : BaseAppCompatActivity() {

    private val binding: ActivityLeadDetailBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_lead_detail)
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private var bundle: Bundle? = null
    private var leadID = 0
    private var leadContact:Long = 0

    companion object {
        private const val KEY_LEAD_ID = "leadId"
        fun start(context: Context, leadID: Int?) {
            val intent = Intent(context, LeadDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(KEY_LEAD_ID, leadID!!)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun init() {
//        showLeadOptionsMenu()
        ArchitectureApp.instance.component.inject(this)
        getLeadId()
    }

    private fun getLeadId() {
        bundle = intent.extras
        bundle?.let {
            leadID = bundle!!.getInt(KEY_LEAD_ID)
        }
        getLeadFormDB(leadID)
    }

    private fun getLeadFormDB(leadID: Int) {
        dataBase.provideDataBaseSource().allLeadsDao().getLead(leadID)
                .observe(this, Observer { lead ->
                    fillDataOnScreen(lead)
                    sharedPreferences.saveLeadDetail(lead)
                })
    }

    private fun fillDataOnScreen(lead: AllLeadMaster?) {
        binding.tvEmail.text = lead?.applicantEmail
        val leadName = lead?.applicantFirstName + lead?.applicantLastName
        binding.tvLeadName.text = leadName
        binding.tvLeadNumber.text = lead?.leadNumber
        binding.tvLocation.text = lead?.applicantAddress
        binding.tvPhone.text = lead?.applicantContactNumber
        binding.tvTypeOfLoan.text = lead?.loanProductName
        leadContact = lead!!.applicantContactNumber!!.toLong()
        setUpRecyclerView()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnUpdateApplication.setOnClickListener {
            LoanApplicationActivity.start(this)
        }

        binding.btnCallToCustomer.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel: +91${leadContact}")
            startActivity(callIntent)
        }

        binding.btnCallUpdates.setOnClickListener {
            UpdateCallActivity.start(this)
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
