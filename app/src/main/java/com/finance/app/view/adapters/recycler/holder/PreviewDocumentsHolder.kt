package com.finance.app.view.adapters.recycler.holder

import android.content.Context

import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.PreviewLayoutDocumentChecklistBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.BankDetailBean

import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.PreviewInnerAdapter

import com.finance.app.viewModel.AppDataViewModel



class PreviewDocumentsHolder(val binding: PreviewLayoutDocumentChecklistBinding, val mContext: Context)
    : RecyclerView.ViewHolder(binding.root) {

    private val presenter = Presenter()



    fun bindItems(list: ArrayList<PersonalApplicantsModel>?, pos: Int, viewModel: AppDataViewModel) {
        if (!list.isNullOrEmpty()) {
           // setUpInnerRecyclerView(list[pos].do, viewModel)
           // handleCollapse()
            binding.tvCoApplicant.setText(LeadMetaData.getLeadData()?.personalData!!.applicantDetails[pos].firstName)
        }
    }

   /* private fun setUpInnerRecyclerView(data: ArrayList<BankDetailBean>, viewModel: AppDataViewModel) {
        val previewInnerAdapter = PreviewInnerAdapter(mContext,
                dataList = data, viewModel = viewModel, previewTypeEnums = AppEnums.PreviewType.BANK)
        binding.rcBank.adapter = previewInnerAdapter

    }*/






}

