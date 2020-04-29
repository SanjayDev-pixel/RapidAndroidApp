package com.finance.app.view.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import com.finance.app.R
import com.finance.app.databinding.ActivityKycBinding
import com.finance.app.databinding.KycoptiondialogBinding
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
import java.nio.charset.StandardCharsets
import java.util.*


class KYCActivity : BaseAppCompatActivity() {

    private val binding: ActivityKycBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_kyc)

    private val kycPresenter = Presenter()
    private var bundle: Bundle? = null
    var kyCID: String? = null
    private var kycOptionDialog: Dialog? = null
    var encodedStringScanned=""

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
       // scanNow()
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

            val scanContent = scanningResult.contents
            val scanFormat = scanningResult.formatName
            val byteData = scanningResult.rawBytes.toString()
            val extradTa = data.toString()
            System.out.println("byteData>>>>"+scanningResult.toString())

            print("Scanned data>>>$scanContent")
            print("Scanned Type>>>>$scanFormat")
           // Toast.makeText(this, scanContent + "   type:" + scanFormat, Toast.LENGTH_SHORT).show()
            System.out.println("Scanned data >>>>"+scanContent+"Type>>>>"+scanFormat)
            Log.e("Tag"," sandeep scan data:: "+ scanFormat + " scan content;;;;;;;;;;;;;;;;" +scanContent)
            val encodedString: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Base64.getEncoder().encodeToString(scanContent.toByteArray())
            } else {
                val data = scanContent.toByteArray(charset("UTF-8"))
                android.util.Base64.encodeToString(data,android.util.Base64.DEFAULT)
            }

           Log.e("Tag","base 64 data:::: "+encodedString)
            encodedStringScanned=encodedString



        }

    }

    fun String.encode(): String {
        return android.util.Base64.encodeToString(this.toByteArray(charset("UTF-8")), android.util.Base64.DEFAULT)
    }

    private fun proceedFurther() {



        bundle = intent.extras
        bundle?.let {

            val leadAppNum = bundle?.getString(Constants.KEY_LEAD_APP_NUM)
            leadAppNum?.let {
                showKycDialog(leadAppNum)
              //  kycPresenter.callNetwork(ConstantsApi.CALL_KYC, dmiConnector = KYCApiCall(leadAppNum))
            }
        }
    }


    private fun showKycDialog(leadApplicantNumber: String?) {

        val bindingDialog = DataBindingUtil.inflate<KycoptiondialogBinding>(LayoutInflater.from(this) , R.layout.kycoptiondialog , null , false)
        val mBuilder = AlertDialog.Builder(this)
                .setView(bindingDialog.root)
                .setCancelable(false)

        kycOptionDialog = mBuilder.show()

        bindingDialog?.btnClose?.setOnClickListener() {
            kycOptionDialog?.dismiss()
        }

        bindingDialog?.groupRadioButton?.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group , checkedId ->

                    if (checkedId == R.id.adharotp) {
                        val leadAppNum = leadApplicantNumber
                       /* leadAppNum?.let {
                            kycPresenter.callNetwork(ConstantsApi.CALL_KYC , dmiConnector = KYCApiCall(leadAppNum))
                        }*/

                    } else if (checkedId == R.id.codeand_pan) {
                        scanNow()

                    } else if (checkedId == R.id.codeand_dl) {
                        Toast.makeText(this , "Currently System working on Aadhar Otp and QR Code and PAN." , Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this , "Currently System working on Aadhar Otp and QR Code and PAN." , Toast.LENGTH_SHORT).show()
                    }
                })




        bindingDialog?.btnProceed?.setOnClickListener() {
            val leadAppNum = leadApplicantNumber
            val radioButtonselect = bindingDialog.groupRadioButton.checkedRadioButtonId
            if (radioButtonselect == R.id.adharotp) {

                leadAppNum?.let {
                    kycPresenter.callNetwork(ConstantsApi.CALL_KYC , dmiConnector = KYCApiCall(leadAppNum))
                }
            }else if (radioButtonselect == R.id.codeand_pan) {

                kycPresenter.callNetwork(ConstantsApi.CALL_KYC_PREPARE , dmiConnector = KYCidApiCall(leadAppNum))
            } else {
                Toast.makeText(this , "Please select KYC type" , Toast.LENGTH_SHORT).show()
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




    inner class KYCidApiCall(private val leadAppNum: String?) : ViewGeneric<Requests.RequestKYCID ,
            Response.ResponseKYC>(context = this) {
        val qrCodeString = ""
        override val apiRequest: Requests.RequestKYCID
            get() = mRequestKyc

        private val mRequestKyc: Requests.RequestKYCID
            get() {
                val leadId = LeadMetaData.getLeadId()
                return Requests.RequestKYCID(leadID = leadId , leadApplicantNumber = leadAppNum , qrCodeData = encodedStringScanned , kycType = "QRCODE_PAN_REQUEST")

            }

        override fun getApiSuccess(value: Response.ResponseKYC) {
            if (value.responseCode == Constants.SUCCESS) {
                val response = value.responseObj
                response?.let {

                    openWebViewForPAN(response.kycID)
                    kycOptionDialog!!.dismiss()
                    finish()

                }
            } else {
                getApiFailure(value.responseMsg)
            }
        }

        private fun openWebViewForPAN(kycID: String?) {
            kycID?.let {
                //                CustomChromeTab().openUrl(activity = this@KYCActivity, url = (URL_KYC + kycID))
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
                builder.setShowTitle(false)

                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context , Uri.parse(Constants.API.URL.URL_KYC + kycID + "&qrCode=true"))
            }
        }


        override fun getApiFailure(msg: String) {
            showToast(msg)
        }
    }
}


