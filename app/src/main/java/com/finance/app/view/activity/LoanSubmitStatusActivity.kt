package com.finance.app.view.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanSubmitStatusBinding
import com.finance.app.persistence.model.DeviationList
import com.finance.app.persistence.model.FinalSubmitLoanResponseNew
import com.finance.app.persistence.model.RejectionList
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.LoanRejectionAdapter
import com.finance.app.view.adapters.recycler.adapter.LoanSubmitStatusAdapter
import kotlinx.android.synthetic.main.activity_loan_submit_status.view.*
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate


class LoanSubmitStatusActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanSubmitStatusBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_submit_status)
    private var bundle: Bundle? = null
    public var finalSubmitLoanResponse:FinalSubmitLoanResponseNew?=null
    private var loanSubmitStatusAdapter: LoanSubmitStatusAdapter?=null
    private var loanRejectionAdapter:LoanRejectionAdapter?=null



    companion object {
        fun start(context: Context, msg: String?) {

        }
    }


    override fun init() {
        hideSecondaryToolbar()

        bundle = intent!!.extras
        if(bundle!=null) {
            finalSubmitLoanResponse = bundle?.get("SubmitResponse") as FinalSubmitLoanResponseNew
            initViews(finalSubmitLoanResponse)
        }
    }




    private fun initViews(finalSubmitLoanResponse:FinalSubmitLoanResponseNew?) {

        binding.leadNumber.setText(LeadMetaData.getLeadId().toString())
        binding.eligibleAmount.setText(("Rs. ").plus(finalSubmitLoanResponse?.responseObj?.eligibleLoanAmount))
        binding.logoLayout.preapproved_text.setText(getString(R.string.preapproved))
        binding.llstatus.tenure_text.setText(finalSubmitLoanResponse?.responseObj?.ruleEngineResponse?.hfcPolicyResponse!!.finalTenure.toString().plus(" months"))
        binding.llstatus.emi_text.setText(("Rs. ").plus(finalSubmitLoanResponse.responseObj.ruleEngineResponse.hfcPolicyResponse!!.proposedEMI.toString()))


        if (finalSubmitLoanResponse.responseObj.ruleEngineResponse?.hfcPolicyResponse!!.rejectionFlag==true){
            binding.logoLayoutReject.visibility= View.VISIBLE
            setRejectionAdapter(finalSubmitLoanResponse.responseObj.ruleEngineResponse.hfcPolicyResponse!!.rejectionList)

        }else if(finalSubmitLoanResponse.responseObj.ruleEngineResponse.hfcPolicyResponse!!.deviationFlag==true){

            setStatusAdapter(finalSubmitLoanResponse.responseObj.ruleEngineResponse.hfcPolicyResponse!!.deviationList)
        }else{

            // already approved
            setAlreadyApproved(finalSubmitLoanResponse)
            binding.logoLayout.preapproved_text.setText(getString(R.string.preapproved))
            binding.llstatus.tenure_text.setText(finalSubmitLoanResponse.responseObj.ruleEngineResponse.hfcPolicyResponse!!.finalTenure.toString().plus(" months"))
            binding.llstatus.emi_text.setText(("Rs. ").plus(finalSubmitLoanResponse.responseObj.ruleEngineResponse.hfcPolicyResponse!!.proposedEMI.toString()))
            //binding.llstatus.rate.setText(finalSubmitLoanResponse?.hfcPolicyData.)


        }
    }

    private fun setAlreadyApproved(finalSubmitLoanResponse: FinalSubmitLoanResponseNew?) {


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
}
