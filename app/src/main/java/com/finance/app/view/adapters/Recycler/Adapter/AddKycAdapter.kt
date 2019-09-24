package com.finance.app.view.adapters.Recycler.Adapter

import android.app.ProgressDialog
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemKycBinding
import com.finance.app.model.Modals
import kotlinx.android.synthetic.main.delete_dialog.view.*

class AddKycAdapter(private val mActivity: FragmentActivity, private val kycList: ArrayList<Modals.AddKyc>) : RecyclerView.Adapter<AddKycAdapter.KYCViewHolder>() {
    private lateinit var binding: ItemKycBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KYCViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_kyc, parent, false)
        return KYCViewHolder(binding)
    }

    override fun getItemCount(): Int = kycList.size

    override fun onBindViewHolder(holder: KYCViewHolder, position: Int) {
        holder.bindItems(kycList[position])
    }

    inner class KYCViewHolder(val binding: ItemKycBinding) : RecyclerView.ViewHolder(binding.root),
            View.OnLongClickListener {

        init {
            itemView.setOnLongClickListener(this)
        }

        fun bindItems(kyc: Modals.AddKyc) {
            binding.tvIdNum.text = kyc.idNum
            binding.tvIdType.text = kyc.idType
            binding.tvVerifiedStatus.text = kyc.verifiedStatus
            binding.ivKyc.setImageBitmap(kyc.kycImage)
        }

        override fun onLongClick(v: View?): Boolean {
            showAlertDialog()
            return true
        }

        private fun showAlertDialog() {
            val deleteDialogView = LayoutInflater.from(mActivity).inflate(R.layout.delete_dialog, null)
            val progressDialog = ProgressDialog(mActivity)
            val mBuilder = AlertDialog.Builder(mActivity)
                    .setView(deleteDialogView)
                    .setTitle("Delete Kyc details")
            val deleteDialog = mBuilder.show()

            deleteDialogView.tvDeleteConfirm.setOnClickListener {
                progressDialog.setMessage("Deleting Kyc details")
                progressDialog.show()
                Handler().postDelayed({
                    deleteKyc()
                    deleteDialog.dismiss()
                    progressDialog.dismiss()
                }, 1000)
            }

            deleteDialogView.tvDonotDelete.setOnClickListener {
                deleteDialog.dismiss()
            }
        }

        private fun deleteKyc() {
            kycList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            notifyItemRangeChanged(adapterPosition, kycList.size)
            Toast.makeText(mActivity, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
