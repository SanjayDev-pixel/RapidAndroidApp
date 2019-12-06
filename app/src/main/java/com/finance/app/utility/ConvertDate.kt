package com.finance.app.utility

import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.text.SimpleDateFormat
import java.util.*

class ConvertDate {
    fun convertToApiFormat(mDate: String): String {
        try {
            val desirablePattern = "yyyy-MM-dd"
            val pattern = "dd-MMM-yyyy"
            val sdf = SimpleDateFormat(pattern)
            val date = sdf.parse(mDate)
            val desiredSdf = SimpleDateFormat(desirablePattern)
            return desiredSdf.format(date)
        } catch (e: Exception) {
            return ""
        }
    }

    fun convertToAppFormat(mDate: String): String {
        try {
            return if (mDate.exIsNotEmptyOrNullOrBlank()) {
                val pattern = "yyyy-MM-dd"
                val desirablePattern = "dd-MMM-yyyy"
                val sdf = SimpleDateFormat(pattern)
                val date = sdf.parse(mDate)
                val desiredSdf = SimpleDateFormat(desirablePattern)
                desiredSdf.format(date)
            } else ""
        } catch (e: Exception) {
            return getDifferenceFromDate(date = mDate)
        }
    }

    fun getDifferenceFromDate(date: String): String {
        try {
            val myFormat = SimpleDateFormat("dd-MMM-yyyy")
            val selectedDate = myFormat.parse(date)
            val todayDate = Date()
            val difference = todayDate.year - selectedDate.year
            return difference.toString()
        } catch (e: Exception) {
            return ""
        }
    }
}