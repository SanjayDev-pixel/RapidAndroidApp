package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.finance.app.R
import com.finance.app.databinding.ActivityUpdateCallBinding
import kotlinx.android.synthetic.main.activity_update_call.*
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate

class UpdateCallActivity : BaseAppCompatActivity() {

    // used to bind element of layout to activity
    private val binding: ActivityUpdateCallBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_update_call)
    private val callStatus = arrayOf("Call Status","Fixed Meeting", "Not Interested", "Follow up")

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UpdateCallActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
//        hideToolbar()
        hideSecondaryToolbar()

        val callStatusAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, callStatus)
        callStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCallStatus.adapter = callStatusAdapter
        binding.spinnerCallStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override
            fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int,
                               id: Long) {
                showViews(callStatus[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // do Nothing
            }
        }
    }

    private fun showViews(value: Any) {
        when (value) {
            callStatus[1] -> {
                layoutFixedMeeting.visibility = View.VISIBLE
                layoutNotInterested.visibility = View.GONE
                layoutFollowUp.visibility = View.GONE

            }
            callStatus[2] -> {
                layoutFixedMeeting.visibility = View.GONE
                layoutNotInterested.visibility = View.VISIBLE
                layoutFollowUp.visibility = View.GONE
            }
            callStatus[3] -> {
                layoutFixedMeeting.visibility = View.GONE
                layoutNotInterested.visibility = View.GONE
                layoutFollowUp.visibility = View.VISIBLE
            }
        }
    }
}
