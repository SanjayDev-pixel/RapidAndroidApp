package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.ActivityLeadCreateBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.utility.SetCreateLeadMandatoryField
import com.finance.app.view.customViews.ChannelPartnerViewCreateLead
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
       // setupCustomView()

        binding.btnCreate.setOnClickListener {

            if (formValidation.validateAddLead(binding, loanProduct, branches)) {
                if(loanProduct.getSelectedValue().toString()!= "null" && branches.getSelectedValue().toString() != "null"){
                presenter.callNetwork(ConstantsApi.CALL_ADD_LEAD, CallCreateLead())}else{
                    Toast.makeText(this,"Please fill maindatory fields",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupCustomView() {
        CreateLeadActivity.let { it->
                binding.viewChannelPartnernew.attachActivity(activity = this,loanData= LoanInfoModel())
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

       // var branchID =branches.getSelectedValue()?.branchID

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
            val loanAmount =binding.etLoanAmount.text.toString().toFloat()
           /* val sPartner = binding.viewChannelPartnernew.getSourcingPartner()
            val channelPartnerID = binding.viewChannelPartnernew.getPartnerName()
            val cpnameTypeDetailId: Int?= channelPartnerID?.channelTypeTypeDetailID
            val sourcingChannelPartID :Int?=sPartner?.typeDetailID*/

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