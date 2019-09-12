package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityResetPasswordBinding
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class ResetPasswordActivity : BaseAppCompatActivity() {

    // used to bind element of layout to activity
    private val binding: ActivityResetPasswordBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_reset_password)


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
        binding.btnProceed.setOnClickListener {
            LoginActivity.start(context = getContext())

        }
//        Call login api on login button

    }




    private fun saveResponseToDB(response: Response.ResponseLogin) {
    }

}
