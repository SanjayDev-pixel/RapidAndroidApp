package com.finance.app.persistence.converters

import androidx.room.TypeConverter
import com.finance.app.persistence.model.*
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
    fun fromStringToLoanInfoObj(value: String?): LoanInfoObj? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<LoanInfoObj>() {

        }.type
        return Gson().fromJson<LoanInfoObj>(value, listType)
    }

    @TypeConverter
    fun fromLoanInfoObjToString(lead: LoanInfoObj?): String? {
        lead?.let {
            val gson = Gson()
            return gson.toJson(lead)
        }
        return null
    }

    @TypeConverter
    fun fromStringToAssetLiabilityMaster(value: String?): AssetLiabilityMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<AssetLiabilityMaster>() {

        }.type
        return Gson().fromJson<AssetLiabilityMaster>(value, listType)
    }

    @TypeConverter
    fun fromAssetLiabilityMasterToString(lead: AssetLiabilityMaster?): String? {
        lead?.let {
            val gson = Gson()
            return gson.toJson(lead)
        }
        return null
    }

    @TypeConverter
    fun fromStringToBankDetailMaster(value: String?): BankDetailMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<BankDetailMaster>() {

        }.type
        return Gson().fromJson<BankDetailMaster>(value, listType)
    }

    @TypeConverter
    fun fromBankDetailMasterToString(lead: BankDetailMaster?): String? {
        lead?.let {
            val gson = Gson()
            return gson.toJson(lead)
        }
        return null
    }

    @TypeConverter
    fun fromStringToBankLoanApplicationObj(value: String?): Response.BankDetailList? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<Response.BankDetailList>() {

        }.type
        return Gson().fromJson<Response.BankDetailList>(value, listType)
    }

    @TypeConverter
    fun fromBankLoanApplicationObjMasterToString(bank: Response.BankDetailList?): String? {
        bank?.let {
            val gson = Gson()
            return gson.toJson(bank)
        }
        return null
    }

    @TypeConverter
    fun fromStringToReferenceMaster(value: String?): ReferenceMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<ReferenceMaster>() {

        }.type
        return Gson().fromJson<ReferenceMaster>(value, listType)
    }

    @TypeConverter
    fun fromReferenceMasterToString(reference: ReferenceMaster?): String? {
        reference?.let {
            val gson = Gson()
            return gson.toJson(reference)
        }
        return null
    }

    @TypeConverter
    fun fromStringToAddressBean(value: String?): Requests.AddressBean? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<ReferenceMaster>() {

        }.type
        return Gson().fromJson<Requests.AddressBean>(value, listType)
    }

    @TypeConverter
    fun fromReferenceMasterToString(address: Requests.AddressBean?): String? {
        address?.let {
            val gson = Gson()
            return gson.toJson(address)
        }
        return null
    }

    @TypeConverter
    fun fromStringToPersonalInfoMaster(value: String?): PersonalInfoMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<PersonalInfoMaster>() {

        }.type
        return Gson().fromJson<PersonalInfoMaster>(value, listType)
    }

    @TypeConverter
    fun fromPersonalInfoMasterToString(personalInfo: PersonalInfoMaster?): String? {
        personalInfo?.let {
            val gson = Gson()
            return gson.toJson(personalInfo)
        }
        return null
    }

    @TypeConverter
    fun fromStringToPersonalApplicantList(value: String?): PersonalApplicantList? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<PersonalApplicantList>() {

        }.type
        return Gson().fromJson<PersonalApplicantList>(value, listType)
    }

    @TypeConverter
    fun fromPersonalApplicantListToString(applicant: PersonalApplicantList?): String? {
        applicant?.let {
            val gson = Gson()
            return gson.toJson(applicant)
        }
        return null
    }

    @TypeConverter
    fun fromStringToEmploymentMaster(value: String?): EmploymentMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<EmploymentMaster>() {

        }.type
        return Gson().fromJson<EmploymentMaster>(value, listType)
    }

    @TypeConverter
    fun fromEmploymentMasterToString(states: EmploymentMaster?): String? {
        states?.let {
            val gson = Gson()
            return gson.toJson(states)
        }
        return null
    }

    @TypeConverter
    fun fromStringToEmploymentApplicantList(value: String?): EmploymentApplicantList? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<EmploymentApplicantList>() {

        }.type
        return Gson().fromJson<EmploymentApplicantList>(value, listType)
    }

    @TypeConverter
    fun fromEmploymentApplicantListToString(applicant: EmploymentApplicantList?): String? {
        applicant?.let {
            val gson = Gson()
            return gson.toJson(applicant)
        }
        return null
    }

    @TypeConverter
    fun fromStringToStateMaster(value: String?): StatesMaster? {
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            return null
        }
        val listType = object : TypeToken<StatesMaster>() {

        }.type
        return Gson().fromJson<StatesMaster>(value, listType)
    }

    @TypeConverter
    fun fromStatesMasterToString(states: StatesMaster?): String? {
        states?.let {
            val gson = Gson()
            return gson.toJson(states)
        }
        return null
    }

}