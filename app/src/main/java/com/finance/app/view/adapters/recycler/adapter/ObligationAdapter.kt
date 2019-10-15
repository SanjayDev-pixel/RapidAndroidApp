package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemObligationBinding

class ObligationAdapter(private val c: Context) : RecyclerView.Adapter<ObligationAdapter.ObligationViewHolder>() {
    private lateinit var binding: ItemObligationBinding

    override fun onBindViewHolder(holder: ObligationViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount() = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObligationViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_obligation, parent, false)
        return ObligationViewHolder(binding, c)
    }

    inner class ObligationViewHolder(val binding: ItemObligationBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
        }
    }
}
