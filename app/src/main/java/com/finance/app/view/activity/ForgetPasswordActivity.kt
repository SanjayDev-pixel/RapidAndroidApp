package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityForgetPasswordBinding
import com.finance.app.databinding.ActivityLoginBinding
import com.finance.app.databinding.ActivityOtpVerifyBinding
import com.finance.app.presenter.connector.LoginConnector
import com.finance.app.presenter.presenter.LoginPresenter
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class ForgetPasswordActivity : BaseAppCompatActivity() {

    private val binding: ActivityForgetPasswordBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_forget_password)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ForgetPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
        binding.btnProceed.setOnClickListener {
            OtpVerifyActivity.start(context = getContext())

        }
    }
}
