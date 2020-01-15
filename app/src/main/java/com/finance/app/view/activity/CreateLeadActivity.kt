package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadCreateBinding
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.persistence.model.UserBranches
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.SetCreateLeadMandatoryField
import com.finance.app.view.customViews.CustomSpinnerViewTest
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import javax.inject.Inject

class CreateLeadActivity : BaseAppCompatActivity() {

    private val binding: ActivityLeadCreateBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_lead_create)
    private lateinit var loanProduct: CustomSpinnerViewTest<LoanProductMaster>
    private lateinit var branches: CustomSpinnerViewTest<UserBranches>
    private val presenter = Presenter()
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CreateLeadActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        hideSecondaryToolbar()
        SetCreateLeadMandatoryField(binding)
        getLoanProductFromDB()
        setBranchesDropDownValue()
        binding.btnCreate.setOnClickListener {
            if (formValidation.validateAddLead(binding)) {
                presenter.callNetwork(ConstantsApi.CALL_ADD_LEAD, CallCreateLead())
            }
        }
    }

    private fun getLoanProductFromDB() {
        dataBase.provideDataBaseSource().loanProductDao().getAllLoanProduct().observe(this, Observer { loanProductValue ->
            loanProductValue.let {
                val arrayListOfLoanProducts = ArrayList<LoanProductMaster>()
                arrayListOfLoanProducts.addAll(loanProductValue)
                setProductDropDownValue(arrayListOfLoanProducts)
            }
        })
    }

    private fun setProductDropDownValue(products: ArrayList<LoanProductMaster>) {
        loanProduct = CustomSpinnerViewTest(context = this, dropDowns = products, label = "Loan Product *")
        binding.layoutLoanProduct.addView(loanProduct)
    }

    private fun setBranchesDropDownValue() {
        val branchList = sharedPreferences.getUserBranches()
        val branch = ArrayList(branchList!!)
        branches = CustomSpinnerViewTest(context = this, dropDowns = branch, label = "Select Branch *")
        binding.layoutBranches.addView(branches)
    }

    inner class CallCreateLead : ViewGeneric<Requests.RequestAddLead, Response.ResponseAddLead>(context = this) {
        override val apiRequest: Requests.RequestAddLead
            get() = leadRequest

        override fun getApiSuccess(value: Response.ResponseAddLead) {
            if (value.responseCode == Constants.SUCCESS) {
                AllLeadActivity.start(this@CreateLeadActivity)
            } else {
                showToast(value.responseMsg)
            }
        }

    }

    private val leadRequest: Requests.RequestAddLead
        get() {
            val lProductDD = loanProduct.getSelectedValue()
            val branchDD = branches.getSelectedValue()

            return Requests.RequestAddLead(applicantAddress = binding.etArea.text.toString(),
                    applicantContactNumber = binding.etContactNum.text.toString(),
                    applicantEmail = binding.etEmail.text.toString(),
                    applicantFirstName = binding.etApplicantFirstName.text.toString(),
                    applicantMiddleName = binding.etApplicantMiddleName.text.toString(),
                    applicantLastName = binding.etApplicantLastName.text.toString(),
                    branchID = branchDD!!.branchID, loanProductID = lProductDD!!.productID)
        }
}