package com.finance.app.view.adapters.recycler.holder

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.PreviewLayoutDocumentChecklistBinding
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.view.activity.FinalSubmitActivity
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.constants.ConstantsApi

class PreviewDocumentsHolder(val binding: PreviewLayoutDocumentChecklistBinding, val mContext: Context)
    : RecyclerView.ViewHolder(binding.root) {

    private val presenter = Presenter()

    fun bindItems(list: ArrayList<PersonalApplicantsModel>?, pos: Int, viewModel: AppDataViewModel) {

        if (!list.isNullOrEmpty()) {
            setValueInCard(list[pos])
        }
    }

    private fun setValueInCard(data: PersonalApplicantsModel) {

        binding.btnSumbitlead.setOnClickListener(){


            val intent = Intent(mContext, FinalSubmitActivity::class.java)
            startActivity(mContext,intent,null)






        }




    }

}