package com.finance.app.presenter.presenter

import android.app.ProgressDialog
import android.content.Context
import com.finance.app.persistence.model.LoanApplicationRequest
import com.finance.app.presenter.connector.Connector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.util.DialogFactory
import motobeans.architecture.util.exShowToast
import javax.inject.Inject

/**
 * Created by munishkumarthakur on 21/12/19.
 */
@Suppress("UNCHECKED_CAST")
class Presenter {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    fun <RequestApi, ResponseApi> callNetwork(type: ConstantsApi, dmiConnector: Connector.ViewOpt<RequestApi, ResponseApi>) {
        val requestApi = when (type) {
            ConstantsApi.CALL_ADD_LEAD -> apiProject.api.addLead(dmiConnector.apiRequest as Requests.RequestAddLead)
            ConstantsApi.CALL_ALL_MASTER_VALUE -> apiProject.api.getAllMasterValue()
            ConstantsApi.CALL_LOAN_PRODUCT -> apiProject.api.getLoanProduct()
            ConstantsApi.CALL_GET_ALL_LEADS -> apiProject.api.getAllLeads()
            ConstantsApi.CALL_ALL_STATES -> apiProject.api.getStates()
            ConstantsApi.CALL_LOGIN -> apiProject.api.loginUser(dmiConnector.apiRequest as Requests.RequestLogin)
            ConstantsApi.CALL_COAPPLICANTS_LIST -> apiProject.api.getCoApplicantsList(dmiConnector.apiRequest as String)
            ConstantsApi.CALL_SEND_OTP -> apiProject.api.sendOTP(dmiConnector.apiRequest as Requests.RequestSendOTP)
            ConstantsApi.CALL_VERIFY_OTP -> apiProject.api.verifyOTP(dmiConnector.apiRequest as Requests.RequestVerifyOTP)
            ConstantsApi.CALL_SOURCE_CHANNEL_PARTNER_NAME -> {
                val strings = dmiConnector.apiRequest as ArrayList<String>
                apiProject.api.sourceChannelPartnerName(strings[0], strings[1], strings[2])
            }
            ConstantsApi.CALL_GET_LOAN_APP -> {
                val strings = dmiConnector.apiRequest as ArrayList<String>
                apiProject.api.getLoanApp(strings[0], strings[1])
            }
            ConstantsApi.CALL_POST_LOAN_APP -> apiProject.api.postLoanApp(dmiConnector.apiRequest as LoanApplicationRequest)
            ConstantsApi.CALL_UPDATE_CALL -> apiProject.api.postCallUpdate((dmiConnector.apiRequest as Requests.RequestCallUpdate).leadID, dmiConnector.apiRequest as Requests.RequestCallUpdate)
           // ConstantsApi.CALL_FINAL_SUBMIT -> apiProject.api.finalSubmit(dmiConnector.apiRequest as LoanApplicationRequest)

            else -> return
        }

        callApi(dmiConnector, requestApi = requestApi)
    }

    private fun <RequestApi, ResponseApi> callApi(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>, requestApi: Observable<out Any>) {
        val dispose = requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewOpt.showProgressDialog() }
                .doFinally { viewOpt.hideProgressDialog() }
                .subscribe({ response -> response?.let { apiSuccess(viewOpt, response as ResponseApi) } },
                        { e -> apiFailure(viewOpt, e) })

    }

    private fun <RequestApi, ResponseApi> apiSuccess(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>, response: ResponseApi) {
        viewOpt.getApiSuccess(value = response)
    }

    private fun <RequestApi, ResponseApi> apiFailure(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>, e: Throwable?) {
        viewOpt.getApiFailure(e?.message ?: "")
    }
}

abstract class ViewGeneric<RequestApi, ResponseApi>(val context: Context) : Connector.ViewOpt<RequestApi, ResponseApi> {

    internal var progressDialog: ProgressDialog? = null

    override fun showToast(msg: String) {
        msg.exShowToast(context)
    }

    override fun showProgressDialog() {
        when (BaseAppCompatActivity.progressDialog == null) {
            true -> BaseAppCompatActivity.progressDialog = DialogFactory.getInstance(context = context)
        }
        BaseAppCompatActivity.progressDialog?.show()
    }

    override fun hideProgressDialog() {
        BaseAppCompatActivity.progressDialog?.hide()
    }

    override fun getApiFailure(msg: String) {
        showToast(msg)
//        showToast(context.getString(R.string.error_api_failure))
    }
}