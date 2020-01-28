package com.finance.app.view.adapters.recycler.holder

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemLeadsBinding
import com.finance.app.others.AppEnums
import com.finance.app.others.setTextVertically
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.utility.ConvertDate
import com.finance.app.view.activity.LeadDetailActivity

class LeadsListingHolder(val binding: ItemLeadsBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bindItems(lead: AllLeadMaster, adapterFor: AppEnums.LEAD_TYPE?) {
        if (adapterFor == AppEnums.LEAD_TYPE.ALL) {
            binding.tvStatusLine.setTextVertically(lead.status)
        }

        binding.tvLeadName.text = lead.applicantFirstName
        binding.tvLeadID.text = "Lead Id : ${lead.leadID.toString()}"
//        binding.tvLeadStatus.text = lead.status
        binding.tvLoanType.text = "Loan Type : ${lead.loanProductName}"
//        binding.tvLeadAddress.text = lead.applicantAddress
        binding.tvCreatedDate.text = "Created Date : ${ConvertDate().convertDate(lead.createdOn!!)}"
        binding.tvUpdatedDate.text = ConvertDate().convertDate(lead.lastModifiedOn!!)

        when (lead.status) {
            AppEnums.LEAD_TYPE.NEW.type -> binding.tvStatusLine.setBackgroundColor(mContext.resources.getColor(R.color.lead_status_new))
            AppEnums.LEAD_TYPE.SUBMITTED.type -> binding.tvStatusLine.setBackgroundColor(mContext.resources.getColor(R.color.lead_status_submitted))
            AppEnums.LEAD_TYPE.PENDING.type -> binding.tvStatusLine.setBackgroundColor(mContext.resources.getColor(R.color.lead_status_pending))
            AppEnums.LEAD_TYPE.REJECTED.type -> binding.tvStatusLine.setBackgroundColor(mContext.resources.getColor(R.color.lead_status_rejected))
        }
        binding.leadCard.setOnClickListener {
            LeadDetailActivity.start(mContext, lead)
        }
    }
}