package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemKycBinding
import com.finance.app.model.Modals

class AddKycAdapter(private val c: Context, private val kycList: ArrayList<Modals.AddKyc>) : RecyclerView.Adapter<AddKycAdapter.KYCViewHolder>() {
    private lateinit var binding: ItemKycBinding

    override fun onBindViewHolder(holder: KYCViewHolder, position: Int) {
        holder.bindItems(kycList[position])
    }

    override fun getItemCount(): Int = kycList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KYCViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_kyc, parent, false)
        return KYCViewHolder(binding, c)
    }

    inner class KYCViewHolder(val binding: ItemKycBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(kyc: Modals.AddKyc) {
            binding.tvIdNum.text = kyc.idNum
            binding.tvIdType.text = kyc.idType
            binding.tvVerifiedStatus.text = kyc.verifiedStatus
            binding.ivKyc.setImageBitmap(kyc.kycImage)
            binding.ivDelete.setOnClickListener {
                deleteKyc()
            }
        }

        private fun deleteKyc() {
            kycList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            notifyItemRangeChanged(adapterPosition, kycList.size)
            Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show()
        }
    }
}
