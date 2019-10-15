package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemApplicantBinding

class CoApplicantsAdapter(private val mContext: Context, private val applicants: ArrayList<String>, private val selectedPosition: Int) : RecyclerView.Adapter<CoApplicantsAdapter.CreditCardViewHolder>() {
    private lateinit var binding: ItemApplicantBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_applicant, parent, false)
        return CreditCardViewHolder(binding, mContext)
    }

    override fun getItemCount() = applicants.size

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bindItems(position)
    }

    inner class CreditCardViewHolder(val binding: ItemApplicantBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
            binding.tvApplicants.text = applicants[position]

            if (adapterPosition == selectedPosition) {
                binding.tvApplicants.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                binding.tvApplicants.setBackgroundResource(R.drawable.selected_applicant_tab)
            } else {
                binding.tvApplicants.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                binding.tvApplicants.setBackgroundResource(R.drawable.unselected_applicant_tab)
            }
        }
    }
}
