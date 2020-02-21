package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.finance.app.R
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData

import kotlinx.android.synthetic.main.activity_final_submit.*
import kotlinx.android.synthetic.main.content_final_submit.*
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

/* temporary activity*/

class FinalSubmitActivity : AppCompatActivity() {
    private val presenter = Presenter()
    private var progressBar: ProgressBar? = null// temp



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_submit)
        setSupportActionBar(toolbar)
        progressBar = findViewById<ProgressBar>(R.id.progress_Bar) as ProgressBar
        button_submitcall.setOnClickListener { view ->
            progressBar!!.visibility = View.VISIBLE
           // presenter.callNetwork(ConstantsApi.CALL_FINAL_SUBMIT, CallFinalSubmit())
            val intent = Intent(this@FinalSubmitActivity, LoanSubmitStatusActivity::class.java)
            startActivity(intent)


        }

    }

    inner class CallFinalSubmit : ViewGeneric<Requests.RequestFinalSubmit, Response.ResponseFinalSubmit>(context = this) {
        override val apiRequest: Requests.RequestFinalSubmit?
            get() = getCallUpdateRequest()


        override fun getApiSuccess(value: Response.ResponseFinalSubmit) {

            if (value.responseCode == Constants.SUCCESS) {
                Toast.makeText(context,"Submitted Successfully.",Toast.LENGTH_SHORT).show()
                finish()
                progressBar!!.visibility = View.GONE
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

    private fun getCallUpdateRequest(): Requests.RequestFinalSubmit? {

        val leadId = LeadMetaData.getLeadId()

        return Requests.RequestFinalSubmit(leadID = leadId!!)


    }


}

