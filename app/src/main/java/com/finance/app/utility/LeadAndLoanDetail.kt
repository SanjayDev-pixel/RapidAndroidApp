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
        val pApplicantList: ArrayList<PersonalApplicantsModel> = ArrayList()
        val pApplicant = PersonalApplicantsModel()
        pApplicant.firstName = "Applicant"
        pApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        pApplicantList.add(pApplicant)
        leadMaster.personalData.applicantDetails = pApplicantList
    }

    fun addEmploymentApplicant(leadMaster: AllLeadMaster) {
        val eApplicantList: ArrayList<EmploymentApplicantsModel> = ArrayList()
        val eApplicant = EmploymentApplicantsModel()
        eApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        eApplicantList.add(eApplicant)
        leadMaster.employmentData.applicantDetails = eApplicantList
    }

    fun addBankApplicant(leadMaster: AllLeadMaster) {
        val bApplicantList: ArrayList<BankDetailModel> = ArrayList()
        val bApplicant = BankDetailModel()
        bApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        bApplicantList.add(bApplicant)
        leadMaster.bankData.applicantDetails = bApplicantList
    }

    fun addAssetLiabilityApplicant(leadMaster: AllLeadMaster) {
        val alApplicantList: ArrayList<AssetLiabilityModel> = ArrayList()
        val alApplicant = AssetLiabilityModel()
        alApplicant.leadApplicantNumber = getLeadApplicantNum(leadMaster.leadNumber!!, leadMaster.personalData.applicantDetails.size)
        alApplicantList.add(alApplicant)
        leadMaster.assetLiabilityData.applicantDetails = alApplicantList
    }

    fun addApplicants(leadMaster: AllLeadMaster) {
        addPersonalApplicant(leadMaster)
        addEmploymentApplicant(leadMaster)
        addBankApplicant(leadMaster)
        addAssetLiabilityApplicant(leadMaster)
    }
}