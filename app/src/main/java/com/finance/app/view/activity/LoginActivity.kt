package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.finance.app.R
import com.finance.app.databinding.ActivityLoginBinding
import com.finance.app.presenter.connector.LoginConnector
import com.finance.app.presenter.presenter.LoginPresenter
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class LoginActivity : BaseAppCompatActivity(), LoginConnector.ViewOpt {

    private val binding: ActivityLoginBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_login)

    private val presenterOpt = LoginPresenter(this)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        binding.btnLogin.setOnClickListener {
            presenterOpt.callNetwork(ConstantsApi.CALL_LOGIN)
        }
    }

    private val mLoginRequestLogin: Requests.RequestLogin
        get() {
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            return Requests.RequestLogin(username = username, password = password)
        }

    override val loginRequest: Requests.RequestLogin
        get() = mLoginRequestLogin

    override fun getLoginSuccess(value: Response.ResponseLogin) {
        saveResponseToDB(value)
        DashboardActivity.start(this)
    }

    private fun saveResponseToDB(response: Response.ResponseLogin) {

    }

    override fun getLoginFailure(msg: String) {
        showToast(msg)
    }
}
