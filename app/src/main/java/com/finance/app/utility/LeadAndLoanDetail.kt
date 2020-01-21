package com.finance.app.utility

import com.finance.app.persistence.model.CoApplicantsList
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class LeadAndLoanDetail {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    fun getLeadApplicantNum(applicantNum: Int, leadNum: String): String {
        return "${leadNum}00${applicantNum}"
    }

    fun getDefaultApplicant(currentPosition: Int, leadNum: String): CoApplicantsList {
        val dApplicant = CoApplicantsList()
        dApplicant.firstName = "Applicant"
        dApplicant.isMainApplicant = currentPosition == 0
        dApplicant.leadApplicantNumber = getLeadApplicantNum(currentPosition + 1, leadNum)
        return dApplicant
    }

    fun getDefaultCoApplicant(currentPosition: Int, leadNum: String): CoApplicantsList {
        val coApplicant = CoApplicantsList()
        coApplicant.firstName = "Co-Applicant ${currentPosition + 1}"
        coApplicant.isMainApplicant = false
        coApplicant.leadApplicantNumber = getLeadApplicantNum(currentPosition + 1, leadNum)
        return coApplicant
    }

}