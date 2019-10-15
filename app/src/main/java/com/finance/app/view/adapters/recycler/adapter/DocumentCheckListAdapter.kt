package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemDocumentChecklistBinding

class DocumentCheckListAdapter(private val mContext: Context) : RecyclerView.Adapter<DocumentCheckListAdapter.DocumentCheckListViewHolder>() {
    private lateinit var binding: ItemDocumentChecklistBinding
    private val documentStatus = arrayOf("Select","YES", "NO", "NA")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentCheckListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_document_checklist, parent, false)
        return DocumentCheckListViewHolder(binding, mContext)
    }

    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: DocumentCheckListViewHolder, position: Int) {
        holder.bindItems(position)
    }

    inner class DocumentCheckListViewHolder(val binding: ItemDocumentChecklistBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
            val adapterDocumentStatus = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, documentStatus)
            adapterDocumentStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDocumentStatus.adapter = adapterDocumentStatus
        }
    }
}
