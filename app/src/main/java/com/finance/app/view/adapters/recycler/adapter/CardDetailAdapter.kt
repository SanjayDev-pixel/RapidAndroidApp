package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemCreditCardBinding
import com.finance.app.persistence.model.AssetLiability
import com.finance.app.persistence.model.CardDetail

class CardDetailAdapter(private val c: Context, private val cards: ArrayList<CardDetail>) : RecyclerView.Adapter<CardDetailAdapter.CreditCardViewHolder>() {
    private lateinit var binding: ItemCreditCardBinding
    private var mOnCardClickListener: CardClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_credit_card, parent, false)
        return CreditCardViewHolder(binding, c)
    }

    override fun getItemCount() = cards.size

    fun setOnCardClickListener(listener: CardClickListener) {
        mOnCardClickListener = listener
    }

    interface CardClickListener {
        fun onCardDetailDeleteClicked(position: Int)
        fun onCardDetailEditClicked(position: Int, card: CardDetail)
    }

    fun getItemList(): ArrayList<CardDetail> {
        return cards
    }

    fun addItem(position: Int = 0, cardDetail: CardDetail) {
        cards?.let {
            it.add(position, cardDetail)
            notifyDataSetChanged()
        }
    }

    fun updateItem(position: Int, cardDetail: CardDetail) {
        cards?.let {
            if (position >= 0 && position <= it.size) {
                it[position] = cardDetail
                notifyDataSetChanged()
            }
        }
    }

    fun deleteItem(position: Int) {
        cards?.let {
            cards.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bindItems(position, cards[position])
    }

    inner class CreditCardViewHolder(val binding: ItemCreditCardBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int, card: CardDetail) {
            binding.tvCardLimit.text = card.cardLimit.toString()
            binding.tvCurrentUtilization.text = card.currentUtilization.toString()
            binding.tvLastPaymentDate.text = card.lastPaymentDate.toString()
            addClickListener(position, card)
        }

        private fun addClickListener(position: Int, card: CardDetail) {
            binding.btnDelete.setOnClickListener {
                mOnCardClickListener!!.onCardDetailDeleteClicked(position)
            }

            binding.btnEdit.setOnClickListener {
                mOnCardClickListener!!.onCardDetailEditClicked(position, card)
            }
        }
    }
}
