package com.finance.app.view.activity
import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityLoginBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.presenter.connector.AllSpinnerValueConnector
import com.finance.app.presenter.connector.LoanProductConnector
import com.finance.app.presenter.connector.LoginConnector
import com.finance.app.presenter.presenter.AllSpinnerValuePresenter
import com.finance.app.presenter.presenter.LoanProductPresenter
import com.finance.app.presenter.presenter.LoginPresenter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class LoginActivity : BaseAppCompatActivity(), LoginConnector.ViewOpt, AllSpinnerValueConnector.ViewOpt, LoanProductConnector.ViewOpt {

    // used to bind element of layout to activity
    private val binding: ActivityLoginBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_login)
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private val loginPresenter = LoginPresenter(this)
    private val loanProductPresenter = LoanProductPresenter(this)
    private val spinnerPresenter = AllSpinnerValuePresenter(this)

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
            loginPresenter.callNetwork(ConstantsApi.CALL_LOGIN)
            loanProductPresenter.callNetwork(ConstantsApi.CALL_LOAN_PRODUCT)
        }
        binding.tvForgotPassword.setOnClickListener {
            ForgetPasswordActivity.start(this)
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
        spinnerPresenter.callNetwork(ConstantsApi.CALL_ALL_SPINNER_VALUE)
        AddLeadActivity.start(this)
    }

    //    Handle failure of the api
    override fun getLoginFailure(msg: String) {
        showToast(msg)
    }

    override fun getAllSpinnerValueSuccess(value: Response.ResponseAllMasterValue) {
        saveMasterDataToDB(value.responseObj)
    }

    private fun saveMasterDataToDB(masterDropdown: AllMasterDropDown) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().allMasterDropDownDao().insertAllMasterDropDownValue(masterDropdown)
        }
    }

    override fun getLoanProductSuccess(value: Response.ResponseLoanProduct) {
        saveLoanProductPurposeDataInDB(value.responseObj)
    }

    private fun saveLoanProductPurposeDataInDB(productPurpose: ArrayList<LoanProductMaster>) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().loanProductDao().insertLoanProductList(productPurpose)
        }
    }

    override fun getLoanProductFailure(msg: String) {
        showToast(msg)
    }

    override fun getAllSpinnerValueFailure(msg: String) {
        showToast(msg)
    }
}
