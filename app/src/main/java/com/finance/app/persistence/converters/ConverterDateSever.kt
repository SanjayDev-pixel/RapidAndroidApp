package com.finance.app.persistence.converters

import androidx.room.TypeConverter
import motobeans.architecture.util.DateUtil.dateFormattingType
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class ConverterDateSever {

    var df: DateFormat = SimpleDateFormat(dateFormattingType.TYPE_API_RESPONSE.value)

    @TypeConverter
    fun toDate(value: String?): Date? {
        if (value != null) {
            try {
                return df.parse(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        } else {
            return null
        }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return (if (date == null) null else date!!.getTime())!!.toLong()
    }

}