package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemNewLeadBinding
//import com.finance.app.databinding.ItemPendingLeadBinding
//import com.finance.app.databinding.ItemRejectedLeadBinding
//import com.finance.app.databinding.ItemSubmittedLeadBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.view.adapters.recycler.holder.NewLeadHolder

class LeadListingAdapterTest(private val mContext: Context, private val leads: ArrayList<AllLeadMaster>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val PENDING = 1
        const val SUBMITTED = 2
        const val REJECTED = 3
        const val NEW = 4
    }

    private lateinit var newLea: ScriptGroup.Binding

    private lateinit var newLeadBinding: ItemNewLeadBinding
//    private lateinit var submittedLeadBinding: ItemSubmittedLeadBinding
//    private lateinit var rejectedLeadBinding: ItemRejectedLeadBinding
//    private lateinit var pendingLeadBinding: ItemPendingLeadBinding

    override fun getItemViewType(position: Int): Int {
        return when (leads[position].status) {
            AppEnums.LEAD_TYPE.PENDING.type -> PENDING
            AppEnums.LEAD_TYPE.REJECTED.type -> REJECTED
            AppEnums.LEAD_TYPE.SUBMITTED.type -> SUBMITTED
            else -> NEW
        }
    }

    override fun getItemCount() = leads.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
//            PENDING -> initLayoutPending(holder as PendingLeadHolder, position)
//            SUBMITTED -> initLayoutSubmitted(holder as SubmittedLeadHolder, position)
//            REJECTED -> initLayoutRejected(holder as RejectedLeadHolder, position)
            NEW -> initLayoutNew(holder as NewLeadHolder, position)
        }
    }

    private fun initLayoutPending(holder: RecyclerView.ViewHolder, pos: Int) {
//        holder.bindItems(leads[pos])
    }

//    private fun initLayoutSubmitted(holder: SubmittedLeadHolder, pos: Int) {
//        holder.bindItems(leads[pos])
//    }
//
//    private fun initLayoutRejected(holder: RejectedLeadHolder, pos: Int) {
//        holder.bindItems(leads[pos])
//    }

    private fun initLayoutNew(holder: NewLeadHolder, pos: Int) {
        holder.bindItems(leads[pos])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

//        return when (viewType) {
//            PENDING -> {
//                pendingLeadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_pending_lead, parent, false)
//                PendingLeadHolder(pendingLeadBinding, mContext)
//            }
//            REJECTED -> {
//                rejectedLeadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_rejected_lead, parent, false)
//                RejectedLeadHolder(rejectedLeadBinding, mContext)
//            }
//            SUBMITTED -> {
//                submittedLeadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_submitted_lead, parent, false)
//                SubmittedLeadHolder(submittedLeadBinding, mContext)
//            }
//            else -> {
        newLeadBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_new_lead, parent, false)
        return NewLeadHolder(newLeadBinding, mContext)
    }
}
