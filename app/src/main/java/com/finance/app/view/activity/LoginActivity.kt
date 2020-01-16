package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import com.finance.app.R
import com.finance.app.databinding.ActivityLoginBinding
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.GetOtherDropDownFromApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.customAppComponents.jetpack.SuperWorker
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.request.Requests.RequestLogin
import motobeans.architecture.retrofit.response.Response.ResponseLogin
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LoginActivity : BaseAppCompatActivity() {

    // used to bind element of layout to activity
    private val binding: ActivityLoginBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_login)
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    private val loginPresenter = Presenter()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideToolbar()
        hideSecondaryToolbar()
        setClickListeners()
    }

    private fun setClickListeners() {
//        Call login api on login button
        binding.btnLogin.setOnClickListener {
//            if (formValidation.validateLogin(binding)) {
                loginPresenter.callNetwork(ConstantsApi.CALL_LOGIN, dmiConnector = LoginApiCall())
//            }
        }

        binding.tvForgotPassword.setOnClickListener {
            ForgetPasswordActivity.start(this)
        }
    }

    inner class LoginApiCall: ViewGeneric<RequestLogin, ResponseLogin>(context = this) {
        override val apiRequest: RequestLogin
            get() = mLoginRequestLogin

        private val mLoginRequestLogin: RequestLogin
            get() {
                binding.etUserName.setText("kuldeep.saini@gmail.com")
                binding.etPassword.setText("Default@123")
                val username = binding.etUserName.text.toString()
                val password = binding.etPassword.text.toString()
                val company = mCompany
                return RequestLogin(username = username, password = password, company = company)
            }

        private val mCompany: Requests.Company
            get() {
                return Requests.Company(1, "comp1")
            }

        override fun getApiSuccess(value: ResponseLogin) {
            if (value.responseCode == Constants.SUCCESS) {
                sharedPreferences.saveLoginData(value)
                getOtherDropdownValue()
                DashboardActivity.start(this@LoginActivity)
            } else {
                showToast(value.responseMsg)
            }
        }

    }

    private fun getOtherDropdownValue() {
        GlobalScope.launch {
            runOnUiThread {
                GetOtherDropDownFromApi(getContext())
            }
        }
    }
}
