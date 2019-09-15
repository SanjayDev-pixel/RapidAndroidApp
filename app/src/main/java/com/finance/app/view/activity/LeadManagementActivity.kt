package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadManagementBinding
import com.finance.app.view.adapters.Recycler.Adapter.LeadPagerAdapter
import com.finance.app.view.fragment.AllLeadsFragment
import com.finance.app.view.fragment.CompletedLeadFragment
import com.finance.app.view.fragment.PendingLeadsFragment
import com.finance.app.view.fragment.RejectedLeadFragment
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LeadManagementActivity : BaseAppCompatActivity() {

    private val binding: ActivityLeadManagementBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_lead_management)

    private var pagerAdapter: LeadPagerAdapter? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LeadManagementActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideSecondaryToolbar()
        pagerAdapter = LeadPagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(PendingLeadsFragment(), "Pending")
        pagerAdapter!!.addFragment(CompletedLeadFragment(), "Completed")
        pagerAdapter!!.addFragment(RejectedLeadFragment(), "Rejected")
        pagerAdapter!!.addFragment(AllLeadsFragment(), "All Leads")
        binding.viewPager.adapter = pagerAdapter
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }
}