package com.finance.app.view.adapters.arrayadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.finance.app.R

/**
 * Created by munishkumarthakur on 25/01/18.
 */

/***** Adapter class extends with ArrayAdapter  */
class CustomSpinnerAdapter<T>(mContext: Context, private val resourceId: Int,
                              val data: ArrayList<T>) :
        ArrayAdapter<T>(mContext, resourceId, data) {

    private var inflater: LayoutInflater = mContext.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override
    fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    fun getCustomView(position: Int, parent: ViewGroup): View {

        val row = inflater.inflate(resourceId, parent, false)
        val tempValue = data[position]
        val tvTitle = row.findViewById(R.id.tvSpinnerValue) as TextView?

        // Set values for spinner each row
        row.tag = tempValue

        tvTitle?.text = tempValue.toString()

        return row
    }
}