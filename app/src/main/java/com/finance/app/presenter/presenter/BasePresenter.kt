package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.IBaseConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class BasePresenter(private val dmiApiConnector: IBaseConnector) {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init { ArchitectureApp.instance.component.inject(this) }

    fun <Request> callNetwork(api: ConstantsApi, request: Request, ids: List<String>?) {
        val requestApi = when (api) {
            ConstantsApi.CALL_ADD_LEAD -> apiProject.api.addLead(request)
            ConstantsApi.CALL_ALL_MASTER_VALUE -> apiProject.api.getAllMasterValue()
            ConstantsApi.CALL_LOGIN -> {
                request as Requests.RequestLogin
                apiProject.api.loginUser(request)
            }
            else -> return
        }

        requestApi.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { dmiApiConnector.showProgressDialog() }
                .doFinally { dmiApiConnector.hideProgressDialog() }
                .subscribe { response -> onApiResponse(response, api) }
    }

    private fun <T> onApiResponse(response: T, api: ConstantsApi) {
        when (api) {
            ConstantsApi.CALL_LOGIN -> {
                response as Response.ResponseLogin
                if (checkApiResult(response, response.responseCode)) {
                    sharedPreferencesUtil.saveLoginData(response)
                }
            }
            else -> return
        }
    }

    private fun <T> checkApiResult(response: T, responseCode: String): Boolean {
        return if (responseCode == "200") {
            dmiApiConnector.getApiSuccess(response)
            true
        } else false
    }
}