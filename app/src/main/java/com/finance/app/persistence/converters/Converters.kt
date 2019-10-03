package com.finance.app.persistence.converters

import androidx.room.TypeConverter
import com.finance.app.persistence.model.AllMasterDropDownValue
import com.finance.app.persistence.model.DropdownMaster
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
        return (date?.time)?.toString()
    }

    @TypeConverter
    fun fromStringToAllDropDownMaster(value: String?): AllMasterDropDownValue? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<AllMasterDropDownValue>() {

        }.type
        return Gson().fromJson<AllMasterDropDownValue>(value, listType)
    }

    @TypeConverter
    fun fromAllDropDownMasterToString(list: AllMasterDropDownValue?): String? {
        list?.let {
            val gson = Gson()
            return gson.toJson(list)
        }
        return null
    }

    @TypeConverter
    fun fromStringToDropdownMaster(value: String?): DropdownMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<DropdownMaster>() {

        }.type
        return Gson().fromJson<DropdownMaster>(value, listType)
    }

    @TypeConverter
    fun fromDropdownMasterToString(list: DropdownMaster?): String? {
        list?.let {
            val gson = Gson()
            return gson.toJson(list)
        }
        return null
    }
}