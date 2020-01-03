package com.finance.app.view.activity
import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityLoginBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.persistence.model.StatesMaster
import com.finance.app.presenter.connector.AllMasterValueConnector
import com.finance.app.presenter.connector.LoanProductConnector
import com.finance.app.presenter.presenter.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.request.Requests.RequestLogin
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.retrofit.response.Response.ResponseLogin
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import java.util.*
import javax.inject.Inject

class LoginActivity : BaseAppCompatActivity(), AllMasterValueConnector.StateDropdown,
        AllMasterValueConnector.MasterDropdown, LoanProductConnector.ViewOpt {

    // used to bind element of layout to activity
    private val binding: ActivityLoginBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_login)
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    private val loginPresenter = Presenter()

    private val loanProductPresenter = LoanProductPresenter(this)
    private val masterPresenter = AllMasterDropdownPresenter(this)
    private val statePresenter = StateDropdownPresenter(this)

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
            if (formValidation.validateLogin(binding)) {
                loginPresenter.callNetwork(ConstantsApi.CALL_LOGIN, dmiConnector = LoginApiCall())
            }
        }
        binding.tvForgotPassword.setOnClickListener {
            ForgetPasswordActivity.start(this)
        }
    }

    inner class LoginApiCall: ViewGeneric<RequestLogin, ResponseLogin>(context = this) {
        override val apiRequest: RequestLogin
            get() = mLoginRequestLogin

        override fun getApiSuccess(value: ResponseLogin) {
            if (value.responseCode == Constants.SUCCESS) {
                masterPresenter.callNetwork(ConstantsApi.CALL_ALL_MASTER_VALUE)
                loanProductPresenter.callNetwork(ConstantsApi.CALL_LOAN_PRODUCT)
                statePresenter.callNetwork(ConstantsApi.CALL_ALL_STATES)
                sharedPreferences.saveLoginData(value)
                DashboardActivity.start(this@LoginActivity)
            } else {
                showToast(value.responseMsg)
            }
        }
    }

    private val mCompany: Requests.Company
        get() {
            return Requests.Company(1, "comp1")
        }

    private val mLoginRequestLogin: RequestLogin
        get() {
//            binding.etUserName.setText("kuldeep.saini@gmail.com")
//            binding.etPassword.setText("Default@123")
            val username = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            val company = mCompany
            return RequestLogin(username = username, password = password, company = company)
        }

    override fun getAllMasterDropdownSuccess(dropdown: Response.ResponseAllMasterDropdown) {
        saveMasterDataToDB(dropdown.responseObj)
        DashboardActivity.start(this)
    }

    override fun getLoanProductSuccess(value: Response.ResponseLoanProduct) {
        saveLoanProductPurposeDataInDB(value.responseObj)
    }

    override fun getStatesDropdownSuccess(value: Response.ResponseStatesDropdown) {
        saveStatesDataInDB(value.responseObj)
    }

    //    Save Data to DB
    private fun saveMasterDataToDB(masterDropdown: AllMasterDropDown) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().allMasterDropDownDao().insertAllMasterDropDownValue(masterDropdown)
        }
    }

    private fun saveLoanProductPurposeDataInDB(productPurpose: ArrayList<LoanProductMaster>) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().loanProductDao().insertLoanProductList(productPurpose)
        }
    }

    private fun saveStatesDataInDB(statesObj: ArrayList<StatesMaster>) {
        GlobalScope.launch {
            dataBase.provideDataBaseSource().statesDao().insertStates(statesObj)
        }
    }

    //    Handle failure of the api
    override fun getStatesDropdownFailure(msg: String) = showToast(msg)

    override fun getAllMasterDropdownFailure(msg: String) = showToast(msg)

    override fun getLoanProductFailure(msg: String) = showToast(msg)
}
