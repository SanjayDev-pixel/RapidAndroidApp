/*
* Copyright (C) 2017 The Munish/BrighterBrain Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.finance.app.viewModel

import android.app.ProgressDialog
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.finance.app.persistence.db.MasterDB
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.CoApplicantsList
import com.finance.app.persistence.model.CoApplicantsMaster
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class LeadDataViewModel(private val activity: FragmentActivity, private val database: MasterDB) : BaseViewModel(activity) {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var api: ApiProject
    private val viewState: MutableLiveData<ProductViewState> = MutableLiveData()
    private val presenter = Presenter()

    init {
        ArchitectureApp.instance.component.inject(this)
        viewState.value = ProductViewState()
    }

    private fun currentViewState(): ProductViewState = viewState.value!!

    data class ProductViewState(var isNoDataFound: Boolean = false,
                                val isLoading: Boolean = false,
                                val isError: Boolean = false,
                                val isEmptyData: Boolean = false,
                                val errorMessage: String? = null)

    private fun isLoading(isLoading: Boolean) {
        viewState.value = currentViewState().copy(isLoading = isLoading)
    }

    private fun isError(isError: Boolean, errorMessage: String?) {
        viewState.value = currentViewState().copy(isError = isError, errorMessage = errorMessage)
    }

    private fun isEmptyList(isEmptyData: Boolean) {
        viewState.value = currentViewState().copy(isEmptyData = isEmptyData)
    }

    fun getLeadData(leadId: String) {
        presenter.callNetwork(ConstantsApi.CALL_LOAN_PRODUCT, dmiConnector = LoanProductsDropdown())
        presenter.callNetwork(ConstantsApi.CALL_ALL_STATES, dmiConnector = AllStatesList())
        presenter.callNetwork(ConstantsApi.CALL_COAPPLICANTS_LIST, dmiConnector = CallCoApplicantList(leadId))
    }

    inner class AllMasterDropdownData : ViewGeneric<String?, Response.ResponseAllMasterDropdown>(context = activity) {
        override val apiRequest: String?
            get() = null

        override fun getApiSuccess(value: Response.ResponseAllMasterDropdown) {
            if (value.responseCode == Constants.SUCCESS) {
                GlobalScope.launch {
                    dataBase.provideDataBaseSource().allMasterDropDownDao().insertAllMasterDropDownValue(value.responseObj)
                }
            } else {
                showToast(value.responseMsg)
            }
        }

    }

    inner class LoanProductsDropdown : ViewGeneric<String?, Response.ResponseLoanProduct>(context = activity) {
        override val apiRequest: String?
            get() = null

        override fun getApiSuccess(value: Response.ResponseLoanProduct) {
            if (value.responseCode == Constants.SUCCESS) {
                GlobalScope.launch {
                    dataBase.provideDataBaseSource().loanProductDao().insertLoanProductList(value.responseObj)
                }
            } else {
                showToast(value.responseMsg)
            }
        }
    }

    inner class AllStatesList : ViewGeneric<String?, Response.ResponseStatesDropdown>(context = activity) {
        override val apiRequest: String?
            get() = null

        override fun getApiSuccess(value: Response.ResponseStatesDropdown) {
            if (value.responseCode == Constants.SUCCESS) {
                GlobalScope.launch {
                    dataBase.provideDataBaseSource().statesDao().insertStates(value.responseObj)
                }
            } else {
                showToast(value.responseMsg)
            }
        }
    }

    inner class AllLeadsList : ViewGeneric<String?, Response.ResponseGetAllLeads>(context = activity) {
        override val apiRequest: String?
            get() = null

        override fun getApiSuccess(value: Response.ResponseGetAllLeads) {
            if (value.responseCode == Constants.SUCCESS) {
                GlobalScope.launch { dataBase.provideDataBaseSource().allLeadsDao().deleteAllLeadMaster() }

                val progress = ProgressDialog(activity)
                progress.setMessage("Getting Leads")
                progress.show()
                Handler().postDelayed({
                    saveDataToDB(value.responseObj)
                    if (progress.isShowing) {
                        progress.dismiss()
                    }
                }, 1000)
            }
        }

        private fun saveDataToDB(leads: ArrayList<AllLeadMaster>) {
            GlobalScope.launch {
                dataBase.provideDataBaseSource().allLeadsDao().insertLeadsList(leads)
            }
        }
    }


    inner class CallCoApplicantList(private val leadId: String) : ViewGeneric<String, Response.ResponseCoApplicants>(context = activity) {
        override val apiRequest: String
            get() = leadId

        override fun getApiSuccess(value: Response.ResponseCoApplicants) {
            if (value.responseCode == Constants.SUCCESS) {
                saveApplicantToDB(value.responseObj)
            }
        }

        private fun saveApplicantToDB(responseObj: ArrayList<CoApplicantsList>) {
            GlobalScope.launch {
                val coApplicantMaster = CoApplicantsMaster()
                coApplicantMaster.coApplicantsList = responseObj
                coApplicantMaster.leadID = leadId.toInt()
                dataBase.provideDataBaseSource().coApplicantsDao().insertCoApplicants(coApplicantMaster)
            }
        }
    }
}