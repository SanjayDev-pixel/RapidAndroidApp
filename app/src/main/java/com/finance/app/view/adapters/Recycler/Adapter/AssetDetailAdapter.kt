package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemAssetBinding
import com.finance.app.databinding.ItemNotificationBinding

class AssetDetailAdapter(private val c: Context) : RecyclerView.Adapter<AssetDetailAdapter.AssetDetailViewHolder>() {
    private lateinit var binding: ItemAssetBinding

    override fun onBindViewHolder(holder: AssetDetailViewHolder, position: Int) {
        holder.bindItems(position)
    }

    override fun getItemCount() = 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetDetailViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_asset, parent, false)
        return AssetDetailViewHolder(binding, c)
    }

    inner class AssetDetailViewHolder(val binding: ItemAssetBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int) {
        }
    }
}
