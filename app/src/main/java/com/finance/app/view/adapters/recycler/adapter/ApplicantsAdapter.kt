package com.finance.app.view.adapters.recycler.adapter

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemApplicantBinding
import kotlinx.android.synthetic.main.delete_dialog.view.*
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class ApplicantsAdapter(private val mContext: Context, private val applicants: ArrayList<Response.CoApplicantsObj>)
    : RecyclerView.Adapter<ApplicantsAdapter.ApplicantsViewHolder>() {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: ItemApplicantBinding
    private var mClickListener: ItemClickListener? = null
    private var mLongClickListener: ItemLongClickListener? = null
    private var selectedPosition = 0

    fun setOnItemClickListener(listener: ItemClickListener) {
        mClickListener = listener
    }

    fun setOnLongClickListener(longListener: ItemLongClickListener) {
        mLongClickListener = longListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantsViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_applicant, parent, false)
        return ApplicantsViewHolder(binding, mContext)
    }

    override fun getItemCount() = applicants.size

    override fun onBindViewHolder(holder: ApplicantsViewHolder, position: Int) {
        holder.bindItems(position, applicants[position])
    }

    interface ItemClickListener {
        fun onApplicantClick(position: Int, coApplicant: Response.CoApplicantsObj)
    }

    interface ItemLongClickListener {
        fun onApplicantLongClick(position: Int)
    }

    inner class ApplicantsViewHolder(val binding: ItemApplicantBinding,
                                     val mContext: Context) : RecyclerView.ViewHolder(binding.root) {

        fun bindItems(position: Int, coApplicant: Response.CoApplicantsObj) {
            binding.tvApplicants.text = coApplicant.firstName
            binding.tvApplicants.setOnClickListener {
                if (mClickListener != null) {
                    selectedPosition = adapterPosition
                    mClickListener!!.onApplicantClick(position, coApplicant)
                }
            }

            itemView.setOnLongClickListener {
                if (mLongClickListener != null) {
                    selectedPosition = adapterPosition
                    mLongClickListener!!.onApplicantLongClick(position)
                    selectedPosition = adapterPosition - 1
                }
                false
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
