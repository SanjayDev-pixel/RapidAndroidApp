package com.finance.app.utility

import com.finance.app.persistence.model.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class LeadAndLoanDetail {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    private fun getLeadApplicantNum(leadNum: String, applicantNum: Int): String {
        return "${leadNum}00${applicantNum + 1}"
    }

    fun getApplicant(leadNum: String?): PersonalApplicantsModel {
        val dApplicant = PersonalApplicantsModel()
        dApplicant.firstName = "Applicant"
        dApplicant.isMainApplicant = true
        dApplicant.leadApplicantNumber = getLeadApplicantNum(leadNum!!, 1)
        return dApplicant
    }

    fun addPersonalApplicant(leadMaster: AllLeadMaster) {
        val pApplicant = PersonalApplicantsModel()
        pApplicant.firstName = "Applicant"
        pApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        leadMaster.personalData.applicantDetails.add(pApplicant)
    }

    fun addEmploymentApplicant(leadMaster: AllLeadMaster) {
        val eApplicant = EmploymentApplicantsModel()
        eApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        leadMaster.employmentData.applicantDetails?.add(eApplicant)
    }

    fun addBankApplicant(leadMaster: AllLeadMaster) {
        val bApplicant = BankDetailModel()
        bApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        leadMaster.bankData.applicantDetails?.add(bApplicant)
    }

    fun addAssetLiabilityApplicant(leadMaster: AllLeadMaster) {
        val alApplicant = AssetLiabilityModel()
        alApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        leadMaster.assetLiabilityData.applicantDetails?.add(alApplicant)
    }

    fun addApplicants(leadMaster: AllLeadMaster) {
        addPersonalApplicant(leadMaster)
        addEmploymentApplicant(leadMaster)
        addBankApplicant(leadMaster)
        addAssetLiabilityApplicant(leadMaster)
    }
}