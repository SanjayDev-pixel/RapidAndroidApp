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
    private var mOnCheckClickListener: CheckListClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckListDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_document_checklist, parent, false)
        return CheckListDetailViewHolder(binding, c)
    }

    override fun getItemCount() = documentCheck.size

    fun setOnCheckListClickListener(listner: CheckListClickListener) {
        mOnCheckClickListener = listner
    }

    interface CheckListClickListener {

    }


    fun getItemList(): ArrayList<DocumentCheckListDetailModel> {
        return documentCheck
    }

//    fun addItem(position: Int = 0, checkListDetail: DocumentCheckListDetailModel) {
//        documentCheck?.let {
//            it.add(position, checkListDetail)
//            notifyDataSetChanged()
//        }
//    }





    override fun onBindViewHolder(holder: CheckListDetailViewHolder, position: Int) {
        holder.bindItems(position, documentCheck[position])
    }

    inner class CheckListDetailViewHolder(val binding: ItemDocumentChecklistBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int, documentCheck: DocumentCheckListDetailModel) {
          //  binding.tvValue.text = documentCheck.assetValue.toString()
            binding.questionNo.text=(position+1).toString()
          //  binding.questiontext.text=documentCheck.


            addClickListener(position, documentCheck)
        }

        private fun addClickListener(position: Int, documentCheck: DocumentCheckListDetailModel) {

        }
    }
}
