package com.finance.app.view.adapters.Recycler.Holder

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.finance.app.databinding.ItemCommonTempBinding
import motobeans.architecture.application.ArchitectureApp


/**
 * Created by motobeans on 1/3/2018.
 */
class TempHolder(val context: Context, val binding: ItemCommonTempBinding) : RecyclerView.ViewHolder(binding.root) {

  init {
    ArchitectureApp.instance.component.inject(this)
  }

  fun handleCard(position: Int, item: String) {
  }
}