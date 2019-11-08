package com.finance.app.utility

import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class LeadAndLoanDetail {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    fun getLeadApplicantNum(applicantNum: Int): String {
        val leadNum = sharedPreferences.getLeadNum()
        return "${leadNum}00${applicantNum}"
    }

    fun getLeadId(): Int {
        return sharedPreferences.getLeadId()!!.toInt()
    }
}