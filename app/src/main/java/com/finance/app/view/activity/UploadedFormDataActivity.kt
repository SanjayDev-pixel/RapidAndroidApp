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

class UploadedFormDataActivity : BaseAppCompatActivity() {

    // used to bind element of layout to activity
    private val binding: ActivityLoginBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_uploaded_form_data)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UploadedFormDataActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideSecondaryToolbar()
    }

    private fun saveResponseToDB(response: Response.ResponseLogin) {
    }
}
