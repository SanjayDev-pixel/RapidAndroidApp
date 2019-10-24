package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadDetailBinding
import com.finance.app.presenter.connector.GetAllLeadsConnector
import com.finance.app.presenter.presenter.GetLeadPresenter
import com.finance.app.view.adapters.recycler.adapter.LeadDetailActivityAdapter
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LeadDetailActivity : BaseAppCompatActivity(), GetAllLeadsConnector.ParticularLead {

    private val binding: ActivityLeadDetailBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_lead_detail)
    @Inject
    lateinit var dataBase: DataBaseUtil
    private val presenter = GetLeadPresenter(this)
    private var bundle: Bundle? = null
    private var leadID = 0

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
        getLeadId()
        setClickListeners()
        presenter.callNetwork(ConstantsApi.CALL_GET_LEAD)
        }

    private fun setRecyclerView() {
        binding.rcActivities.layoutManager = LinearLayoutManager(this)
        binding.rcActivities.adapter = LeadDetailActivityAdapter(this)
    }

    private fun getLeadId() {
        bundle = intent.extras
        bundle?.let {
            leadID = bundle!!.getInt(KEY_LEAD_ID)
        }
    }

    private fun setClickListeners() {
        binding.btnUpdateApplication.setOnClickListener {
            LoanApplicationActivity.start(this)
        }

        binding.btnCallToCustomer.setOnClickListener {
            val mobileNum = 8920992443
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel: +91${mobileNum}")
            startActivity(callIntent)
        }

        binding.btnCallUpdates.setOnClickListener {
            UpdateCallActivity.start(this)
        }

        binding.btnAddTask.setOnClickListener {
            AddTaskActivity.start(this)
        }
    }

    override val leadId: Int
        get() = leadID

    override fun getLeadSuccess(value: Response.ResponseGetLead) {
        showToast("success")
        setRecyclerView()
        fillDataOnScreen(value.responseObj)
    }

    private fun fillDataOnScreen(value: Response.LeadObj) {

    }

    override fun getLeadFailure(msg: String) {
        showToast(msg)
    }
}
