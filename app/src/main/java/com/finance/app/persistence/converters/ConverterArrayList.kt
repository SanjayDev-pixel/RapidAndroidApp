package com.finance.app.persistence.converters
import androidx.room.TypeConverter
import com.finance.app.persistence.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

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
    fun fromAllLeadArrayListToString(ArrayList: ArrayList<AllLeadMaster>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }


    @TypeConverter
    fun fromStringToArrayListAssetDetail(value: String?): ArrayList<AssetDetail>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<AssetDetail>>() {

        }.type
        return Gson().fromJson<ArrayList<AssetDetail>>(value, arrayListType)
    }

    @TypeConverter
    fun fromAssetDetailArrayListToString(ArrayList: ArrayList<AssetDetail>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListCardDetail(value: String?): ArrayList<CardDetail>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<CardDetail>>() {

        }.type
        return Gson().fromJson<ArrayList<CardDetail>>(value, arrayListType)
    }

    @TypeConverter
    fun fromCardDetailArrayListToString(ArrayList: ArrayList<CardDetail>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListObligationDetail(value: String?): ArrayList<ObligationDetail>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<ObligationDetail>>() {

        }.type
        return Gson().fromJson<ArrayList<ObligationDetail>>(value, arrayListType)
    }

    @TypeConverter
    fun fromObligationDetailArrayListToString(ArrayList: ArrayList<ObligationDetail>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListReferenceMaster(value: String?): ArrayList<ReferenceMaster>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<ReferenceMaster>>() {

        }.type
        return Gson().fromJson<ArrayList<ReferenceMaster>>(value, arrayListType)
    }

    @TypeConverter
    fun fromReferenceArrayListToString(ArrayList: ArrayList<ReferenceMaster>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListPersonalApplicantModel(value: String?): ArrayList<PersonalApplicantsModel>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<PersonalApplicantsModel>>() {

        }.type
        return Gson().fromJson<ArrayList<PersonalApplicantsModel>>(value, arrayListType)
    }

    @TypeConverter
    fun fromPersonalApplicantModelArrayListToString(ArrayList: ArrayList<PersonalApplicantsModel>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

    @TypeConverter
    fun fromStringToArrayListStateMaster(value: String?): ArrayList<StatesMaster>? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val arrayListType = object : TypeToken<ArrayList<StatesMaster>>() {

        }.type
        return Gson().fromJson<ArrayList<StatesMaster>>(value, arrayListType)
    }

    @TypeConverter
    fun fromStateMasterArrayListToString(ArrayList: ArrayList<StatesMaster>?): String? {
        ArrayList?.let {
            val gson = Gson()
            return gson.toJson(ArrayList)
        }
        return null
    }

}
