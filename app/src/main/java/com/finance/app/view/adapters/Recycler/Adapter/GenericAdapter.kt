package com.finance.app.view.adapters.Recycler.Adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.view.adapters.Recycler.Adapter.GenericAdapter.Companion.MyHolder

/**
 * Created by abul on 14/4/18.
 */
abstract class GenericAdapter<T : ViewDataBinding, L>(
    private val mContext: Context, private val mList: ArrayList<L>,
    private val mLayout: Int)//        SocietyApplication.getInstance().getAppComponent().inject(this);
    : androidx.recyclerview.widget.RecyclerView.Adapter<MyHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder<T> {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = DataBindingUtil.inflate<T>(inflater, mLayout, parent, false)
        return MyHolder(
            binding.root, binding)
    }

    override fun onBindViewHolder(holder: MyHolder<T>, position: Int) {
        customFun(holder.binding as T, position, mList)
    }

    abstract fun customFun(binding: T, position: Int, mList: List<L>)

    override fun getItemCount(): Int {
        return mList.size
    }

    companion object {

        class MyHolder<T : ViewDataBinding>(itemView: View,
            var binding: T) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
    }

    @FunctionalInterface
    interface AdapterInterface<T : ViewDataBinding, L> {
        fun customFun(binding: T, position: Int, mList: List<L>)
    }
}
