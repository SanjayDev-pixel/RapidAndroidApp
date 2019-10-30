package com.finance.app.utility

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class AgeFromDOB(mContext: Context,  dateField: TextView, private val ageField: TextView) {
    init {
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val date = "$day-${month + 1}-$year"
            convertToDesirableFormat(date, dateField)
        }, year, month, day)
        dialog.show()
    }

    private fun convertToDesirableFormat(mDate: String, dateField: TextView) {
        val desirablePattern = "dd-MMM-yyyy"
        val pattern = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(pattern)
        val date = sdf.parse(mDate)
        val desiredSdf = SimpleDateFormat(desirablePattern)
        val dateToShow = desiredSdf.format(date)
        dateField.text = dateToShow
        setAgeOnAgeField(date, ageField)
    }

    private fun setAgeOnAgeField(date: Date, ageField: TextView) {
        val todayDate = Date()
        val difference = todayDate.year - date.year
        ageField.text = difference.toString()
    }
}