package com.finance.app.persistence.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import motobeans.architecture.util.DateUtil.dateFormattingType.TYPE_API_RESPONSE
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


/**
 * Created by munishkumarthakur on 19/03/18.
 */
class Converters {

    var dfResponse: DateFormat = SimpleDateFormat(TYPE_API_RESPONSE.value)
    val alDateFormats = arrayListOf(dfResponse)

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        /*for(dateFormate in alDateFormats){
          try {
            return dateFormate.parse(value)
          } catch (e: Exception) {
            e.printStackTrace()
          }
        }*/
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return (if (date == null) null else date.time)?.toString()
    }

    /**
     * Converter AppMeetingTypeMaster
     */
    @TypeConverter
    fun fromStringToObject(value: String?): Object? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<Object>() {

        }.type
        return Gson().fromJson<Object>(value, listType)
    }

    @TypeConverter
    fun fromObjectToString(list: Object?): String? {
        list?.let {
            val gson = Gson()
            return gson.toJson(list)
        }
        return null
    }
}