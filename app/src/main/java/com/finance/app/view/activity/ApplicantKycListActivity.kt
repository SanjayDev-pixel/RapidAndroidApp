package com.finance.app.view.activity

import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.DialogKycDetailBinding
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.CoApplicantsConnector
import com.finance.app.presenter.presenter.CoApplicantsPresenter
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.ConvertDate
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.ApplicantKycListAdapter
import com.finance.app.view.adapters.recycler.adapter.CardDetailAdapter
import com.finance.app.viewModel.LeadDataViewModel
import kotlinx.android.synthetic.main.layout_zip_address.view.*
import motobeans.architecture.appDelegates.ViewModelType
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApplicantKycListActivity : AppCompatActivity(),ApplicantKycListAdapter.CardClickListener{
    private var progressBar: ProgressBar? = null// temp
    private val presenter = Presenter()
    private var leadId : String ? = null
    private var leadApplicantNumber : String ? = null
    private val leadDataViewModel: LeadDataViewModel by motobeans.architecture.appDelegates.viewModelProvider(this, ViewModelType.WITH_DAO)
    private var leadData :AllLeadMaster?=null
    private var detailKycDialog: Dialog? = null
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
                System.out.println("ResponseObject>>>>"+value.responseObj?.applicantDetails?.size)
                System.out.println("Name>>>>"+value.responseObj!!.applicantDetails[0].firstName)
                if(value.responseObj !=null) {

                    progressBar!!.visibility = View.GONE

                    setUpRecyclerView(value.responseObj?.applicantDetails,leadId!!)
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

    private fun setUpRecyclerView(list:ArrayList<PersonalApplicantsModel> , leadId : String) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewApplicantDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //System.out.println("Name>>>"+list.applicantDetails[0].firstName)
        //creating our adapter
        System.out.println("Applicant Detail>>>>"+list.size)
        val adapter = ApplicantKycListAdapter(this,list,leadId)
        adapter.setOnCardClickListener(this)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter

    }

    override fun onCardFetchKycClicked(position: Int,leadId: String,leadApplicantNumber : String?) {
        System.out.println("Position>>>>"+position+"lead Id>>>>"+leadId+"number>>>"+leadApplicantNumber)
        this.leadApplicantNumber = leadApplicantNumber
        this.leadId = leadId
        callApiKycList(Integer.parseInt(leadId))
    }
    private fun callApiKycList(leadId: Int?) {

        presenter.callNetwork(ConstantsApi.CALL_KYC_DETAIL , CallKYCDetail())
        progressBar!!.visibility = View.VISIBLE
    }
    inner class CallKYCDetail : ViewGeneric<Requests.RequestKycDetail , Response.ResponseKycDetail>(context = this) {
        override val apiRequest: Requests.RequestKycDetail
            get() = getKycDetail()

        override fun getApiSuccess(value: Response.ResponseKycDetail) {
            if (value.responseCode == Constants.SUCCESS) {
                progressBar!!.visibility = View.GONE

                if (value.responseObj.kycApplicantDetailsList.size > 0) {
                    val kycDetailResponse: KycListModel = value.responseObj

                    // open Fragment Dilaog here
                    showKYCDetailDialog(kycDetailResponse)
                } else {
                    showToast("KYC is not available now.")  //value.responseMsg
                    progressBar!!.visibility = View.GONE
                }
            } else {
                showToast(value.responseMsg)
                progressBar!!.visibility = View.GONE
            }
        }

        override fun getApiFailure(msg: String) {
            System.out.println("Api Failure>>>>"+msg)
            if (msg.exIsNotEmptyOrNullOrBlank()) {
                super.getApiFailure(msg)
                progressBar!!.visibility = View.GONE
            } else {
                super.getApiFailure("Time out Error")
                progressBar!!.visibility = View.GONE
            }

        }

        private fun getKycDetail(): Requests.RequestKycDetail {
            val leadId = Integer.parseInt(leadId)
            val leadApplicantNumber= leadApplicantNumber

            return Requests.RequestKycDetail(leadID = leadId!! , leadApplicantNumber = leadApplicantNumber!!) //return Requests.RequestKycDetail(leadID = 2,leadApplicantNumber= "2001")

        }
    }
    private fun showKYCDetailDialog(kycDetailResponse: KycListModel) {

        val bindingDialog = DataBindingUtil.inflate<DialogKycDetailBinding>(LayoutInflater.from(this) , R.layout.dialog_kyc_detail , null , false)
        val mBuilder = AlertDialog.Builder(this)
                .setView(bindingDialog.root)
                .setCancelable(false)

        detailKycDialog = mBuilder.show()

        var name: String? = ""
        var pincode: String? = ""
        var genderValue: String? = ""
        var dob: String? = ""
        var address: String? = ""
        var careOf: String? = ""
        var addressNew: String? = ""
        var matchPercentage: String? = ""
        var faceAuthStatus : String ? = ""

        for (i in 0 until kycDetailResponse.kycApplicantDetailsList.size) {

            if(kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList.size>0) {
                for (j in 0 until kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList.size) {
                    if(j ==0) {
                        pincode = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].pinCode
                        name = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].name
                        genderValue = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].gender
                        dob = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].dob
                        address = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].address
                        careOf = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].careOf
                        matchPercentage = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].faceAuthScore
                        faceAuthStatus = kycDetailResponse.kycApplicantDetailsList[i].kycAadharZipInlineDataList[j].faceAuthStatus
                        bindingDialog.tvName.text = name
                        bindingDialog.tvcareof.text = careOf
                        bindingDialog.tvGender.text = if (genderValue.equals("M")) "Male" else if (genderValue.equals("F")) "Female" else "TransGender"
                        bindingDialog.tvAddress.text = address
                        bindingDialog.tvdob.text = ConvertDate().convertToAppFormatNew(dob)
                        dob = bindingDialog.tvdob.text.toString()
                        bindingDialog.matchpercentage.text = matchPercentage
                        bindingDialog.faceAuthStatus.text = faceAuthStatus
                    }
                }
            }
            else  if(kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList.size>0) {
                for (j in 0 until kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList.size) {
                    if(j ==0) {
                        pincode = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].pincode
                        name = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].name
                        genderValue = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].gender
                        dob = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].dob
                        address = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].address
                        careOf = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].careOf
                        matchPercentage = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].faceAuthScore
                        faceAuthStatus = kycDetailResponse.kycApplicantDetailsList[i].kycPanQrCodeDataList[j].faceAuthStatus

                        System.out.println("Name>>>>>>" + name)
                        bindingDialog.tvName.text = name
                        bindingDialog.tvcareof.text = careOf
                        bindingDialog.tvGender.text = if (genderValue.equals("male")) "Male" else if (genderValue.equals("female")) "Female" else "TransGender"
                        bindingDialog.tvAddress.text = address
                        bindingDialog.tvdob.text = ConvertDate().convertToAppFormatNew(dob)
                        dob = bindingDialog.tvdob.text.toString()
                        bindingDialog.matchpercentage.text = matchPercentage
                        bindingDialog.faceAuthStatus.text = faceAuthStatus
                    }
                }
            }
            else if(kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList.size>0) {
                for (j in 0 until kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList.size) {
                    if(j ==0) {
                        pincode = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].pincode
                        name = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].name
                        genderValue = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].gender
                        dob = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].dob
                        address = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].address
                        careOf = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].careOf
                        matchPercentage = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].faceAuthScore
                        faceAuthStatus = kycDetailResponse.kycApplicantDetailsList[i].kycDLQrCodeDataList[j].faceAuthStatus
                        System.out.println("Name>>>>>>" + name)
                        bindingDialog.tvName.text = name
                        bindingDialog.tvcareof.text = careOf
                        bindingDialog.tvGender.text = if (genderValue.equals("male")) "Male" else if (genderValue.equals("female")) "Female" else "TransGender"
                        bindingDialog.tvAddress.text = address
                        bindingDialog.tvdob.text = ConvertDate().convertToAppFormatNew(dob)
                        dob = bindingDialog.tvdob.text.toString()
                        bindingDialog.matchpercentage.text = matchPercentage
                        bindingDialog.faceAuthStatus.text = faceAuthStatus
                    }


                }

            }

        }

        bindingDialog?.btnClose?.setOnClickListener() {
            detailKycDialog?.dismiss()
        }
        bindingDialog.btnMove.text = "Ok"
        bindingDialog?.btnMove?.setOnClickListener() {

            detailKycDialog?.dismiss()

        }

    }

}