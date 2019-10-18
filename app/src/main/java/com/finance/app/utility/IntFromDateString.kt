package com.finance.app.utility

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class IntFromDateString(private val date: String, field: TextView) {
    init {
        val myFormat = SimpleDateFormat("dd-MMM-yyyy")
        val selectedDate = myFormat.parse(date)
        val todayDate = Date()
        val difference = todayDate.year - selectedDate.year
//        val yearBetween = (difference / (1000 * 60 * 60 * 24 * 12))
        field.text = difference.toString()
    }
}