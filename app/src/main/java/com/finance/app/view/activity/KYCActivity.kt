package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.finance.app.R
import com.finance.app.databinding.ActivityKycBinding
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadMetaData
import com.google.zxing.integration.android.IntentIntegrator
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.Constants.API.URL.URL_KYC
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate


class KYCActivity : BaseAppCompatActivity() {

    private val binding: ActivityKycBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_kyc)

    private val kycPresenter = Presenter()
    private var bundle: Bundle? = null
    var kyCID: String? = null

    companion object {
        fun start(context: Context, leadApplicantNum: String?) {
            val intent = Intent(context, KYCActivity::class.java)
            val bundle = Bundle()
            bundle.putString(Constants.KEY_LEAD_APP_NUM, leadApplicantNum)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        hideSecondaryToolbar()
        setClickListeners()
        proceedFurther()
        //SAcn Adhar QR
        //scanNow()
    }

    private fun setClickListeners() {
    }
    private fun scanNow(){
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator.setPrompt("Scan a Aadharcard QR Code")
        integrator.setResultDisplayDuration(500)
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int , resultCode: Int , data: Intent?) {
        super.onActivityResult(requestCode , resultCode , data)
        //Reterive Scan Result
        val scanningResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(scanningResult != null){
            //we have a result
            //we have a result
            val scanContent = scanningResult.contents
            val scanFormat = scanningResult.formatName

            print("Scanned data>>>$scanContent")
            print("Scanned Type>>>>$scanFormat")
            Toast.makeText(this, scanContent + "   type:" + scanFormat, Toast.LENGTH_SHORT).show();
        }

    }

    private fun proceedFurther() {
        bundle = intent.extras
        bundle?.let {

            val leadAppNum = bundle?.getString(Constants.KEY_LEAD_APP_NUM)
            leadAppNum?.let {
                kycPresenter.callNetwork(ConstantsApi.CALL_KYC, dmiConnector = KYCApiCall(leadAppNum))
            }
        }
    }


    inner class KYCApiCall(private val leadAppNum: String) : ViewGeneric<Requests.RequestKYC,
            Response.ResponseKYC>(context = this) {

        override val apiRequest: Requests.RequestKYC
            get() = mRequestKyc

        private val mRequestKyc: Requests.RequestKYC
            get() {
                val leadId = LeadMetaData.getLeadId()
                return Requests.RequestKYC(leadID = leadId, leadApplicantNumber = leadAppNum)
            }

        override fun getApiSuccess(value: Response.ResponseKYC) {
            if (value.responseCode == Constants.SUCCESS) {
                val response = value.responseObj
                response?.let {
                    openWebViewForKYCData(response.kycID)
//                    showToast("Success")
                    finish()
                }
            } else {
                getApiFailure(value.responseMsg)
            }
        }

        private fun openWebViewForKYCData(kycID: String?) {
            kycID?.let {
                //                CustomChromeTab().openUrl(activity = this@KYCActivity, url = (URL_KYC + kycID))
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
                builder.setShowTitle(false)

                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(this@KYCActivity, Uri.parse(URL_KYC + kycID))
            }
        }

        override fun getApiFailure(msg: String) {
            showToast(msg)
        }
    }
}


