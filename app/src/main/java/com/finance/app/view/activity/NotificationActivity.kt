package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityAddLeadBinding
import com.finance.app.databinding.ActivityNotificationBinding
import com.finance.app.presenter.connector.AddLeadConnector
import com.finance.app.presenter.presenter.AddLeadPresenter
import com.finance.app.view.adapters.Recycler.Adapter.LeadListingAdapter
import com.finance.app.view.adapters.Recycler.Adapter.NotificationAdapter
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class NotificationActivity : BaseAppCompatActivity(){

    private val binding: ActivityNotificationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_notification)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rcNotification.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rcNotification.adapter = NotificationAdapter(this)
    }
}

