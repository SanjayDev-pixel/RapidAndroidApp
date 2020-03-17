package com.finance.app.view.adapters.recycler.holder

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.PreviewLayoutDocumentChecklistBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.PreviewInnerAdapter

import com.finance.app.viewModel.AppDataViewModel



class PreviewDocumentsHolder(val binding: PreviewLayoutDocumentChecklistBinding, val mContext: FragmentActivity)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindItems(list: ArrayList<DocumentCheckList>, pos: Int, viewModel: AppDataViewModel) {
        if (!list.isNullOrEmpty()) {
            setUpInnerRecyclerView(list[pos].checklistDetails, viewModel)
            handleCollapse()
            binding.tvCoApplicant.setText(LeadMetaData.getLeadData()?.personalData!!.applicantDetails[pos].firstName)
        }
    }

    private fun setUpInnerRecyclerView(data: ArrayList<DocumentCheckListDetailModel>, viewModel: AppDataViewModel) {
        val previewInnerAdapter =PreviewInnerAdapter(mContext,dataList= data,viewModel = viewModel,previewTypeEnums = AppEnums.PreviewType.DOCUMENT)
        binding.rcDocument.adapter = previewInnerAdapter
    }

    private fun handleCollapse() {
        binding.collapseForm.setOnClickListener {
            binding.cardApplicant.visibility = View.GONE
            binding.collapseForm.visibility = View.GONE
            binding.expandForm.visibility = View.VISIBLE
        }

        binding.expandForm.setOnClickListener {
            binding.cardApplicant.visibility = View.VISIBLE
            binding.collapseForm.visibility = View.VISIBLE
            binding.expandForm.visibility = View.GONE
        }
    }

}

