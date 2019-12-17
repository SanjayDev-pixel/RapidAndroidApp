package com.finance.app.utility

import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.text.SimpleDateFormat
import java.util.*

class ConvertDate {
    fun convertToApiFormat(mDate: String): String {
        return try {
            val desirablePattern = "yyyy-MM-dd"
            val pattern = "dd-MMM-yyyy"
            val sdf = SimpleDateFormat(pattern)
            val date = sdf.parse(mDate)
            val desiredSdf = SimpleDateFormat(desirablePattern)
            desiredSdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun convertToAppFormat(mDate: String): String {
        return try {
            if (mDate.exIsNotEmptyOrNullOrBlank()) {
                val pattern = "yyyy-MM-dd"
                val desirablePattern = "dd-MMM-yyyy"
                val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
                val date = sdf.parse(mDate)
                val desiredSdf = SimpleDateFormat(desirablePattern, Locale.ENGLISH)
                desiredSdf.format(date)
            } else ""
        } catch (e: Exception) {
            getDifferenceFromDate(date = mDate)
        }
    }

    private fun getDifferenceFromDate(date: String): String {
        return try {
            val myFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            val selectedDate = myFormat.parse(date)
            val todayDate = Date()
            val difference = todayDate.year - selectedDate.year
            difference.toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun convertDate(mDate: String): String {
        return try {
            val pattern = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
            val ts = sdf.parse(mDate).time /1000
            val date = sdf.parse(mDate)

            val desirablePattern = "dd MMM : HH:mm"
            val desiredSdf = SimpleDateFormat(desirablePattern, Locale.ENGLISH)
            desiredSdf.format(ts)
        } catch (e: Exception) {
            ""
        }
    }
}