package com.finance.app.view.adapters.Recycler.Holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.databinding.EmptyCommonListBinding

/**
 * Created by motobeans on 1/3/2018.
 */
class EmptyCommonListHolder(val context: Context,
    val binding: EmptyCommonListBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    fun handleCard(position: Int, emptyText: String = "No Result Found") {
        binding.tvEmptyText.text = emptyText
    }
}