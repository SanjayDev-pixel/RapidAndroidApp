package com.finance.app.persistence.converters

import android.arch.persistence.room.TypeConverter
import java.util.Date

class ConverterDateMillisecond {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return (if (date == null) null else date!!.getTime())!!.toLong()
    }

}