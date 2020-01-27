package com.finance.app.view.adapters.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemLeadsBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.adapters.recycler.holder.LeadsListingHolder
import com.finance.app.viewModel.AppDataViewModel

class LeadListingAdapter(private val mContext: FragmentActivity, private val appDataViewModel: AppDataViewModel,
                         private val leadStatusEnums: AppEnums.LEAD_TYPE) : RecyclerView.Adapter<LeadsListingHolder>() {

    private lateinit var binding: ItemLeadsBinding
    private var leads = ArrayList<AllLeadMaster>()

    init {
        when (leadStatusEnums.type) {
            AppEnums.LEAD_TYPE.ALL.type -> getAllLeads()
            else -> getLeadsByStatus(leadStatusEnums.type)
        }
    }

    private fun getAllLeads() {
        appDataViewModel.getAllLeads().observe(mContext, Observer { itemsFromDB ->
            itemsFromDB?.let {
                leads.clear()
                leads.addAll(itemsFromDB)

                notifyDataSetChanged()
            }
        })

    }

    private fun getLeadsByStatus(status: String) {
        appDataViewModel.getLeadsByStatus(status).observe(mContext, Observer { itemsFromDB ->
            itemsFromDB?.let {
                leads.clear()
                leads.addAll(itemsFromDB)

                notifyDataSetChanged()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadsListingHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_leads, parent, false)
        return LeadsListingHolder(binding, mContext)
    }

    override fun getItemCount() = leads.size

    override fun onBindViewHolder(holder: LeadsListingHolder, position: Int) {
        if (!leads.isNullOrEmpty()) {
            holder.bindItems(leads[position], leadStatusEnums)
        }
    }
}
