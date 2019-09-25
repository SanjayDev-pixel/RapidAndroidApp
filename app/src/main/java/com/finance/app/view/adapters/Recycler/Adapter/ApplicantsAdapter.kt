package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemApplicantsBinding

class ApplicantsAdapter(private val mContext: Context, private val applicants: ArrayList<String>) : RecyclerView.Adapter<ApplicantsAdapter.CreditCardViewHolder>() {
    private lateinit var binding: ItemApplicantsBinding
    private var mClickListener: ItemClickListener? = null

    companion object {
        private var selectedPosition = 0
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_applicants, parent, false)
        return CreditCardViewHolder(binding, mContext)
    }

    override fun getItemCount() = applicants.size

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bindItems(position)
    }

    interface ItemClickListener {
        fun onApplicantClick(position: Int)
    }

    inner class CreditCardViewHolder(val binding: ItemApplicantsBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
            binding.tvApplicants.text = applicants[position]
            binding.tvApplicants.setOnClickListener {
                if (mClickListener != null) {
                    selectedPosition = adapterPosition
                    mClickListener!!.onApplicantClick(position)
                    notifyDataSetChanged()
                }
            }

            if (selectedPosition == adapterPosition) {
                binding.tvApplicants.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                binding.tvApplicants.setBackgroundResource(R.drawable.button_theme_style)
            } else {
                binding.tvApplicants.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                binding.tvApplicants.setBackgroundResource(R.drawable.rectangular_curved_bg_blue)
            }
        }
    }
}
