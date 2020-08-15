package com.finance.app.utility

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class SelectDOB(mContext: Context, dateField: TextView, private val differenceField: TextView) {
    init {
        val calender = Calendar.getInstance()
        calender.add(Calendar.YEAR, -18)
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { _, dYear, dMonth, dDay ->
            val date = "$dDay-${dMonth + 1}-$dYear"
            convertToDesirableFormat(date, dateField,mContext)
        }, year, month, day)


        dialog.datePicker.maxDate = calender.timeInMillis
        dialog.show()
    }

    private fun convertToDesirableFormat(mDate: String, dateField: TextView,mContext: Context) {
        val desirablePattern = "dd-MMM-yyyy"
        val pattern = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(pattern, Locale.US)
        val date = sdf.parse(mDate)
        val desiredSdf = SimpleDateFormat(desirablePattern, Locale.US)
        val dateToShow = desiredSdf.format(date)
        dateField.text = dateToShow
        setDifferenceInField(date, differenceField,mContext,dateField)
    }

    private fun setDifferenceInField(date: Date, differenceField: TextView,mContext: Context,dateField: TextView) {
        val todayDate = Date()
        val difference = todayDate.year - date.year
        System.out.println("difference>>>>"+difference)
        if(difference <=100) {
            differenceField.text = difference.toString()

        }
        else
        {
           Toast.makeText(mContext,"Age should be less than 100 year",Toast.LENGTH_LONG).show()
            dateField.text = ""
             //differenceField.error = "Age should be less then equal to  100 years"
             //differenceField.text = ""
        }
    }
}