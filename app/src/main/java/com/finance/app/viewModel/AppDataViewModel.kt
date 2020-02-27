package com.finance.app.viewModel

import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.finance.app.others.AppEnums
import com.finance.app.persistence.db.MasterDB
import com.finance.app.persistence.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    fun getCoapplicantsList(leadId: Int): LiveData<CoApplicantsMaster?> {
        return masterDB.coApplicantsDao().getCoApplicants(leadId)
    }

    fun getLeadData(leadId: Int): LiveData<AllLeadMaster?> {
        return masterDB.allLeadsDao().getLead(leadId)
    }

    fun saveLead(lead: AllLeadMaster) {
        GlobalScope.launch {
            masterDB.allLeadsDao().insertLead(lead)
        }
    }

    private fun getLoanProductFromId(id: Int): LiveData<LoanProductMaster?> {
        return masterDB.loanProductDao().getLoanProductWithId(id)
    }

    private fun getStateNameFromId(id: Int): LiveData<StatesMaster?> {
        return masterDB.statesDao().getState(id)
    }


    fun getLoanProductNameFromId(id: Int?, tvLoanProduct: TextView) {
        id?.let {
            val loanProductLiveData = getLoanProductFromId(id)
            loanProductLiveData.observeForever {
                it?.let { lpMaster ->
                    tvLoanProduct.text = lpMaster.productName
                }
            }
        }
    }

    fun getLoanPurposeNameFromId(productId: Int?, purposeId: Int?, tvLoanPurpose: TextView) {
        productId?.let {
            purposeId?.let {

                val loanProductLiveData = getLoanProductFromId(productId)
                loanProductLiveData.observeForever {
                    it?.let {
                        val purposeList = it.loanPurposeList
                        purposeList?.let {
                            purposeList.forEach { purpose ->

                                if (purpose.loanPurposeID == purposeId) {
                                    tvLoanPurpose.text = purpose.loanPurposeName
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun getStateNameFromId(id: Int?, field: TextView) {
        id?.let {
            val stateLiveData = getStateNameFromId(id)
            stateLiveData.observeForever {
                it?.let { state ->
                    field.text = state.stateName
                }
            }
        }
    }

    fun getMasterDropdownNameFromId(id: Int?, column: AppEnums.DropdownMasterType, field: TextView) {
        id?.let {
            val allMasterDropDown = getAllMasterDropdown()
            allMasterDropDown.observeForever { dropdown ->
                val masterDataMap = dropdown?.getMasterDropDownMap()
                masterDataMap?.let {
                    field.text = returnMasterName(id, column, masterDataMap)
                }
            }
        }
    }


    private fun returnMasterName(id: Int, column: AppEnums.DropdownMasterType, master: HashMap<AppEnums.DropdownMasterType,
            ArrayList<DropdownMaster>?>): String? {

        val col = master[column]
        col?.let {
            col.forEach { dd ->
                if (dd.typeDetailID == id) {
                    return dd.toString()
                }
            }
        }
        return null
    }
}