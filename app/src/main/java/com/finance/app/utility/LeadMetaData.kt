package com.finance.app.utility

import androidx.lifecycle.MutableLiveData
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.BankDetailModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.persistence.model.ReferenceModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.DataBaseUtil
import java.util.*
import javax.inject.Inject

class LeadMetaData : Observable() {

    @Inject
    lateinit var dataBase: DataBaseUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    fun getAndPopulateLeadData(leadId: Int) {
        dataBase.provideDataBaseSource().allLeadsDao().getLead(leadId).observeForever { lead ->
            setLeadData(lead)
        }
    }

    companion object {
        private var leadData = MutableLiveData<AllLeadMaster?>()
        fun setLeadData(leadDetail: AllLeadMaster?) {
            leadData.value = leadDetail
        }

        fun getLeadObservable() = leadData
        fun getLeadData() = leadData.value
        fun getLeadId() = leadData.value?.leadID

        fun addCoApplicant() {
            val leadData = getLeadData()
            leadData?.let {
                LeadAndLoanDetail().addApplicants(leadData)
            }
            setLeadData(leadData)
        }
    }

    private fun insertLeadIntoDB(lead: AllLeadMaster): Job {
        lead.isSyncWithServer = false
        return GlobalScope.launch {
            dataBase.provideDataBaseSource().allLeadsDao().insertLead(lead)
        }
    }

    fun savePersonalData(applicants: ArrayList<PersonalApplicantsModel>) {
        val lead = getLeadData()
        lead?.let {
            lead.personalData.applicantDetails = applicants
            insertLeadIntoDB(lead)
        }
    }

    fun saveBankData(bankDetailsList: ArrayList<BankDetailModel>) {
        val lead = getLeadData()
        lead?.let {
            lead.bankData.applicantBankDetails = bankDetailsList
            insertLeadIntoDB(lead)
        }
    }

    fun saveReferenceData(referenceDetailsList: ArrayList<ReferenceModel>) {
        val lead = getLeadData()
        lead?.let {
            lead.referenceData.referenceDetails = referenceDetailsList
            insertLeadIntoDB(lead)
        }
    }

}