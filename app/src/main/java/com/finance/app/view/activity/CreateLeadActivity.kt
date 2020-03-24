package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.finance.app.utility.LeadMetaData
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
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.lang.Exception
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
    private var bundle: Bundle? = null
    private var lead: AllLeadMaster? = null
    var leadId:Any? = 0

    companion object {
        fun start(context: Context,lead: AllLeadMaster) {
            val intent = Intent(context, CreateLeadActivity::class.java)

            context.startActivity(intent)
        }
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        try {
            leadId = intent!!.extras["key_id"]
        }catch(e: Exception){
            e.printStackTrace()
        }

        hideSecondaryToolbar()
        SetCreateLeadMandatoryField(binding)
        getLoanProductFromDB()
        setBranchesDropDownValue()
       // setupCustomView()


       if( leadId !=null && leadId != 0){
        setValuesonView(LeadMetaData.getLeadData())
        }


        binding.btnCreate.setOnClickListener {

            if (formValidation.validateAddLead(binding, loanProduct, branches)) {
                if(loanProduct.getSelectedValue().toString()!= "null" && branches.getSelectedValue().toString() != "null"){
                    if(leadId!=null && leadId !=0){
                       editLead(leadId)
                    }else {
                        presenter.callNetwork(ConstantsApi.CALL_ADD_LEAD, CallCreateLead())
                        binding.progressBar!!.visibility = View.VISIBLE
                    }
                }else{
                    Toast.makeText(this,"Please fill maindatory fields",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun setValuesonView(leadData: AllLeadMaster?) {

        binding.etApplicantFirstName.setText(leadData?.applicantFirstName)
        binding.etApplicantMiddleName.setText(leadData?.applicantMiddleName)
        binding.etApplicantLastName.setText(leadData?.applicantLastName)
        binding.etLoanAmount.setText(leadData?.amountRequest.toString())
        binding.etArea.setText(leadData?.applicantAddress)
        binding.etEmail.setText(leadData?.applicantEmail)
        binding.etContactNum.setText(leadData?.applicantContactNumber)
        binding.btnCreate.setText(R.string.save)
        binding.heading.setText("Edit Lead")



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

        if(leadId !=null && leadId !=0){
            lead=LeadMetaData.getLeadData()
            loanProduct.setSelection(lead?.loanProductID.toString())
        }
    }

    private fun setBranchesDropDownValue() {
        val branchList = sharedPreferences.getUserBranches()
        val branch = ArrayList(branchList!!)
        branches = CustomSpinnerView(mContext = this, dropDowns = branch, label = "Select Branch *")
        binding.layoutBranches.addView(branches)

        if(leadId !=null && leadId !=0){
            lead=LeadMetaData.getLeadData()
            branches.setSelection(lead?.branchID.toString())
        }

    }

    private fun editLead(leadId: Any?) {
        presenter.callNetwork(ConstantsApi.CALL_EDIT_LEAD, CallEditLead())
        binding.progressBar!!.visibility = View.VISIBLE



    }

    inner class CallCreateLead : ViewGeneric<Requests.RequestAddLead, Response.ResponseAddLead>(context = this) {
        override val apiRequest: Requests.RequestAddLead
            get() = leadRequest

        override fun getApiSuccess(value: Response.ResponseAddLead) {
            if (value.responseCode == Constants.SUCCESS) {
                binding.progressBar!!.visibility =View.GONE
                AllLeadActivity.start(this@CreateLeadActivity)
                this@CreateLeadActivity.finish()


            } else {
                showToast(value.responseMsg)
                binding.progressBar!!.visibility =View.GONE
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



    inner class CallEditLead : ViewGeneric<Requests.RequestEditLead, Response.ResponseEditLead>(context = this) {
        override val apiRequest: Requests.RequestEditLead
            get() = editLeadRequest

        override fun getApiSuccess(value: Response.ResponseEditLead) {
            if (value.responseCode == Constants.SUCCESS) {
                binding.progressBar!!.visibility =View.GONE
                showToast(value.responseMsg)
                AllLeadActivity.start(this@CreateLeadActivity)
                this@CreateLeadActivity.finish()


            } else {
                showToast(value.responseMsg)
                binding.progressBar!!.visibility =View.GONE
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

    private val editLeadRequest: Requests.RequestEditLead
        get() {
            val lProductDD = loanProduct.getSelectedValue()
            val branchDD = branches.getSelectedValue()
            val loanAmount =binding.etLoanAmount.text.toString().toFloat()
            val leadId=LeadMetaData.getLeadId()


            return Requests.RequestEditLead(leadID=leadId,applicantFirstName = binding.etApplicantFirstName.text.toString(),
                    applicantMiddleName = binding.etApplicantMiddleName.text.toString(),
                    applicantLastName = binding.etApplicantLastName.text.toString(),
                    applicantContactNumber = binding.etContactNum.text.toString(),
                    applicantAlternativeContactNumber=null,
                    applicantEmail = binding.etEmail.text.toString(),
                    applicantAddress = binding.etArea.text.toString(),
                    remarks=null,
                    loanProductID = lProductDD?.productID,amountRequest=loanAmount,
                    branchID = branchDD?.branchID )
        }
}