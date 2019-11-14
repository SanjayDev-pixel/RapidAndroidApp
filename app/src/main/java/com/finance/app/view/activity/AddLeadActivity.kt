package com.finance.app.view.activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.ActivityAddLeadBinding
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.presenter.connector.AddLeadConnector
import com.finance.app.presenter.presenter.AddLeadPresenter
import com.finance.app.view.adapters.recycler.Spinner.LoanProductSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.UserBranchesSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class AddLeadActivity : BaseAppCompatActivity(), AddLeadConnector.ViewOpt {

    private val binding: ActivityAddLeadBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_add_lead)
    private val presenterOpt = AddLeadPresenter(this)
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideSecondaryToolbar()
        getLoanProductFromDB()
        setBranchesDropDownValue()
        binding.btnCreate.setOnClickListener {
            if (formValidation.validateAddLead(binding)) {
                presenterOpt.callNetwork(ConstantsApi.CALL_ADD_LEAD)
            }
        }
    }

    private fun setBranchesDropDownValue() {
        binding.spinnerBranches.adapter = UserBranchesSpinnerAdapter(this, sharedPreferences.getUserBranches()!!)
    }

    private fun getLoanProductFromDB() {
        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(this, Observer {loanProducts->
            loanProducts?.let{
                setProductDropDownValue(loanProducts)
            }
        })
    }

    private fun setProductDropDownValue(products: List<LoanProductMaster>) {
        binding.spinnerLoanProduct.adapter = LoanProductSpinnerAdapter(this, products)
    }

    private val leadRequest: Requests.RequestAddLead
        get() {
            val loanProduct = binding.spinnerLoanProduct.selectedItem as
                    LoanProductMaster?
            val branch = binding.spinnerBranches.selectedItem as
                    Response.UserBranches?
            return Requests.RequestAddLead(applicantAddress = binding.etAddress.text.toString(),
                    applicantContactNumber = binding.etContactNum.text.toString(),
                    applicantEmail = binding.etEmail.text.toString(),
                    applicantFirstName = binding.etApplicantFirstName.text.toString(),
                    applicantMiddleName = binding.etApplicantMiddleName.text.toString(),
                    applicantLastName = binding.etApplicantLastName.text.toString(),
                    branchID = 2, loanProductID = 1)
//                    branchID = branch!!.branchID, loanProductID = loanProduct!!.productID)
        }

    override val addLeadRequest: Requests.RequestAddLead
        get() = leadRequest

    override fun getAddLeadSuccess(value: Response.ResponseAddLead) {
        AllLeadActivity.start(this)
        showToast("success")
    }

    override fun getAddLeadFailure(msg: String) = showToast(msg)
}
