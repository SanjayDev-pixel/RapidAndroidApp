package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadCreateBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.LoanInfoModel
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.persistence.model.UserBranches
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.SetCreateLeadMandatoryField
import com.finance.app.view.customViews.CustomSpinnerView
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.appDelegates.ViewModelType
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

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation

    private lateinit var loanProduct: CustomSpinnerView<LoanProductMaster>
    private lateinit var branches: CustomSpinnerView<UserBranches>
    private val presenter = Presenter()
    private val appDataViewModel: AppDataViewModel by motobeans.architecture.appDelegates.viewModelProvider(this, ViewModelType.WITH_DAO)

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
        setupCustomView()
        binding.btnCreate.setOnClickListener {
            if (formValidation.validateAddLead(binding, loanProduct, branches)) {
                presenter.callNetwork(ConstantsApi.CALL_ADD_LEAD, CallCreateLead())
            }
        }

    }

    private fun setupCustomView() {

        CreateLeadActivity.let { it->
                binding.viewChannelPartner.attachActivity(activity = this, loanData = LoanInfoModel())
            }


    }

    private fun getLoanProductFromDB() {
        appDataViewModel.getLoanProductMaster().observe(this, Observer { loanProductValue ->
            loanProductValue?.let {
                val arrayListOfLoanProducts = ArrayList<LoanProductMaster>()
                arrayListOfLoanProducts.addAll(loanProductValue)
                setProductDropDownValue(arrayListOfLoanProducts)
            }
        })
//add new @S
        appDataViewModel.getAllMasterDropdown().observe(this,Observer{masterDrownDownValues->
            masterDrownDownValues?.let {
               // setMasterDropDownValue(masterDrownDownValues)

            }
        })
    }


    private fun setProductDropDownValue(products: ArrayList<LoanProductMaster>) {
        loanProduct = CustomSpinnerView(mContext = this, dropDowns = products, label = "Loan Product *")
        binding.layoutLoanProduct.addView(loanProduct)
    }

    private fun setBranchesDropDownValue() {
        val branchList = sharedPreferences.getUserBranches()
        val branch = ArrayList(branchList!!)
        branches = CustomSpinnerView(mContext = this, dropDowns = branch, label = "Select Branch *")
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
            val sPartner = binding.viewChannelPartner.getSourcingPartner()
            val channelPartnerID = binding.viewChannelPartner.getPartnerName()
            val loanAmount =binding.etLoanAmount.text.toString().toFloat()

            return Requests.RequestAddLead(applicantAddress = binding.etArea.text.toString(),
                    applicantContactNumber = binding.etContactNum.text.toString(),
                    applicantEmail = binding.etEmail.text.toString(),
                    applicantFirstName = binding.etApplicantFirstName.text.toString(),
                    applicantMiddleName = binding.etApplicantMiddleName.text.toString(),
                    applicantLastName = binding.etApplicantLastName.text.toString(),
                    branchID = branchDD?.branchID, loanProductID = lProductDD?.productID,
                    channelPartnerID=null,sourcingChannelPartnerTypeDetailCode=null,amountRequest=loanAmount)
        }
}