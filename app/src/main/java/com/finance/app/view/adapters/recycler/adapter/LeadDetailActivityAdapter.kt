package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.LeadDetailActivitiesItemBinding
import java.util.*

class LeadDetailActivityAdapter(private val c: Context) : RecyclerView.Adapter<LeadDetailActivityAdapter.LeadDetailActivityHolder>() {
    private lateinit var binding: LeadDetailActivitiesItemBinding

    private val name = arrayOf("ChamanLal", "Sanat bhagel", " bhagel", "Sanat ")
    private val timeStamp = arrayOf(6541313L, 54684981L, 3216546L, 6461313L)

    override fun onBindViewHolder(holder: LeadDetailActivityHolder, position: Int) {
        holder.bindItems(name, position, timeStamp)
    }

    override fun getItemCount() = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadDetailActivityHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.lead_detail_activities_item,
                parent, false)
        return LeadDetailActivityHolder(binding, c)
    }

    inner class LeadDetailActivityHolder(val binding: LeadDetailActivitiesItemBinding,
                                         val c: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bindItems(name: Array<String>, position: Int, timeStamp: Array<Long>) {
            convertToHumanFormat(timeStamp)
        }

        private fun convertToHumanFormat(timeStamp: Array<Long>) {

            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timeStamp[position] * 1000L
            val date = DateFormat.format("dd MMM", cal).toString()
            binding.tvActivityDate.text = date
        }
    }
}
