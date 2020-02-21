package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemKycBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.KYCDetail
import com.finance.app.utility.ConvertDate

class KycListAdapter(private val context: Context, private val kycList: ArrayList<KYCDetail>) : RecyclerView.Adapter<KycListAdapter.KycViewHolder>() {

    private var listSize: MutableLiveData<Int>? = MutableLiveData()

    inner class KycViewHolder(val binding: ItemKycBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(kyc: KYCDetail) {
            binding.tvIdType.text = kyc.identificationTypeDetailID.toString()
            binding.tvIdNum.text = kyc.identificationNumber
            binding.tvVerifiedStatus.text = kyc.verifiedStatusTypeDetailID.toString()
            binding.tvIssueDate.text = ConvertDate().convertToAppFormat(kyc.issueDate)
            binding.tvExpiryDate.text = ConvertDate().convertToAppFormat(kyc.expireDate)
        }

    }

    fun addItem(kycDetail: KYCDetail) {
        kycList.add(0, kycDetail)
        notifyDataSetChanged()
        listSize?.postValue(itemCount)
    }

    fun updateItem(position: Int, kycDetail: KYCDetail) {
        if (position >= 0 && position <= kycList.size) {
            kycList[position] = kycDetail
            notifyDataSetChanged()
            listSize?.postValue(itemCount)
        }
    }

    fun deleteItem(position: Int) {
        if (position >= 0 && position <= kycList.size) {
            kycList.removeAt(position)
            notifyDataSetChanged()
            listSize?.postValue(itemCount)
        }
    }

    fun updateList(kycDetailList: ArrayList<KYCDetail>) {
        kycList.clear()
        kycList.addAll(kycDetailList)
        notifyDataSetChanged()
        listSize?.postValue(itemCount)
    }

    fun getItemList() = kycList

    fun getItemCountObserver() = listSize

    override fun getItemCount(): Int = kycList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KycViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return KycViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_kyc, parent, false))
    }

    override fun onBindViewHolder(holder: KycViewHolder, position: Int) {
        holder.bindItems(kycList[position])
    }

}
