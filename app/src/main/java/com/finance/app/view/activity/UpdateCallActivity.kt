package com.finance.app.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.ActivityUpdateCallBinding
import com.finance.app.others.AppEnums
import com.finance.app.others.setTextVertically
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.utility.ConvertDate
import kotlinx.android.synthetic.main.activity_update_call.*
import kotlinx.android.synthetic.main.layout_header_with_back_btn.view.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class UpdateCallActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    private var bundle: Bundle? = null
    private var leadID = 0

    // used to bind element of layout to activity
    private val binding: ActivityUpdateCallBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_update_call)
    private val callStatus = arrayOf("Call Status", "Fixed Meeting", "Not Interested", "Follow up")

    companion object {
        private const val KEY_LEAD_ID = "leadIdForApplicant"
        fun start(context: Context, leadID: Int?) {
            val intent = Intent(context, UpdateCallActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(KEY_LEAD_ID, leadID!!)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideToolbar()
        hideSecondaryToolbar()

        val callStatusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, callStatus)
        callStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCallStatus.adapter = callStatusAdapter
        binding.spinnerCallStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override
            fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int,
                               id: Long) {
                showViews(callStatus[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // do Nothing
            }
        }

        setClickListeners()

        getLeadId()
    }

    private fun setClickListeners() {
        binding.header.lytBack.setOnClickListener { onBackPressed() }
    }

    private fun getLeadId() {
        bundle = intent.extras
        bundle?.let {
            leadID = bundle!!.getInt(KEY_LEAD_ID)
        }
        getLeadFormDB(leadID)
    }

    private fun showViews(value: Any) {
        when (value) {
            callStatus[1] -> {
                layoutFixedMeeting.visibility = View.VISIBLE
                layoutNotInterested.visibility = View.GONE
                layoutFollowUp.visibility = View.GONE

            }
            callStatus[2] -> {
                layoutFixedMeeting.visibility = View.GONE
                layoutNotInterested.visibility = View.VISIBLE
                layoutFollowUp.visibility = View.GONE
            }
            callStatus[3] -> {
                layoutFixedMeeting.visibility = View.GONE
                layoutNotInterested.visibility = View.GONE
                layoutFollowUp.visibility = View.VISIBLE
            }
        }
    }

    private fun getLeadFormDB(leadID: Int) {
        dataBase.provideDataBaseSource().allLeadsDao().getLead(leadID)
                .observe(this, Observer { lead ->
                    setLeadDetailsToViews(lead)
                    sharedPreferences.saveLeadDetail(lead)
                })
    }

    @SuppressLint("SetTextI18n")
    private fun setLeadDetailsToViews(lead: AllLeadMaster) {
        binding.header.tvLeadNumber.text = lead.leadNumber
        binding.leadDetails.tvStatusLine.setTextVertically(lead.status)

        binding.leadDetails.tvLeadName.text = lead.applicantFirstName
        binding.leadDetails.tvLeadID.text = "Lead Id : ${lead.leadID.toString()}"
        binding.leadDetails.tvLoanType.text = "Loan Type : ${lead.loanProductName}"
        binding.leadDetails.tvCreatedDate.text = "Created Date : ${ConvertDate().convertDate(lead.createdOn!!)}"
        binding.leadDetails.tvUpdatedDate.text = ConvertDate().convertDate(lead.lastModifiedOn!!)

        when (lead.status) {
            AppEnums.LEAD_TYPE.NEW.type -> binding.leadDetails.tvStatusLine.setBackgroundColor(resources.getColor(R.color.lead_status_new))
            AppEnums.LEAD_TYPE.SUBMITTED.type -> binding.leadDetails.tvStatusLine.setBackgroundColor(resources.getColor(R.color.lead_status_submitted))
            AppEnums.LEAD_TYPE.PENDING.type -> binding.leadDetails.tvStatusLine.setBackgroundColor(resources.getColor(R.color.lead_status_pending))
            AppEnums.LEAD_TYPE.REJECTED.type -> binding.leadDetails.tvStatusLine.setBackgroundColor(resources.getColor(R.color.lead_status_rejected))
        }

    }
}
