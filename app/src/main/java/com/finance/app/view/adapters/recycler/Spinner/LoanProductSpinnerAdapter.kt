package com.finance.app.view.adapters.recycler.Spinner

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import com.finance.app.R
import com.finance.app.persistence.model.LoanProductMaster
import motobeans.architecture.retrofit.response.Response

class LoanProductSpinnerAdapter(val mContext: Context, val value: ArrayList<LoanProductMaster>) : BaseAdapter() {

    init {

        val userSelectTextBranch = LoanProductMaster()
        userSelectTextBranch.productID = -1
        userSelectTextBranch.productName = "Select Loan"

        value.add(0, userSelectTextBranch)
    }

    private var inflater: LayoutInflater = mContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var spinnerValue: LoanProductMaster

    override fun getCount() = value.size
    override fun getItem(position: Int) = value[position]
    override fun getItemId(position: Int): Long {
        return 0
    }
    
    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        spinnerValue = value[position]

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_custom_spinner, parent, false)
            val textView = view.findViewById<View>(R.id.dropdown) as TextView
            textView.text = spinnerValue.productName
        } else {
            view = convertView
        }

        view.tag = spinnerValue

        return view
    }

    override
    fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

}