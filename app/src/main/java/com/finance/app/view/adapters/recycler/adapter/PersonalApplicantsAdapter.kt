package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemApplicantBinding
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class PersonalApplicantsAdapter(private val mContext: Context, private val applicants: ArrayList<String>) : RecyclerView.Adapter<PersonalApplicantsAdapter.CreditCardViewHolder>() {
    private lateinit var binding: ItemApplicantBinding
    private var mClickListener: ItemClickListener? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    companion object {
        private var selectedPosition = 0
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_applicant, parent, false)
        return CreditCardViewHolder(binding, mContext)
    }

    override fun getItemCount() = applicants.size

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bindItems(position)
    }

    interface ItemClickListener {
        fun onApplicantClick(position: Int)
    }

    inner class CreditCardViewHolder(val binding: ItemApplicantBinding, val mContext: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
            binding.tvApplicants.text = applicants[position]
            binding.tvApplicants.setOnClickListener {
                if (mClickListener != null) {
                    selectedPosition = adapterPosition
                    mClickListener!!.onApplicantClick(position)
//                    sharedPreferences.setCoApplicantsPosition(position.toString())
                    notifyDataSetChanged()
                }
            }

            if (selectedPosition == adapterPosition) {
                binding.tvApplicants.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                binding.tvApplicants.setBackgroundResource(R.drawable.selected_applicant_tab)
            } else {
                binding.tvApplicants.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                binding.tvApplicants.setBackgroundResource(R.drawable.unselected_applicant_tab)
            }
        }
    }
}
