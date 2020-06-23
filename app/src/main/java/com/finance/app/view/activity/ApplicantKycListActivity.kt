package com.finance.app.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.CoApplicantsConnector
import com.finance.app.presenter.presenter.CoApplicantsPresenter
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.ApplicantKycListAdapter
import com.finance.app.viewModel.LeadDataViewModel
import motobeans.architecture.appDelegates.ViewModelType
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

class ApplicantKycListActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null// temp
    private val presenter = Presenter()
    private var leadId : String ? = null
    private val leadDataViewModel: LeadDataViewModel by motobeans.architecture.appDelegates.viewModelProvider(this, ViewModelType.WITH_DAO)
    private var leadData :AllLeadMaster?=null
    //private val coApplicantPresenter = CoApplicantsPresenter(CoApplicantsConnector.CoApplicants)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_kyc_applicant_list)
        progressBar = findViewById<ProgressBar>(R.id.progress_Bar) as ProgressBar
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            leadId = bundle.getString("leadId") // 1
            System.out.println("LeadIdIntent>>>>"+leadId)
            presenter.callNetwork(ConstantsApi.CALL_KYC_APPLICANT_DETAILS,CallFinalSubmit())
        }


    }
    inner class CallFinalSubmit : ViewGeneric<Requests.RequestKYCApplicantList, Response.ResponseApplicantKycList>(context = this) {
        override val apiRequest: Requests.RequestKYCApplicantList?
            get() = getCallUpdateRequest()
        override fun getApiSuccess(value: Response.ResponseApplicantKycList) {
            if (value.responseCode == Constants.SUCCESS) {
                //val submitLoanResponse: AppK?=value.responseObj
                System.out.println("ResponseObject>>>>"+value.responseObj?.applicantKycListModel?.size)
                if(value.responseObj !=null) {

                    progressBar!!.visibility = View.GONE

                    //setUpRecyclerView(value.responseObj,leadId!!)
                   /* val intent = Intent(this@FinalSubmitActivity, LoanSubmitStatusActivity::class.java)
                    intent.putExtra("SubmitResponse", submitLoanResponse)
                    startActivity(intent)*/
                }else{
                    showToast(value.responseMsg)
                }
                //finish()


            } else {
                showToast(value.responseMsg)
                progressBar!!.visibility = View.GONE
            }

        }

        override fun getApiFailure(msg:String) {
            if(msg.exIsNotEmptyOrNullOrBlank()){
                super.getApiFailure(msg)
                progressBar!!.visibility = View.GONE
            }else{ super.getApiFailure("Time out Error")
                progressBar!!.visibility = View.GONE}

        }
    }

    private fun getCallUpdateRequest(): Requests.RequestKYCApplicantList? {

        return Requests.RequestKYCApplicantList(leadID = leadId!!)

    }

    private fun setUpRecyclerView(list:PersonalApplicantList , leadId : String) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewApplicantDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //System.out.println("Name>>>"+list.applicantDetails[0].firstName)
        //creating our adapter
        System.out.println("Applicant Detail>>>>"+list.applicantDetails.size)
       /* val adapter = ApplicantKycListAdapter(this,list,leadId)
        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter*/
    }
}