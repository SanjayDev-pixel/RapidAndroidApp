package com.finance.app.persistence.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.util.ArrayList


/**
 * The 'ConverterArrayList' is converting ArrayList of HashMap to String and vice-versa to make it store-able entity in Database
 */
class ConverterArrayList {

    /**
     * Handle ArrayList of String
     */
    @TypeConverter
    fun fromStringToStringArrayList(value: String): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.type
        return Gson().fromJson<ArrayList<String>>(value, listType)
    }

    @TypeConverter
    fun fromStringArrayListToString(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    /**
     * Handle ArrayList of ResponseImageUrl
     */
    @TypeConverter
    fun fromStringToObjectArrayList(value: String?): ArrayList<Object>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<ArrayList<Object>>() {

        }.type
        return Gson().fromJson<ArrayList<Object>>(value, listType)
    }

    @TypeConverter
    fun fromObjectArrayListToString(list: ArrayList<Object>?): String? {
        list?.let {
            val gson = Gson()
            return gson.toJson(list)
        }
        return null
    }
}