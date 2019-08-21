package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.finance.app.R
import com.finance.app.databinding.ActivityAddNewLeadBinding
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class AddNewLeadActivity : BaseAppCompatActivity() {

    private val binding: ActivityAddNewLeadBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_add_new_lead)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddNewLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        binding.btnAddLead.setOnClickListener {
            LeadManagementActivity.start(this)
        }
    }
}

