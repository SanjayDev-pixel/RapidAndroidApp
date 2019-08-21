package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanApplicationBinding
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LoanApplicationActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanApplicationBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_application)

    private var isExpand = false

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoanApplicationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        binding.applicent.text = "Applicant"
    }
}
