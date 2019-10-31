package com.finance.app.persistence.converters

import androidx.room.TypeConverter
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.LoanInfoMaster
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank
import java.util.*

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
    fun fromStringToAllDropDownMaster(value: String?): AllMasterDropDown? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<AllMasterDropDown>() {

        }.type
        return Gson().fromJson<AllMasterDropDown>(value, listType)
    }

    @TypeConverter
    fun fromAllDropDownMasterToString(list: AllMasterDropDown?): String? {
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

    @TypeConverter
    fun fromStringToLoanPurpose(value: String?): Response.LoanPurpose? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<Response.LoanPurpose>() {

        }.type
        return Gson().fromJson<Response.LoanPurpose>(value, listType)
    }

    @TypeConverter
    fun fromLoanPurposeToString(list: Response.LoanPurpose?): String? {
        list?.let {
            val gson = Gson()
            return gson.toJson(list)
        }
        return null
    }

    @TypeConverter
    fun fromStringToAllLeadMaster(value: String?): AllLeadMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<AllLeadMaster>() {

        }.type
        return Gson().fromJson<AllLeadMaster>(value, listType)
    }

    @TypeConverter
    fun fromAllLeadMasterToString(lead: AllLeadMaster?): String? {
        lead?.let {
            val gson = Gson()
            return gson.toJson(lead)
        }
        return null
    }

    @TypeConverter
    fun fromStringToLoanInfoMaster(value: String?): LoanInfoMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<LoanInfoMaster>() {

        }.type
        return Gson().fromJson<LoanInfoMaster>(value, listType)
    }

    @TypeConverter
    fun fromLoanInfoMasterToString(lead: LoanInfoMaster?): String? {
        lead?.let {
            val gson = Gson()
            return gson.toJson(lead)
        }
        return null
    }

    @TypeConverter
    fun fromStringToLoanInfoObj(value: String?): Requests.LoanInfoObj? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<Requests.LoanInfoObj>() {

        }.type
        return Gson().fromJson<Requests.LoanInfoObj>(value, listType)
    }

    @TypeConverter
    fun fromLoanInfoObjToString(lead: Requests.LoanInfoObj?): String? {
        lead?.let {
            val gson = Gson()
            return gson.toJson(lead)
        }
        return null
    }

}