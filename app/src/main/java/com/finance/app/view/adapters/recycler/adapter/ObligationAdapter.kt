package com.finance.app.view.adapters.recycler.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemObligationBinding
import com.finance.app.persistence.model.ObligationDetail
import kotlinx.android.synthetic.main.asset_creditcard_dialog.*
import kotlinx.android.synthetic.main.obligation_item_dialog.*

class ObligationAdapter(private val c: Context, private val obligations: ArrayList<ObligationDetail>) : RecyclerView.Adapter<ObligationAdapter.ObligationViewHolder>() {
    private lateinit var binding: ItemObligationBinding
    private var mOnObligationClickListener: ObligationClickListener? = null
    private lateinit var obligationItemDetailDialogView: View
    private lateinit var obligationItemDetailDialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObligationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_obligation, parent, false)
        return ObligationViewHolder(binding, c)
    }

    override fun getItemCount() = obligations.size

    fun setOnObligationClickListener(listener: ObligationClickListener) {
        mOnObligationClickListener = listener
    }

    interface ObligationClickListener {
        fun onObligationDeleteClicked(position: Int)
        fun onObligationEditClicked(position: Int, obligation: ObligationDetail)
    }

    override fun onBindViewHolder(holder: ObligationViewHolder, position: Int) {
        holder.bindItems(position, obligations[position])
    }


    inner class ObligationViewHolder(val binding: ItemObligationBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int, obligation: ObligationDetail) {
            binding.tvFinancerName.text = obligation.financerName.toString()
            binding.tvLoanAmount.text = obligation.loanAmount.toString()
            binding.tvLoanAcNum.text = obligation.loanAccountNumber.toString()
            binding.tvTenure.text = obligation.tenure.toString()
            binding.tvBalanceTenure.text = obligation.balanceTenure.toString()
            binding.tvNumOfBouncesInSixMonths.text = obligation.numberOfBouncesInLastSixMonth.toString()
            binding.tvNumOfBouncesInNineMonths.text = obligation.numberOfBouncesInLastNineMonth.toString()
            binding.tvLoanAcNum.text=obligation.loanAmount.toString()
            addClickListener(position, obligation)
        }

        private fun addClickListener(position: Int, obligation: ObligationDetail) {
            binding.btnDelete.setOnClickListener {
                mOnObligationClickListener!!.onObligationDeleteClicked(position)
            }

            binding.btnEdit.setOnClickListener {
              //  mOnObligationClickListener!!.onObligationEditClicked(position, obligation)
                showItemDetail(position,obligation)
            }


        }
    }

    private fun showItemDetail(position: Int, obligation: ObligationDetail) {

        Log.e("Tag","ssss"+obligation.toString())
        obligationItemDetailDialogView = LayoutInflater.from(c).inflate(R.layout.obligation_item_dialog, null)
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(c)
                .setView(obligationItemDetailDialogView)
                .setCancelable(true)

        obligationItemDetailDialog = mBuilder.show()

       /* obligationItemDetailDialog.cancel_bttn.setOnClickListener(){

            obligationItemDetailDialog.dismiss()
        }*/


        obligationItemDetailDialog.tvFinancerName.setText(obligation.financerName)
        obligationItemDetailDialog.tvTenure.setText(obligation.tenure.toString())
        obligationItemDetailDialog.tvBalanceTenure.setText(obligation.balanceTenure.toString())
        obligationItemDetailDialog.tvEMI.setText(obligation.emiAmount.toString())
        obligationItemDetailDialog.tvNumOfBouncesInSixMonths.setText(obligation.numberOfBouncesInLastSixMonth.toString())
        obligationItemDetailDialog.tvNumOfBouncesInNineMonths.setText(obligation.numberOfBouncesInLastNineMonth.toString())
        obligationItemDetailDialog.tvLoanAcNum.setText(obligation.loanAccountNumber)
        obligationItemDetailDialog.tvEmiPaid.setText(obligation.bounseEmiPaidInSameMonth.toString())
        obligationItemDetailDialog.tvLoanAmount.setText(obligation.loanAmount.toString())



    }
}
