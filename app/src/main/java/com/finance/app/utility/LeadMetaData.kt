package com.finance.app.utility

import androidx.lifecycle.MutableLiveData
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AssetLiabilityList
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.persistence.model.PropertyModel
import kotlinx.coroutines.GlobalScope
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

    fun savePersonalData(applicants: ArrayList<PersonalApplicantsModel>) {
        val lead = getLeadData()
        lead?.let {
            lead.personalData.applicantDetails = applicants
            GlobalScope.launch {
                dataBase.provideDataBaseSource().allLeadsDao().insertLead(lead)
            }
        }
    }


     fun saveAssetLiabilityData(pApplicantList: AssetLiabilityList?) {

         val lead: AllLeadMaster? = getLeadData()
         lead?.let {
             lead.assetLiabilityData = pApplicantList!!
             GlobalScope.launch {
                 dataBase.provideDataBaseSource().allLeadsDao().insertLead(lead)
             }

         }
     }

    fun savePropertyData(leadPropertyData:PropertyModel){


        val lead: AllLeadMaster? = getLeadData()
        lead?.let {
            lead.propertyData = leadPropertyData
            GlobalScope.launch {
                dataBase.provideDataBaseSource().allLeadsDao().insertLead(lead)
            }

        }
    }

}