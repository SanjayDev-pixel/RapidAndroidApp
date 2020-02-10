package com.finance.app.view.activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.finance.app.R
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData

import kotlinx.android.synthetic.main.activity_final_submit.*
import kotlinx.android.synthetic.main.content_final_submit.*
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response

/* temporary activity*/

class FinalSubmitActivity : AppCompatActivity() {
    private val presenter = Presenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_submit)
        setSupportActionBar(toolbar)

        button_submitcall.setOnClickListener { view ->

            presenter.callNetwork(ConstantsApi.CALL_FINAL_SUBMIT, CallFinalSubmit())

        }



    }
    inner class CallFinalSubmit : ViewGeneric<Requests.RequestFinalSubmit, Response.ResponseCallUpdate>(context = this) {
        override val apiRequest: Requests.RequestFinalSubmit?
            get() = getCallUpdateRequest()

        override fun getApiSuccess(value: Response.ResponseCallUpdate) {
            //write code here after getting successful
        }
    }

    private fun getCallUpdateRequest(): Requests.RequestFinalSubmit? {
        val leadId = LeadMetaData.getLeadId()


        return Requests.RequestFinalSubmit(leadID = leadId!!)


    }
}

