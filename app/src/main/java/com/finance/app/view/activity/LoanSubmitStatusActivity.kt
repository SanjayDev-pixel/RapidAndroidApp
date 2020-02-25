package com.finance.app.view.activity


class LoanSubmitStatusActivity {}
/*class LoanSubmitStatusActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanSubmitStatusBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_submit_status)
    private var bundle: Bundle? = null
    public var finalSubmitLoanResponse:FinalSubmitLoanResponse?=null
    private var loanSubmitStatusAdapter: LoanSubmitStatusAdapter?=null
    private var loanRejectionAdapter:LoanRejectionAdapter?=null



    companion object {
        fun start(context: Context, msg: String?) {
        }
    }


    override fun init() {

        bundle = intent?.extras
      //  finalSubmitLoanResponse = bundle?.get("SubmitResponse") as FinalSubmitLoanResponse

       // initViews(finalSubmitLoanResponse)
    }





    private fun initViews(finalSubmitLoanResponse:FinalSubmitLoanResponse?) {

        binding.leadNumber.setText(LeadMetaData.getLeadId().toString())
        binding.eligibleAmount.setText(("Rs. ").plus(finalSubmitLoanResponse?.hfcPolicyData!!.finalLoanOfferAmount))



        if (finalSubmitLoanResponse?.hfcPolicyData!!.rejectionFlag==true){
            binding.logoLayout.preapproved_text.setText(getString(R.string.rejection))
            setRejectionAdapter(finalSubmitLoanResponse?.hfcPolicyData!!.rejectionList)

        }else if(finalSubmitLoanResponse?.hfcPolicyData!!.deviationFlag==true){
            binding.logoLayout.preapproved_text.setText(getText(R.string.deviation))
            setStatusAdapter(finalSubmitLoanResponse?.hfcPolicyData!!.deviationList)
        }else{

            // already approved
            setAlreadyApproved(finalSubmitLoanResponse)
            binding.logoLayout.preapproved_text.setText(getString(R.string.preapproved))
            binding.llstatus.tenure_text.setText(finalSubmitLoanResponse?.hfcPolicyData.finalTenure.toString().plus(" months"))
            binding.llstatus.emi_text.setText(("Rs. ").plus(finalSubmitLoanResponse?.hfcPolicyData.proposedEMI.toString()))
            //binding.llstatus.rate.setText(finalSubmitLoanResponse?.hfcPolicyData.)




        }


    }

    private fun setAlreadyApproved(finalSubmitLoanResponse: FinalSubmitLoanResponse?) {


    }

    private fun setStatusAdapter(deviationList: ArrayList<DeviationList>){

        binding.recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        loanSubmitStatusAdapter = LoanSubmitStatusAdapter(this, deviationList)
        binding.recyclerview.adapter = loanSubmitStatusAdapter

    }


    private fun setRejectionAdapter(rejectionList: ArrayList<RejectionList>) {

        binding.recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        loanRejectionAdapter = LoanRejectionAdapter(this, rejectionList)
        binding.recyclerview.adapter = loanSubmitStatusAdapter

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setOnClickListners() {

    }
}*/
