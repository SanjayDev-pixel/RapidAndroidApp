package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.ItemLeadBinding
import com.finance.app.view.activity.LeadDetailActivity
import java.util.*

class LeadListingAdapter(private val c: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<LeadListingAdapter.LeadManagementViewHolder>() {
    private lateinit var binding: ItemLeadBinding

    private val name = arrayOf("ChamanLal", "Sanat bhagel", " bhagel", "Sanat ")
    private val timeStamp = arrayOf(6541313L, 54684981L, 3216546L, 6461313L)

    override fun onBindViewHolder(holder: LeadManagementViewHolder, position: Int) {
        holder.bindItems(name, position, timeStamp)
    }

    override fun getItemCount() = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadManagementViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lead, parent, false)
        return LeadManagementViewHolder(binding, c)
    }

    inner class LeadManagementViewHolder(val binding: ItemLeadBinding, val c: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
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