package com.finance.app.view.adapters.Recycler.Holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.ErrorCommonListBinding

/**
 * Created by motobeans on 1/3/2018.
 */
class ErrorCommonListHolder(val context: Context,
    val binding: ErrorCommonListBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    private var mandatoryText = "Mandatory"

    fun handleCard(position: Int, emptyText: String = mandatoryText) {
        binding.tvErrorText.text = emptyText
    }
}