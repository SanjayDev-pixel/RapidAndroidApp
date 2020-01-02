package com.finance.app.view.adapters.recycler.Holder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.EmptyCommonListBinding
import com.finance.app.databinding.ItemNavBinding
import com.finance.app.databinding.ItemNewLeadBinding
import com.finance.app.databinding.ItemPendingLeadBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.utility.ConvertDate
import com.finance.app.view.activity.LeadDetailActivity
import com.finance.app.view.adapters.recycler.adapter.NavMenuConnector
import motobeans.architecture.util.setDrawableColor

class PendingLeadHolder(val binding: ItemPendingLeadBinding, val mContext:Context) : RecyclerView.ViewHolder(binding.root) {
    fun bindItems(lead: AllLeadMaster) {
        binding.tvLeadName.text = lead.applicantFirstName
        binding.tvLeadStatus.text = lead.status
        binding.tvLoanType.text = lead.loanProductName
        binding.tvLeadAddress.text = lead.applicantAddress
        binding.tvDateAndTime.text = ConvertDate().convertDate(lead.createdOn!!)

        binding.tvDateAndTime.setDrawableColor(R.color.gradient_black)
        binding.tvLeadStatus.setDrawableColor(R.color.md_yellow_400)

        binding.leadCard.setOnClickListener {
            LeadDetailActivity.start(mContext, lead.leadID)
        }
    }
}