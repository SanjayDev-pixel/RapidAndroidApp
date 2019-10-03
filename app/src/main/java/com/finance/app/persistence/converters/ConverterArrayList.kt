package com.finance.app.persistence.converters
import androidx.room.TypeConverter
import com.finance.app.persistence.model.AllMasterDropDownValue
import com.finance.app.persistence.model.DropdownMaster
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

/**
 * The 'ConverterArrayList' is converting ArrayList of HashMap to String and vice-versa to make it store-able entity in Database
 */
class ConverterArrayList {

    @TypeConverter
    fun fromStringToStringArrayList(value: String): List<String>? {
        val arrayListType = object : TypeToken<List<String>>() {

        }.type
        return Gson().fromJson<List<String>>(value, arrayListType)
    }

    @TypeConverter
    fun fromStringArrayListToString(arrayList: List<String>): String {
        val gson = Gson()
        return gson.toJson(arrayList)
    }

    @TypeConverter
    fun fromStringToArrayListAllSpinnerMaster(value: String?): ArrayList<AllMasterDropDownValue>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<AllMasterDropDownValue>>() {

        }.type
        return Gson().fromJson<ArrayList<AllMasterDropDownValue>>(value, arrayListType)
    }

    @TypeConverter
    fun fromAllSpinnerMasterArrayListToString(ArrayList: ArrayList<AllMasterDropDownValue>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListDropdownMaster(value: String?): ArrayList<DropdownMaster>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<DropdownMaster>>() {

        }.type
        return Gson().fromJson<ArrayList<DropdownMaster>>(value, arrayListType)
    }

    @TypeConverter
    fun fromDropdownMasterArrayListToString(ArrayList: ArrayList<DropdownMaster>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }
}