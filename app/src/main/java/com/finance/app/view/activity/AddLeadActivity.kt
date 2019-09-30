package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import com.finance.app.R
import com.finance.app.databinding.ActivityAddLeadBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.AddLeadConnector
import com.finance.app.presenter.presenter.AddLeadPresenter
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter
import com.finance.app.view.adapters.Recycler.Adapter.UserBranchesSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class AddLeadActivity : BaseAppCompatActivity(), AddLeadConnector.ViewOpt {

    private val binding: ActivityAddLeadBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_add_lead)
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private val presenterOpt = AddLeadPresenter(this)

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        setDropDownValue()
        binding.btnAddLead.setOnClickListener {
            presenterOpt.callNetwork(ConstantsApi.CALL_ADD_LEAD)
            showToast("${binding.spinnerBranchId.selectedItem.toString().toInt()}")
        }
    }

    private fun setDropDownValue() {
        val branches = sharedPreferences.getUserBranches()
        binding.spinnerBranchId.adapter = UserBranchesSpinnerAdapter(this, branches!!)

        val lists: ArrayList<DropdownMaster> = ArrayList()
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())
        binding.spinnerTypeOfLoan.adapter = GenericSpinnerAdapter(this, lists)

    }

    private val leadRequest: Requests.RequestAddLead
        get() {
            return Requests.RequestAddLead(applicantAddress = binding.etAddress.text.toString(),
                    applicantContactNumber = binding.etContactNum.text.toString(),
                    applicantEmail = binding.etEmail.text.toString(),
                    applicantFirstName = binding.etApplicantFirstName.text.toString(),
                    applicantMiddleName = binding.etApplicantMiddleName.text.toString(),
                    applicantLastName = binding.etApplicantLastName.text.toString(),
                    branchID = binding.spinnerBranchId.selectedItem.toString().toInt()
            )
        }

    override val addLeadRequest: Requests.RequestAddLead
        get() = leadRequest

    override fun getAddLeadSuccess(value: Response.ResponseAddLead) {
        AssignedLeadActivity.start(this)
        showToast("success")
    }

    override fun getAddLeadFailure(msg: String) {
        showToast("failed")
    }
}

