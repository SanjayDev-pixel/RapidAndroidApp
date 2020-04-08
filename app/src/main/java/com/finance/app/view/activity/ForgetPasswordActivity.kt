package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.finance.app.R
import com.finance.app.databinding.ActivityForgetPasswordBinding
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

class ForgetPasswordActivity : BaseAppCompatActivity() {

    private val binding: ActivityForgetPasswordBinding by ActivityBindingProviderDelegate(
            this , R.layout.activity_forget_password
    )
    private val presenter = Presenter()

    companion object {
        fun start(context: Context) {
            val intent = Intent(context , ForgetPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideToolbar()
        hideSecondaryToolbar()
        binding.btnProceed.setOnClickListener {
            if (formValidations.validateForgetPassword(binding)) {
                presenter.callNetwork(ConstantsApi.CALL_GET_OTP , dmiConnector = callGetotp())
                binding.progressBar.visibility = View.VISIBLE
            } else {
                Toast.makeText(this , "Please fill mandatory fields" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class callGetotp : ViewGeneric<Requests.RequestGetOTP , Response.ResponseGetOTP>(context = this) {
        override val apiRequest: Requests.RequestGetOTP?
            get() = requestOTP//To change initializer of created properties use File | Settings | File Templates.

        override fun getApiSuccess(value: Response.ResponseGetOTP) {
            if (value.responseCode == Constants.SUCCESS) {
                binding.progressBar.visibility = View.GONE
                OtpVerifyActivity.start(context = getContext())
            } else {
                showToast(value.responseMsg)
                binding.progressBar.visibility = View.GONE
                OtpVerifyActivity.start(context = getContext())
            }
        }

        override fun getApiFailure(msg: String) {
            if (msg.exIsNotEmptyOrNullOrBlank()) {
                super.getApiFailure(msg)
                binding.progressBar!!.visibility = View.GONE
            } else {
                super.getApiFailure("Time out Error")
                binding.progressBar!!.visibility = View.GONE
            }


        }
    }
    private val requestOTP: Requests.RequestGetOTP
        get() {
            val strUserName = binding.etMobile.text.toString()
            System.out.println("strUserName>>>>"+strUserName)
            return Requests.RequestGetOTP(userName = strUserName
            )
        }


}
