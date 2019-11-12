package com.finance.app.utility

import com.finance.app.persistence.model.*
import com.google.gson.Gson
import motobeans.architecture.retrofit.response.Response
import org.json.JSONArray

class ResponseConversion {

    companion object {
        val gson = Gson()
    }

    fun toLoanMaster(response: Response.ResponseObj): LoanInfoMaster? {
        val master = LoanInfoMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, LoanInfoObj::class.java)
        return master
    }

    fun toPersonalMaster(response: Response.ResponseObj): PersonalInfoMaster {
        val master = PersonalInfoMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, PersonalApplicantList::class.java)
        return master
    }

    fun toEmploymentMaster(response: Response.ResponseObj): EmploymentMaster {
        val master = EmploymentMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, EmploymentApplicantList::class.java)
        return master
    }
}