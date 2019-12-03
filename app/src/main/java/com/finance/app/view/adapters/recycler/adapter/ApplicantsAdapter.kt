package com.finance.app.view.adapters.recycler.adapter

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
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

    private lateinit var binding: ItemApplicantBinding
    private var mClickListener: ItemClickListener? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    private var selectedPosition = 0

    fun setOnItemClickListener(listener: ItemClickListener) {
        mClickListener = listener
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

    inner class ApplicantsViewHolder(val binding: ItemApplicantBinding,
                                     val mContext: Context) : RecyclerView.ViewHolder(binding.root), View.OnLongClickListener {

        init {
            itemView.setOnLongClickListener(this)
        }

        fun bindItems(position: Int, coApplicant: Response.CoApplicantsObj) {
            binding.tvApplicants.text = coApplicant.firstName
            binding.tvApplicants.setOnClickListener {
                if (mClickListener != null) {
                    selectedPosition = adapterPosition
                    mClickListener!!.onApplicantClick(position, coApplicant)
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

        override fun onLongClick(v: View?): Boolean {
            if (adapterPosition != 0) {
                showAlertDialog()
            }
            return true
        }

        private fun showAlertDialog() {
            val deleteDialogView = LayoutInflater.from(mContext).inflate(R.layout.delete_dialog, null)
            val progressDialog = ProgressDialog(mContext)
            val mBuilder = AlertDialog.Builder(mContext)
                    .setView(deleteDialogView)
                    .setTitle("Delete Applicant")
            val deleteDialog = mBuilder.show()

            deleteDialogView.tvDeleteConfirm.setOnClickListener {
                progressDialog.setMessage("Deleting Applicant")
                progressDialog.show()
                Handler().postDelayed({
                    deleteApplicant()
                    deleteDialog.dismiss()
                    progressDialog.dismiss()
                }, 1000)
            }

            deleteDialogView.tvDonotDelete.setOnClickListener {
                deleteDialog.dismiss()
            }
        }

        private fun deleteApplicant() {
            applicants.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            notifyItemRangeChanged(adapterPosition, applicants.size)
            selectedPosition = adapterPosition - 1
        }
    }
}
