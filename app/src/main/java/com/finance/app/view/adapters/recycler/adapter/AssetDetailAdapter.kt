package com.finance.app.view.adapters.recycler.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.ItemAssetBinding
import com.finance.app.persistence.model.AssetLiability

class AssetDetailAdapter(private val c: Context, private val assets: ArrayList<AssetLiability>) : RecyclerView.Adapter<AssetDetailAdapter.AssetDetailViewHolder>() {
    private lateinit var binding: ItemAssetBinding
    private var mOnAssetClickListener: AssetClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_asset, parent, false)
        return AssetDetailViewHolder(binding, c)
    }

    override fun getItemCount() = assets.size

fun  setOnAssetClickListener(listner:AssetClickListener)
  {
    mOnAssetClickListener=listner
  }

    interface AssetClickListener {
        fun onAssetDeleteClicked(position: Int)
        fun onAssetEditClicked(position: Int, asset: AssetLiability)
    }

    override fun onBindViewHolder(holder: AssetDetailViewHolder, position: Int) {
        holder.bindItems(position, assets[position])
    }

    inner class AssetDetailViewHolder(val binding: ItemAssetBinding, val c: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(position: Int, asset: AssetLiability) {
            binding.tvValue.text = asset.assetValue.toString()
            addClickListener(position, asset)
        }

        private fun addClickListener(position: Int, asset: AssetLiability) {
            binding.btnDelete.setOnClickListener {
                mOnAssetClickListener!!.onAssetDeleteClicked(position)

            }

            binding.btnEdit.setOnClickListener {
                mOnAssetClickListener!!.onAssetEditClicked(position, asset)


            }
        }
    }
}
