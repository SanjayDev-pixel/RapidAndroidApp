package com.finance.app.view.adapters.recycler.holder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.PreviewLayoutDocumentChecklistBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.ApplicantionSubmitModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.LoanSubmitStatusActivity
import com.finance.app.viewModel.AppDataViewModel
import kotlinx.android.synthetic.main.preview_layout_document_checklist.view.*
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank


class PreviewDocumentsHolder(val binding: PreviewLayoutDocumentChecklistBinding, val mContext: Context)
    : RecyclerView.ViewHolder(binding.root) {

    private val presenter = Presenter()
    var lastIndex:Int=0
    var listSize:Int=0
    // private var progressBar: ProgressBar? = null// temp

    fun bindItems(list: ArrayList<PersonalApplicantsModel>?, pos: Int, viewModel: AppDataViewModel) {

        if (!list.isNullOrEmpty()) {

             listSize=list.size
            lastIndex= list.lastIndex
            setValueInCard(list[pos],listSize,lastIndex)

        }
    }

    private fun setValueInCard(data: PersonalApplicantsModel,listSize:Int,lastIndex:Int) {

        if (data.isMainApplicant == true && listSize==1) {

            binding.layoutSubmitlead.visibility=View.VISIBLE
        }else if(data.isMainApplicant == false && listSize>1){

            binding.layoutSubmitlead.visibility=View.VISIBLE
        }





        binding.layoutSubmitlead.btnSubmit.setOnClickListener() {

            if (LeadMetaData.getLeadData()?.status == "Submitted") {
                Toast.makeText(mContext, "Lead is already submitted.", Toast.LENGTH_SHORT).show()
                val lead: AllLeadMaster? = LeadMetaData.getLeadData()
                getSubmittedStateResponse(lead)
            } else {

                val lead: AllLeadMaster? = LeadMetaData.getLeadData()
                checkAndStartLoanApplicationActivity(lead)

            }

        }

        binding.layoutSubmitlead.btnCancel.setOnClickListener(){

            (mContext as Activity).finish()
        }

    }

    private fun getSubmittedStateResponse(lead: AllLeadMaster?) {

        presenter.callNetwork(ConstantsApi.Call_FINAL_RESPONSE, CallFinalSubmitResponse())


    }
//
    private fun checkAndStartLoanApplicationActivity(lead: AllLeadMaster?) {

        val isLeadOfflineDataSync = lead!!.isDetailAlreadySync

        when (isLeadOfflineDataSync) {
            true -> presenter.callNetwork(ConstantsApi.CALL_FINAL_SUBMIT, CallFinalSubmit())
            false -> Toast.makeText(mContext, "Please wait lead is syncronise with server", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CallFinalSubmit : ViewGeneric<Requests.RequestFinalSubmit, Response.ResponseFinalSubmit>(context = mContext) {
        override val apiRequest: Requests.RequestFinalSubmit?
            get() = getCallUpdateRequest()

        override fun getApiSuccess(value: Response.ResponseFinalSubmit) {

            if (value.responseCode == Constants.SUCCESS) {
                Toast.makeText(context, "Submitted Successfully.", Toast.LENGTH_SHORT).show()

                   binding.progressBar!!.visibility = View.GONE

                val submitLoanResponse: ApplicantionSubmitModel? = value.responseObj

                if (value.responseObj != null) {
                    val intent = Intent(mContext, LoanSubmitStatusActivity::class.java)
                    intent.putExtra("SubmitResponse", submitLoanResponse)
                    mContext.startActivity(intent)


                } else {

                    showToast(value.responseMsg)
                }


            } else {
                showToast(value.responseMsg)
                  binding.progressBar!!.visibility = View.GONE
            }

        }

        override fun getApiFailure(msg: String) {

            if (msg.exIsNotEmptyOrNullOrBlank()) {
                super.getApiFailure(msg)
                   binding.progressBar!!.visibility = View.GONE
            } else {
                super.getApiFailure("Time out Error")
                binding.progressBar!!.visibility = View.GONE}

            }


        private fun getCallUpdateRequest(): Requests.RequestFinalSubmit? {
            val leadId = LeadMetaData.getLeadId()
            return Requests.RequestFinalSubmit(leadID = leadId!!)

        }

    }

    inner class CallFinalSubmitResponse:ViewGeneric<Requests.RequestSubmittedLead, Response.ResponseFinalSubmitted>(context = mContext) {
        override val apiRequest: Requests.RequestSubmittedLead?
            get() = getRequestSubmittedLead()


        override fun getApiSuccess(value: Response.ResponseFinalSubmitted) {

            if (value.responseCode == Constants.SUCCESS) {

                binding.progressBar!!.visibility = View.GONE

                val submitLoanResponse: ApplicantionSubmitModel? = value.responseObj

                if (value.responseObj != null) {
                    val intent = Intent(mContext, LoanSubmitStatusActivity::class.java)
                    intent.putExtra("SubmitResponse", submitLoanResponse)
                    mContext.startActivity(intent)


                } else {

                    showToast(value.responseMsg)
                }


            } else {
                showToast(value.responseMsg)
                binding.progressBar!!.visibility = View.GONE
            }

        }

        override fun getApiFailure(msg: String) {

            if (msg.exIsNotEmptyOrNullOrBlank()) {
                super.getApiFailure(msg)
                binding.progressBar!!.visibility = View.GONE
            } else {
                super.getApiFailure("Time out Error")
                binding.progressBar!!.visibility = View.GONE}

        }



        private fun getRequestSubmittedLead():Requests.RequestSubmittedLead?{
            val leadId:Int?=LeadMetaData.getLeadId()
            return Requests.RequestSubmittedLead(leadID=leadId!!)
        }

    }




}

