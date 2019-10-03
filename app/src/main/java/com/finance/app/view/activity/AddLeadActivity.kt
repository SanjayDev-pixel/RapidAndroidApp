package com.finance.app.view.activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
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
import motobeans.architecture.development.interfaces.DataBaseUtil
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
    @Inject
    lateinit var dataBase: DataBaseUtil
    private val presenterOpt = AddLeadPresenter(this)
    private lateinit var loanType: ArrayList<DropdownMaster>

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        getLoanTypeFromDB()
        binding.btnAddLead.setOnClickListener {
            presenterOpt.callNetwork(ConstantsApi.CALL_ADD_LEAD)
            showToast("${binding.spinnerTypeOfLoan.selectedItem.toString().toInt()}")
            showToast("${binding.spinnerTypeOfLoan.selectedItem}")
        }
    }

    private fun getLoanTypeFromDB() {
        loanType = ArrayList()
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(this, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                masterDrownDownValues.LoanType?.let { loanTypeDropDown ->
                    loanType = loanTypeDropDown
                    setDropDownValue()
                }
            }
        })
    }

    private fun setDropDownValue() {
        val branches = sharedPreferences.getUserBranches()
        binding.spinnerBranchId.adapter = UserBranchesSpinnerAdapter(this, branches!!)
        binding.spinnerTypeOfLoan.adapter = GenericSpinnerAdapter(this, loanType)
    }

    private val leadRequest: Requests.RequestAddLead
        get() {
            val loanTypeDetailId = binding.spinnerTypeOfLoan.selectedItem as
                    DropdownMaster
            return Requests.RequestAddLead(applicantAddress = binding.etAddress.text.toString(),
                    applicantContactNumber = binding.etContactNum.text.toString(),
                    applicantEmail = binding.etEmail.text.toString(),
                    applicantFirstName = binding.etApplicantFirstName.text.toString(),
                    applicantMiddleName = binding.etApplicantMiddleName.text.toString(),
                    applicantLastName = binding.etApplicantLastName.text.toString(),
                    branchID = 1,
                    loanTypeDetailID = loanTypeDetailId.typeDetailID!!
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
