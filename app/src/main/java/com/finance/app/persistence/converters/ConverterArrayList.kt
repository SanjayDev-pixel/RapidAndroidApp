package com.finance.app.persistence.converters
import androidx.room.TypeConverter
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import motobeans.architecture.retrofit.response.Response
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
    fun fromStringToArrayListAllSpinnerMaster(value: String?): ArrayList<AllMasterDropDown>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<AllMasterDropDown>>() {

        }.type
        return Gson().fromJson<ArrayList<AllMasterDropDown>>(value, arrayListType)
    }

    @TypeConverter
    fun fromAllSpinnerMasterArrayListToString(arrayList: ArrayList<AllMasterDropDown>?): String? {
        arrayList?.let {
            val gson = Gson()
            return gson.toJson(arrayList)
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

    @TypeConverter
    fun fromStringToArrayListLoanPurpose(value: String?): ArrayList<Response.LoanPurpose>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<Response.LoanPurpose>>() {

        }.type
        return Gson().fromJson<ArrayList<Response.LoanPurpose>>(value, arrayListType)
    }

    @TypeConverter
    fun fromLoanPurposeArrayListToString(ArrayList: ArrayList<Response.LoanPurpose>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListAllLeadMaster(value: String?): ArrayList<AllLeadMaster>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<AllLeadMaster>>() {

        }.type
        return Gson().fromJson<ArrayList<AllLeadMaster>>(value, arrayListType)
    }

    @TypeConverter
    fun fromAllLeadArrayListToStringMaster(ArrayList: ArrayList<AllLeadMaster>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }
}