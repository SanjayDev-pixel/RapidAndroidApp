package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemDocumentChecklistBinding
import com.finance.app.persistence.model.DocumentCheckListDetailModel

class CheckListAdapter (private val c: Context, private val documentCheck: ArrayList<DocumentCheckListDetailModel>) : RecyclerView.Adapter<CheckListAdapter.CheckListDetailViewHolder>() {
    private lateinit var binding: ItemDocumentChecklistBinding
    private var mOnAssetClickListener: AssetClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_asset, parent, false)
        return CheckListDetailViewHolder(binding, c)
    }

    override fun getItemCount() = documentCheck.size

    fun setOnAssetClickListener(listner: AssetClickListener) {
        mOnAssetClickListener = listner
    }

    interface AssetClickListener {
        fun onAssetDeleteClicked(position: Int)
        fun onAssetEditClicked(position: Int, asset: DocumentCheckListDetailModel)
    }


    fun getItemList(): ArrayList<DocumentCheckListDetailModel> {
        return documentCheck
    }

    fun addItem(position: Int = 0, checkListDetail: DocumentCheckListDetailModel) {
        documentCheck?.let {
            it.add(position, checkListDetail)
            notifyDataSetChanged()
        }
    }

    fun updateItem(position: Int, assetDetail: DocumentCheckListDetailModel) {
        documentCheck?.let {
            if (position >= 0 && position <= it.size) {
                it[position] = assetDetail
                notifyDataSetChanged()
            }
        }
    }



    override fun onBindViewHolder(holder: CheckListDetailViewHolder, position: Int) {
        holder.bindItems(position, documentCheck[position])
    }

    inner class CheckListDetailViewHolder(val binding: ItemDocumentChecklistBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int, documentCheck: DocumentCheckListDetailModel) {
          //  binding.tvValue.text = documentCheck.assetValue.toString()
            addClickListener(position, documentCheck)
        }

        private fun addClickListener(position: Int, documentCheck: DocumentCheckListDetailModel) {

        }
    }
}
