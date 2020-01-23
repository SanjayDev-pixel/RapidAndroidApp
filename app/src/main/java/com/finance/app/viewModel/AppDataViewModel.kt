package com.finance.app.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.finance.app.persistence.db.MasterDB
import com.finance.app.persistence.model.*

class AppDataViewModel(activity: FragmentActivity, private val masterDB: MasterDB) : BaseViewModel(activity, masterDB) {

    fun getAllLeads(): LiveData<List<AllLeadMaster>?> {
        return masterDB.allLeadsDao().getAllLeads()
    }

    fun getLeadsByStatus(status: String): LiveData<List<AllLeadMaster>?> {
        return masterDB.allLeadsDao().getLeadsByStatus(status)
    }

    fun getAllStates(): LiveData<List<StatesMaster>?> {
        return masterDB.statesDao().getAllStates()
    }

    fun getAllMasterDropdown(): LiveData<AllMasterDropDown?> {
        return masterDB.allMasterDropDownDao().getMasterDropdownValue()
    }

    fun getLoanInfo(leadId: String): LiveData<LoanInfoMaster?> {
        return masterDB.loanInfoDao().getLoanInfo(leadId)
    }

    fun getPersonalInfo(leadId: String): LiveData<PersonalInfoMaster?> {
        return masterDB.personalInfoDao().getPersonalInfo(leadId)
    }

    fun getLoanProductMaster(): LiveData<List<LoanProductMaster>?> {
        return masterDB.loanProductDao().getAllLoanProduct()
    }

}