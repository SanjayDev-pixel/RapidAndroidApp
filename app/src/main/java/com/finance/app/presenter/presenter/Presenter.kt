package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.Connector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import java.util.*
import javax.inject.Inject

/**
 * Created by munishkumarthakur on 21/12/19.
 */
class Presenter{

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    fun <RequestApi, ResponseApi> callNetwork(type: ConstantsApi, dmiConnector: Connector.ViewOpt<RequestApi, ResponseApi>) {
        when(type) {
            ConstantsApi.CALL_LOGIN -> callLoginApi(dmiConnector)
        }
    }

    private fun <RequestApi, ResponseApi> callLoginApi(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>) {

        val requestNew = apiProject.api.loginUser(viewOpt.apiRequest as Requests.RequestLogin)
        var newRequest = requestNew.map { it as Objects }

        callApi(viewOpt, newRequest)
    }

    private fun <RequestApi, ResponseApi> callApi(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>, requestApi: Observable<Objects>) {
        val dispose = requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> viewOpt.showProgressDialog() }
                .doFinally { viewOpt.hideProgressDialog() }
                .subscribe({ response -> apiSuccess(viewOpt, response) },
                        { e -> apiFailure(viewOpt, e) })

    }

    private fun <RequestApi, ResponseApi> apiSuccess(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>, response: Objects?) {
        viewOpt.getApiSuccess(value = response as ResponseApi)
    }

    private fun <RequestApi, ResponseApi> apiFailure(viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>, e: Throwable?) {
        viewOpt.getApiFailure(e?.message ?: "")
    }
}