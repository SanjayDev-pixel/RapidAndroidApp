package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityAddLeadBinding
import com.finance.app.presenter.connector.AddLeadConnector
import com.finance.app.presenter.presenter.AddLeadPresenter
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class AddLeadActivity : BaseAppCompatActivity(), AddLeadConnector.ViewOpt {

    private val binding: ActivityAddLeadBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_add_lead)

    private val presenterOpt = AddLeadPresenter(this)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
        binding.btnAddLead.setOnClickListener {
            presenterOpt.callNetwork(ConstantsApi.CALL_ADD_LEAD)
            AssignedLeadActivity.start(this)
        }
    }

    private val leadRequest: Requests.RequestAddLead
        get() {
            return Requests.RequestAddLead("", binding.etLocation.text.toString(),
                    "", binding.etContactNum.text.toString(),
                    binding.etEmail.text.toString(), binding.etApplicantName.text.toString(),
                    "", "", 123, "",
                    "", 0, "", "", "",
                    "", "", "", "",
                    123, "")
        }

    override val addLeadRequest: Requests.RequestAddLead
        get() = leadRequest

    override fun getAddLeadSuccess(value: Response.ResponseAddLead) {
        showToast("success")
    }

    override fun getAddLeadFailure(msg: String) {
        showToast("failed")
    }
}

