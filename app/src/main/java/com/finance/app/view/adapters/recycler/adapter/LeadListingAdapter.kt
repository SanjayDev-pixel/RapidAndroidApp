package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemLeadBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.activity.LeadDetailActivity
import java.util.*

class LeadListingAdapter(private val mContext: Context, private val leads: ArrayList<AllLeadMaster>) :
        RecyclerView.Adapter<LeadListingAdapter.LeadManagementViewHolder>() {

    private lateinit var binding: ItemLeadBinding

    override fun onBindViewHolder(holder: LeadManagementViewHolder, position: Int) {
        holder.bindItems(leads[position])
    }

    override fun getItemCount() = leads.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadManagementViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lead, parent, false)
        return LeadManagementViewHolder(binding)
    }

    inner class LeadManagementViewHolder(val binding: ItemLeadBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(lead: AllLeadMaster) {
            binding.tvLeadName.text = lead.applicantFirstName
            binding.tvLeadStatus.text = lead.status
            binding.tvLoanType.text = lead.loanProductName
            binding.tvLeadAddress.text = lead.applicantAddress
            convertToHumanFormat(lead.lastModifiedOn!!)
            binding.leadCard.setOnClickListener {
                LeadDetailActivity.start(mContext, lead.leadID)
            }
        }

        private fun convertToHumanFormat(timeStamp: Long) {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp * 1000L
            val date = DateFormat.format("dd MMM : hh:mm", cal).toString()
            binding.tvDateAndTime.text = date
        }
    }
}
