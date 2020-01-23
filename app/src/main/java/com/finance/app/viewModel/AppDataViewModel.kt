package com.finance.app.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.finance.app.persistence.db.MasterDB
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.persistence.model.StatesMaster

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

    fun getLoanProductMaster(): LiveData<List<LoanProductMaster>?> {
        return masterDB.loanProductDao().getAllLoanProduct()
    }

//    fun getLoanProducts(VisualPlanning: VisualPlanning) {
//        GlobalScope.launch { masterDB.visualPlanningDao().insertPlan(VisualPlanning) }
//    }
}