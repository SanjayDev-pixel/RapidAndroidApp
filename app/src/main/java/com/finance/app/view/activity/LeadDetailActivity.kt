package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadDetailBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.CoApplicantsList
import com.finance.app.persistence.model.CoApplicantsMaster
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.view.adapters.recycler.adapter.LeadDetailActivityAdapter
import kotlinx.android.synthetic.main.layout_header_with_back_btn.view.*

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
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
    private var leadContact: Long = 0
    private val presenter = Presenter()

    companion object {
        private const val KEY_LEAD_ID = "leadIdForApplicant"
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
        hideToolbar()
        hideSecondaryToolbar()
        getLeadId()
        presenter.callNetwork(ConstantsApi.CALL_COAPPLICANTS_LIST, dmiConnector = CallCoApplicantList())
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
            AppEnums.LEAD_TYPE.PENDING.type -> binding.tvLeadStatus.setTextColor(Color.YELLOW)
            AppEnums.LEAD_TYPE.SUBMITTED.type -> binding.tvLeadStatus.setTextColor(Color.BLUE)
            AppEnums.LEAD_TYPE.REJECTED.type -> binding.tvLeadStatus.setTextColor(Color.RED)
            else -> binding.tvLeadStatus.setTextColor(Color.GREEN)
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

            UpdateCallActivity.start(this, leadID)
        }

        binding.btnAddTask.setOnClickListener {
            AddTaskActivity.start(this)
        }
    }

    private fun setUpRecyclerView() {
        binding.rcActivities.layoutManager = LinearLayoutManager(this)
        binding.rcActivities.adapter = LeadDetailActivityAdapter(this)
    }

    inner class CallCoApplicantList : ViewGeneric<String, Response.ResponseCoApplicants>(context = this) {
        override val apiRequest: String
            get() = leadID.toString()

        override fun getApiSuccess(value: Response.ResponseCoApplicants) {
            if (value.responseCode == Constants.SUCCESS) {
                saveApplicantToDB(value.responseObj)
            }
        }

        private fun saveApplicantToDB(responseObj: ArrayList<CoApplicantsList>) {
            GlobalScope.launch {
                val coApplicantMaster = CoApplicantsMaster()
                coApplicantMaster.coApplicantsList = responseObj
                coApplicantMaster.leadID = leadID
                dataBase.provideDataBaseSource().coApplicantsDao().insertCoApplicants(coApplicantMaster)
            }
        }
    }
}
