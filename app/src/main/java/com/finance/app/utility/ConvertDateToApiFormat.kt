package com.finance.app.utility

import java.text.SimpleDateFormat

class ConvertDateToApiFormat {

    fun convertToDesirableFormat(mDate: String): String {
        val desirablePattern = "yyyy-MM-dd"
        val pattern = "dd-MMM-yyyy"
        val sdf = SimpleDateFormat(pattern)
        val date = sdf.parse(mDate)
        val desiredSdf = SimpleDateFormat(desirablePattern)
        return desiredSdf.format(date)
    }
}