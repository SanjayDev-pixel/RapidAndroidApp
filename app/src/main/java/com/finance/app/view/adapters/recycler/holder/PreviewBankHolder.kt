package com.finance.app.view.adapters.recycler.holder

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.PreviewLayoutBankBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.BankDetailBean
import com.finance.app.persistence.model.BankDetailModel
import com.finance.app.view.adapters.recycler.adapter.PreviewInnerAdapter
import com.finance.app.viewModel.AppDataViewModel

class PreviewBankHolder(val binding: PreviewLayoutBankBinding, val mContext: FragmentActivity)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindItems(list: ArrayList<BankDetailModel>?, pos: Int, viewModel: AppDataViewModel) {

        if (!list.isNullOrEmpty()) {
            setUpInnerRecyclerView(list[pos].applicantBankDetailsBean, viewModel)
            handleCollapse()
        }
    }

    private fun setUpInnerRecyclerView(data: ArrayList<BankDetailBean>, viewModel: AppDataViewModel) {
        val previewInnerAdapter = PreviewInnerAdapter(mContext,
                dataList = data, viewModel = viewModel, previewTypeEnums = AppEnums.PreviewType.BANK)
        binding.rcBank.adapter = previewInnerAdapter

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