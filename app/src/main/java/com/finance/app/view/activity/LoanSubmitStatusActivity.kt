package com.finance.app.view.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ActivityLoanSubmitStatusBinding
import com.finance.app.persistence.model.*
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.LoanRejectionAdapter
import com.finance.app.view.adapters.recycler.adapter.LoanSubmitStatusAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_loan_submit_status.view.*
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import java.math.RoundingMode
import java.text.DecimalFormat


class LoanSubmitStatusActivity : BaseAppCompatActivity() {

    private val binding: ActivityLoanSubmitStatusBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_loan_submit_status)
    private var bundle: Bundle? = null
    public var finalSubmitLoanResponse: ApplicantionSubmitModel? = null
    private var loanSubmitStatusAdapter: LoanSubmitStatusAdapter? = null
    private var loanRejectionAdapter: LoanRejectionAdapter? = null
    private var hfcPolicyResponse: HfcPolicyResponse? = null


    companion object {
        fun start(context: Context, msg: String?) {

        }
    }


    override fun init() {
        hideSecondaryToolbar()

        bundle = intent!!.extras
        if (bundle != null) {
            finalSubmitLoanResponse = bundle?.get("SubmitResponse") as ApplicantionSubmitModel
            initViews(finalSubmitLoanResponse)
        }

    }


    private fun initViews(finalSubmitLoanResponse: ApplicantionSubmitModel?) {

        binding.leadNumber.setText(LeadMetaData.getLeadData()?.leadNumber)
        binding.eligibleAmount.setText(("Rs. ").plus(finalSubmitLoanResponse?.eligibleLoanAmount))
        binding.logoLayout.preapproved_text.setText(getString(R.string.preapproved))
        val json = finalSubmitLoanResponse?.ruleEngineResponse
        val gson = Gson()


        val ruleEngineResponse = gson.fromJson(json, RuleEngineResponse::class.java)

        binding.llstatus.tenure_text.setText(ruleEngineResponse.hfcPolicyResponse?.finalTenure.toString().plus(" months"))
        var proposedEMI =ruleEngineResponse.hfcPolicyResponse?.proposedEMI
        if(proposedEMI != null){
        binding.llstatus.emi_text.setText(("Rs. ").plus(roundOffDecimal(proposedEMI)))
        }
        binding.llstatus.rate.setText(ruleEngineResponse.hfcPolicyResponse?.roi.toString().plus("%"))



       if (ruleEngineResponse.hfcPolicyResponse!!.rejectionFlag == true) {
            binding.logoLayoutReject.visibility = View.VISIBLE
            setRejectionAdapter(ruleEngineResponse.hfcPolicyResponse!!.rejectionList)

        } else if (ruleEngineResponse.hfcPolicyResponse!!.deviationFlag == true) {
            binding.logoLayoutDeviation.visibility =View.VISIBLE
            setStatusAdapter(ruleEngineResponse.hfcPolicyResponse!!.deviationList)
        } else {
            binding.logoLayout.visibility= View.VISIBLE
           binding.leadStatusTxt.setText("")
            // already approved
            setAlreadyApproved(finalSubmitLoanResponse)
        }

        setOnClickListners()
    }

    private fun setAlreadyApproved(finalSubmitLoanResponse: ApplicantionSubmitModel?) {



    }

    private fun setStatusAdapter(deviationList: ArrayList<DeviationList>) {
        binding.leadStatusTxt.setText("Deviation")
        binding.recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        loanSubmitStatusAdapter = LoanSubmitStatusAdapter(this, deviationList)
        binding.recyclerview.adapter = loanSubmitStatusAdapter

    }


    private fun setRejectionAdapter(rejectionList: ArrayList<RejectionList>) {
        binding.leadStatusTxt.setText("Rejection")
        binding.recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        loanRejectionAdapter = LoanRejectionAdapter(this, rejectionList)
        binding.recyclerview.adapter = loanRejectionAdapter

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setOnClickListners() {

        binding.bttnDashboard.setOnClickListener(){
            DashboardActivity.start(this)
            this.finish()
        }

    }

    fun roundOffDecimal(number: Double): Double? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
}
