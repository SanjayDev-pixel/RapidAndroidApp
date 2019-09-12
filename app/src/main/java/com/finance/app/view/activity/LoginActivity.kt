package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
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

    // used to bind element of layout to activity
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
        hideToolbar()
        hideSecondaryToolbar()
//        Call login api on login button
        binding.btnLogin.setOnClickListener {
            presenterOpt.callNetwork(ConstantsApi.CALL_LOGIN)
        }
        binding.tvForgotPassword.setOnClickListener {
            ForgetPasswordActivity.start(context = getContext())
        }
    }

    private val mCompany: Requests.Company
        get() {
            return Requests.Company(1, "comp1")
        }

    private val mLoginRequestLogin: Requests.RequestLogin
        get() {

            binding.etUserName.setText("kuldeep.saini@gmail.com")
            binding.etPassword.setText("Default@123")

            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            val company = mCompany
            return Requests.RequestLogin(username = username, password = password, company = company)
        }

    override val loginRequest: Requests.RequestLogin
        get() = mLoginRequestLogin

    //    Handle success of the api
    override fun getLoginSuccess(value: Response.ResponseLogin) {
        saveResponseToDB(value)
        DashboardActivity.start(this)
    }

    private fun saveResponseToDB(response: Response.ResponseLogin) {
    }

    //    Handle failure of the api
    override fun getLoginFailure(msg: String) {
        showToast(msg)
    }
}
