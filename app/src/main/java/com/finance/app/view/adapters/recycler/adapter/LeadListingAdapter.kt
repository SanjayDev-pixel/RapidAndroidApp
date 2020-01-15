package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemNewLeadBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.adapters.recycler.Holder.LeadsListingHolder

class LeadListingAdapter(private val mContext: Context, private val leads: ArrayList<AllLeadMaster>?) :
        RecyclerView.Adapter<LeadsListingHolder>() {

    private lateinit var binding: ItemNewLeadBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadsListingHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_new_lead, parent, false)
        return LeadsListingHolder(binding, mContext)
    }

    override fun getItemCount() = leads?.size ?: 0

    override fun onBindViewHolder(holder: LeadsListingHolder, position: Int) {
        if (!leads.isNullOrEmpty()) {
            holder.bindItems(leads[position])
        }
    }
}
