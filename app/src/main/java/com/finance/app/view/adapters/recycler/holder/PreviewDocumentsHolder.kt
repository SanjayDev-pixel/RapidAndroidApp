package com.finance.app.view.adapters.recycler.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.PreviewLayoutDocumentChecklistBinding
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.viewModel.AppDataViewModel

class PreviewDocumentsHolder(val binding: PreviewLayoutDocumentChecklistBinding, val mContext: Context)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindItems(list: ArrayList<PersonalApplicantsModel>?, pos: Int, viewModel: AppDataViewModel) {

        if (!list.isNullOrEmpty()) {
            setValueInCard(list[pos])
        }
    }

    private fun setValueInCard(data: PersonalApplicantsModel) {
    }

}