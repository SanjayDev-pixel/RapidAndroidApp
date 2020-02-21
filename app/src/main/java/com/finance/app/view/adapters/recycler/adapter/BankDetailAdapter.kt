package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemBankBinding
import com.finance.app.persistence.model.BankDetailBean

class BankDetailAdapter(private val context: Context, private val bankDetailList: ArrayList<BankDetailBean>) : RecyclerView.Adapter<BankDetailAdapter.BankDetailViewHolder>() {
    private lateinit var binding: ItemBankBinding
    private var mOnItemClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onBankDetailDeleteClicked(position: Int)
        fun onBankDetailEditClicked(position: Int, bank: BankDetailBean)
    }

    inner class BankDetailViewHolder(val binding: ItemBankBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int, bank: BankDetailBean) {
            binding.tvBankName.text = bank.bankName.toString()
            binding.tvAccountNum.text = bank.accountNumber.toString()
            binding.tvAccountHolder.text = bank.accountHolderName.toString()
            binding.tvAccountType.text = bank.accountTypeName.toString()
            binding.tvSalaryCreditedNum.text = bank.numberOfCredit.toString()
            addClickListener(position, bank)
        }

        private fun addClickListener(position: Int, bank: BankDetailBean) {
            binding.ivDelete.setOnClickListener {
                mOnItemClickListener?.onBankDetailDeleteClicked(position)
            }

            binding.ivEdit.setOnClickListener {
                mOnItemClickListener?.onBankDetailEditClicked(position, bank)
            }
        }
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun getItemCount() = bankDetailList.size

    fun getItemList(): ArrayList<BankDetailBean> {
        return bankDetailList
    }


    fun addItem(bankDetail: BankDetailBean) {
        bankDetailList.add(0, bankDetail)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, bankDetail: BankDetailBean) {
        if (position >= 0 && position <= bankDetailList.size) {
            bankDetailList[position] = bankDetail
            notifyDataSetChanged()
        }
    }

    fun deleteItem(position: Int) {
        if (position >= 0 && position <= bankDetailList.size) {
            bankDetailList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_bank, parent, false)
        return BankDetailViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: BankDetailViewHolder, position: Int) {
        if (!bankDetailList.isNullOrEmpty()) {
            holder.bindItems(position, bankDetailList[position])
        }
    }
}
