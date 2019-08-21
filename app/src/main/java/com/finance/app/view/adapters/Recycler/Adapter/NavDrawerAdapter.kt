package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.DrawerItemBinding

class NavDrawerAdapter(private val c: Context) : RecyclerView.Adapter<NavDrawerAdapter.NavDrawerViewHolder>() {
    private lateinit var binding: DrawerItemBinding
    private val itemName = arrayOf("Drawable", "Assigned Lead", "Notifications")
    private val itemIcon = arrayOf(R.drawable.notification_bell, R.drawable.notification_bell,
            R.drawable.notification_bell)

    override fun onBindViewHolder(holder: NavDrawerViewHolder, position: Int) {
        holder.bindItems(itemName[position], itemIcon[position])
    }

    override fun getItemCount() = itemName.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDrawerViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.drawer_item, parent, false)
        return NavDrawerViewHolder(binding, c)
    }

    inner class NavDrawerViewHolder(val binding: DrawerItemBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(name: String, icon: Int) {
            binding.tvItemName.text = name
            binding.ivItemIcon.setImageResource(icon)
        }
    }
}
