package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
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
        binding.rcActivities.layoutManager = LinearLayoutManager(this)
        binding.rcActivities.adapter = LeadDetailActivityAdapter(this)

        binding.btnCallToCustomer.setOnClickListener {
             }
    }
}
