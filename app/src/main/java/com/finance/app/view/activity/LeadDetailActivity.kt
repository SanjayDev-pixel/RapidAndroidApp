package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadDetailBinding
import com.finance.app.view.adapters.Recycler.Adapter.LeadDetailActivityAdapter
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LeadDetailActivity : BaseAppCompatActivity() {

    private val binding: ActivityLeadDetailBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_lead_detail)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LeadDetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
//        showLeadOptionsMenu()
        setClickListeners()
        binding.rcActivities.layoutManager = LinearLayoutManager(this)
        binding.rcActivities.adapter = LeadDetailActivityAdapter(this)
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
}
