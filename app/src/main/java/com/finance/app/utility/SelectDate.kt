package com.finance.app.utility

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class SelectDate(field: TextView, mContext: Context, setMax: Boolean = true) {
    init {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { _, dYear, dMonth, dDay ->
            val date = "$dDay-${dMonth + 1}-$dYear"
            convertToDesirableFormat(date, field)
        }, year, month, day)

        if (setMax) dialog.datePicker.maxDate = Date().time
        dialog.show()
    }

    private fun convertToDesirableFormat(mDate: String, field: TextView) {
        val desirablePattern = "dd-MMM-yyyy"
        val pattern = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(pattern, Locale.US)
        val date = sdf.parse(mDate)
        val desiredSdf = SimpleDateFormat(desirablePattern, Locale.US)
        val dateToShow = desiredSdf.format(date)
        field.text = dateToShow
    }
}