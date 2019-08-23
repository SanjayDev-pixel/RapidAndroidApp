package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.LeadsCardViewBinding
import com.finance.app.view.activity.LeadDetailActivity
import java.util.*

class LeadListingAdapter(private val c: Context) : RecyclerView.Adapter<LeadListingAdapter.LeadManagementViewHolder>() {
    private lateinit var binding: LeadsCardViewBinding

    private val name = arrayOf("ChamanLal", "Sanat bhagel", " bhagel", "Sanat ")
    private val timeStamp = arrayOf(6541313L, 54684981L, 3216546L, 6461313L)

    override fun onBindViewHolder(holder: LeadManagementViewHolder, position: Int) {
        holder.bindItems(name, position, timeStamp)
    }

    override fun getItemCount() = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadManagementViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.leads_card_view, parent, false)
        return LeadManagementViewHolder(binding, c)
    }

    inner class LeadManagementViewHolder(val binding: LeadsCardViewBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(name: Array<String>, position: Int, timeStamp: Array<Long>) {
            binding.tvLeadName.text = name[position]
            convertToHumanFormat(timeStamp)
            binding.leadCard.setOnClickListener {
                LeadDetailActivity.start(c)
            }
        }

        private fun convertToHumanFormat(timeStamp: Array<Long>) {

            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp[position] * 1000L
            val date = DateFormat.format("dd MMM : hh:mm", cal).toString()
            binding.tvDateAndTime.text = date

        }
    }
}
