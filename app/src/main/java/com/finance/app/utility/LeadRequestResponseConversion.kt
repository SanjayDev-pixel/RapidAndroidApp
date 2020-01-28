package com.finance.app.utility

import com.finance.app.others.AppEnums
import com.finance.app.others.AppEnums.FormType.*
import com.finance.app.persistence.model.*
import com.google.gson.Gson
import motobeans.architecture.retrofit.response.Response
import java.io.Serializable
import java.util.*

class LeadRequestResponseConversion {

    companion object {
        val gson = Gson()
    }

    fun getResponseObject(form: AppEnums.FormType, response: Response.LoanApplicationGetObj?): Serializable? {

        var objectGeneric: Serializable? = null
        response?.let {
            objectGeneric = when (form) {
                LOANINFO -> gson.fromJson(response.draftData, LoanInfoModel::class.java)
                PERSONALINFO -> gson.fromJson(response.draftData, PersonalApplicantList::class.java)
                EMPLOYMENT -> gson.fromJson(response.draftData, EmploymentApplicantList::class.java)
                BANKDETAIL -> gson.fromJson(response.draftData, BankDetailList::class.java)
                LIABILITYASSET -> gson.fromJson(response.draftData, AssetLiabilityList::class.java)
                PROPERTY -> gson.fromJson(response.draftData, PropertyModel::class.java)
                REFERENCE -> gson.fromJson(response.draftData, ReferencesList::class.java)
            }
        }

        return objectGeneric
    }
}