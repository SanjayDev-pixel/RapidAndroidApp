package com.finance.app.view.adapters.recycler.Spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.finance.app.R
import motobeans.architecture.retrofit.response.Response

class UserBranchesSpinnerAdapter(mContext: Context, val value: List<Response.UserBranches>) :
        BaseAdapter() {

    private var inflater: LayoutInflater = mContext.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): Any? {
        return value[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return value.size
    }

    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val spinnerValue = value[position]

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_custom_spinner, parent, false)
            val textView: TextView = view.findViewById(R.id.dropdown)
            textView.text = spinnerValue.branchName
        } else {
            view = convertView
        }
        return view
    }

    override
    fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }
}