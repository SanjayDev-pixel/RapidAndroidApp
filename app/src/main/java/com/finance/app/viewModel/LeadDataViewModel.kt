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

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.LeadRequestResponseConversion
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

class LeadDataViewModel(private val activity: FragmentActivity) : BaseViewModel(activity) {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var api: ApiProject
    private val presenter = Presenter()

    val isAllApiCallCompleted = MutableLiveData<Boolean>()
    private val isLeadSync_LoanInfo = MutableLiveData<Boolean>()
    private val isLeadSync_PersonalInfo = MutableLiveData<Boolean>()
    private val isLeadSync_Employment = MutableLiveData<Boolean>()
    private val isLeadSync_BankDetail = MutableLiveData<Boolean>()
    private val isLeadSync_LiabilityAndAssets = MutableLiveData<Boolean>()
    private val isLeadSync_Property = MutableLiveData<Boolean>()
    private val isLeadSync_Reference = MutableLiveData<Boolean>()
    private val isLeadSync_DocumentChecklist = MutableLiveData<Boolean>()

    private var leadData: AllLeadMaster? = null

    private val listOfToSyncData = listOf(isLeadSync_LoanInfo, isLeadSync_PersonalInfo, isLeadSync_Employment,
            isLeadSync_BankDetail, isLeadSync_LiabilityAndAssets, isLeadSync_Property, isLeadSync_Reference, isLeadSync_DocumentChecklist)

    init {
        ArchitectureApp.instance.component.inject(this)

        checkIfAppConfiguredSuccessfully()
        setObservers()
    }

    private fun setObservers() {
        isAllApiCallCompleted.observeForever {
            when (it) {
                true -> {
                    leadData?.isDetailAlreadySync = true
                    saveLead(leadData)
                }
            }
        }

        listOfToSyncData?.forEach {
            it.observeForever { checkIfAppConfiguredSuccessfully() }
        }
    }

    private fun checkIfAppConfiguredSuccessfully() {

        var isAllApiCallsCompletedValue = true

        permissionLoop@ for (observable in listOfToSyncData) {
            val isSyncCompleted = getBoolean(observable)
            if (!isSyncCompleted) {
                isAllApiCallsCompletedValue = false
                break@permissionLoop
            }
        }

        isAllApiCallCompleted.value = isAllApiCallsCompletedValue
    }

    private fun getBoolean(liveData: MutableLiveData<Boolean>) = liveData.value ?: false

    fun getLeadData(leadData: AllLeadMaster) {
        this.leadData = leadData

        val leadId = (leadData.leadID ?: "").toString()
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.LOANINFO))
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.PERSONALINFO))
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.EMPLOYMENT))
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.BANKDETAIL))
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.LIABILITYASSET))
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.PROPERTY))
        presenter.callNetwork(ConstantsApi.CALL_GET_LOAN_APP, CallGetLoan(leadData = leadData, leadId = leadId, form = AppEnums.FormType.REFERENCE))

    }

    private fun saveLead(leadData: AllLeadMaster?) {
        leadData?.let {
            GlobalScope.launch {
                dataBase.provideDataBaseSource().allLeadsDao().insertLead(leadData!!)
            }
        }
    }

    inner class CallGetLoan(private val leadData: AllLeadMaster, private val leadId: String, private val form: AppEnums.FormType) : ViewGeneric<ArrayList<String>?,
            Response.ResponseGetLoanApplication>(context = activity) {
        override val apiRequest: ArrayList<String>?
            get() = arrayListOf(leadId, form.type)

        override fun getApiSuccess(value: Response.ResponseGetLoanApplication) {
            if (value.responseCode == Constants.SUCCESS) {
                value.responseObj?.let {
                    saveDataToLead(value.responseObj)
                }
            }
        }

        private fun saveDataToLead(responseObj: Response.LoanApplicationGetObj) {

            val apiResponseObject = LeadRequestResponseConversion().getResponseObject(form = form, response = responseObj)
            when (form) {
                AppEnums.FormType.LOANINFO -> {
                    setObservableValue(isLeadSync_LoanInfo, true)
                    leadData.loanData = apiResponseObject as LoanInfoModel
                }
                AppEnums.FormType.PERSONALINFO -> {
                    setObservableValue(isLeadSync_PersonalInfo, true)
                    leadData.personalData = apiResponseObject as PersonalApplicantList
                }
                AppEnums.FormType.EMPLOYMENT -> {
                    setObservableValue(isLeadSync_Employment, true)
                    leadData.employmentData = apiResponseObject as EmploymentApplicantList
                }
                AppEnums.FormType.BANKDETAIL -> {
                    setObservableValue(isLeadSync_BankDetail, true)
                    leadData.bankData = apiResponseObject as BankDetailList
                }
                AppEnums.FormType.LIABILITYASSET -> {
                    setObservableValue(isLeadSync_LiabilityAndAssets, true)
                    leadData.assetLiabilityData = apiResponseObject as AssetLiabilityList
                }
                AppEnums.FormType.PROPERTY -> {
                    setObservableValue(isLeadSync_Property, true)
                    leadData.propertyData = apiResponseObject as PropertyModel
                }
                AppEnums.FormType.REFERENCE -> {
                    setObservableValue(isLeadSync_Reference, true)
                    leadData.referenceData = apiResponseObject as ReferencesList
                }
            }
        }

        private fun setObservableValue(observaleSync: MutableLiveData<Boolean>, isSync: Boolean) {
            observaleSync.value = isSync
        }
    }
}