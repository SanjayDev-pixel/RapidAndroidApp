package com.finance.app.utility

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class SelectDate(field: TextView, mContext: Context) {

    init {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            field.setText("$day/ ${month + 1}/ $year")
        }, year, month, day)
        dialog.show()
    }
}