package com.finance.app.utility

import com.finance.app.persistence.model.AllLeadMaster
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.DataBaseUtil
import java.util.*
import javax.inject.Inject

class LeadMetaData(leadId: Int) : Observable() {

    @Inject
    lateinit var dataBase: DataBaseUtil
    private var leadData: AllLeadMaster? = null

    init {
        ArchitectureApp.instance.component.inject(this)
        dataBase.provideDataBaseSource().allLeadsDao().getLead(leadId).observeForever { lead ->
            lead?.let { setValue(lead) }
        }
    }

    fun setValue(leadDetail: AllLeadMaster) {
        leadData = leadDetail
        setChanged()
        notifyObservers()
    }

    fun getLead(): AllLeadMaster? {
        return leadData
    }
}