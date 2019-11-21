package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
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

    private lateinit var branchList: List<Response.UserBranches>

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        branchList = sharedPreferences.getUserBranches()!!

        hideSecondaryToolbar()
        getLoanProductFromDB()
        setBranchesDropDownValue()

        binding.btnCreate.setOnClickListener {
            if (isValidToProceed()) {
                presenterOpt.callNetwork(ConstantsApi.CALL_ADD_LEAD)
            }
        }
    }

    private fun isValidToProceed(): Boolean {

        var isValid = true
        val isValidBinding = formValidation.validateAddLead(binding)

        val selectedLoan = getSelectedLoanProductMasterType()
        val isLoanValid = selectedLoan != null && selectedLoan.productID > 0
        binding.spinnerLoanProduct.isEnableErrorLabel = !isLoanValid
        when(isLoanValid) {
            false -> {
                binding.spinnerLoanProduct.error = "Select Loan"
            }
        }

        val selectedBranch = getSelectedBranchType()
        val isBranchValid = selectedBranch != null && selectedBranch.branchID > 0
        binding.spinnerBranches.isEnableErrorLabel = !isBranchValid
        when(isBranchValid) {
            false -> {
                binding.spinnerBranches.error = "Select Branch"
            }
        }
        isValid = isValidBinding && isLoanValid && isBranchValid
        return isValid
    }

    private fun setBranchesDropDownValue() {
        binding.spinnerBranches.adapter = UserBranchesSpinnerAdapter(this, branchList!!)
        binding.spinnerBranches.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val branchSelected = getSelectedBranchType()

                    // TEMP CODE - MUNISH THAKUR (below print code is temporary)
                    println("Munish Thakur -> Debug Point");
                }
            }
        }


    }

    private fun getLoanProductFromDB() {
        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(this, Observer { loanProducts ->
            loanProducts?.let {
                val arrayListOfLoanProducts = ArrayList<LoanProductMaster>()
                arrayListOfLoanProducts.addAll(loanProducts)
                setProductDropDownValue(arrayListOfLoanProducts)
            }
        })
    }

    private fun setProductDropDownValue(products: ArrayList<LoanProductMaster>) {
        binding.spinnerLoanProduct.adapter = LoanProductSpinnerAdapter(this, products)
    }

    private val leadRequest: Requests.RequestAddLead
        get() {
            val loanProduct = getSelectedLoanProductMasterType()
            val branch = getSelectedBranchType()
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

    private fun getSelectedLoanProductMasterType(): LoanProductMaster? {
        try {
            return binding.spinnerLoanProduct.selectedView.tag as LoanProductMaster
        } catch (e: Exception) {
            return null
        }
    }

    private fun getSelectedBranchType(): Response.UserBranches? {
        try {
            return binding.spinnerBranches.selectedView.tag as Response.UserBranches
        } catch (e: Exception) {
            return null
        }
    }
}
