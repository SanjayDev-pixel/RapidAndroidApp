package com.finance.app.view.activity

import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity

class KYCActivity : BaseAppCompatActivity() {
    override fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    private val binding: ActivityKycBinding by ActivityBindingProviderDelegate(
//            this, R.layout.activity_kyc)
//
//    private val kycPresenter = Presenter()
//    private var bundle: Bundle? = null
//
//    companion object {
//        fun start(context: Context, leadApplicantNum: String?) {
//            val intent = Intent(context, KYCActivity::class.java)
//            val bundle = Bundle()
//            bundle.putString(Constants.KEY_LEAD_APP_NUM, leadApplicantNum)
//            intent.putExtras(bundle)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)
//        }
//    }
//
//    override fun init() {
//        hideToolbar()
//        hideSecondaryToolbar()
//        setClickListeners()
//        proceedFurther()
//    }
//
//    private fun setClickListeners() {
//        binding.etIssueDate.setOnClickListener {
//            SelectDate(binding.etIssueDate, this)
//        }
//        binding.etExpiryDate.setOnClickListener {
//            SelectDate(binding.etExpiryDate, this)
//        }
//    }
//
//    private fun proceedFurther() {
//        bundle = intent.extras
//        bundle?.let {
//
//            val leadAppNum = bundle?.getString(Constants.KEY_LEAD_APP_NUM)
//            leadAppNum?.let {
//                kycPresenter.callNetwork(ConstantsApi.CALL_KYC, dmiConnector = KYCApiCall(leadAppNum))
//            }
//        }
//    }
//
//
//    inner class KYCApiCall(private val leadAppNum: String) : ViewGeneric<Requests.RequestKYC,
//            Response.ResponseKYC>(context = this) {
//
//        override val apiRequest: Requests.RequestKYC
//            get() = mLoginRequestLogin
//
//        private val mLoginRequestLogin: Requests.RequestKYC
//            get() {
//                val leadId = LeadMetaData.getLeadId()
//                return Requests.RequestKYC(leadID = leadId, leadApplicantNumber = leadAppNum)
//            }
//
//        override fun getApiSuccess(value: Response.ResponseKYC) {
//            if (value.responseCode == Constants.SUCCESS) {
//                val response = value.responseObj
//                response?.let {
//                    openWebViewForKYCData(response.kycID)
//                    showToast("Success")
//                }
//            } else {
//                getApiFailure(value.responseMsg)
//            }
//        }
//
//        private fun openWebViewForKYCData(kycID: String?) {
//            kycID?.let {
//                binding.llKYC.visibility = View.GONE
//                binding.webView.visibility = View.VISIBLE
//                binding.webView.settings.javaScriptEnabled
//
//                val headerMap: HashMap<String, String> = HashMap()
//                headerMap["Authorization"] = "Bearer".plus(kycID)
//                headerMap["ApplicationUserAgent"] = "dmi-droid"
//                binding.webView.loadUrl(URL_KYC, headerMap)
//
//                //    binding.webView.loadUrl(URL_KYC.plus(kycID))
//
//            }
//        }
//
//        override fun getApiFailure(msg: String) {
//            binding.webView.visibility = View.GONE
//            binding.llKYC.visibility = View.VISIBLE
//            showToast(msg)
//        }
//    }
}