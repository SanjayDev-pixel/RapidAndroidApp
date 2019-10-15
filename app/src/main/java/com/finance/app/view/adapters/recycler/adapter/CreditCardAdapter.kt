package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemCreditCardBinding

class CreditCardAdapter(private val c: Context) : RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder>() {
    private lateinit var binding: ItemCreditCardBinding

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount() = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_credit_card, parent, false)
        return CreditCardViewHolder(binding, c)
    }

    inner class CreditCardViewHolder(val binding: ItemCreditCardBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
        }
    }
}
