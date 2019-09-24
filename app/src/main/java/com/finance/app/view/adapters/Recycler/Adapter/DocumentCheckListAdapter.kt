package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemDocumentChecklistBinding
import com.finance.app.databinding.ItemObligationBinding

class DocumentCheckListAdapter(private val mContext: Context) : RecyclerView.Adapter<DocumentCheckListAdapter.DocumentCheckListViewHolder>() {
    private lateinit var binding: ItemDocumentChecklistBinding

    override fun onBindViewHolder(holder: DocumentCheckListViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount() = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentCheckListViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_document_checklist, parent, false)
        return DocumentCheckListViewHolder(binding, mContext)
    }

    inner class DocumentCheckListViewHolder(val binding: ItemDocumentChecklistBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
        }
    }
}
