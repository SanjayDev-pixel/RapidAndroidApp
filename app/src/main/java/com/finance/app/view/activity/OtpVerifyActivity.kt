package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityOtpVerifyBinding
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class OtpVerifyActivity : BaseAppCompatActivity() {

    // used to bind element of layout to activity
    private val binding: ActivityOtpVerifyBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_otp_verify)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, OtpVerifyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
        binding.btnProceed.setOnClickListener {
            ResetPasswordActivity.start(context = getContext())

        }
    }
}
