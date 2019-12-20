package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.Connector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import javax.inject.Inject


/**
 * Created by munishkumarthakur on 31/12/17.
 */
abstract class Presenter<RequestApi, ResponseApi>(private val viewOpt: Connector.ViewOpt<RequestApi, ResponseApi>) : Connector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }


    override fun callNetwork(type: ConstantsApi) {
        when(type) {
            ConstantsApi.CALL_LOGIN -> callLoginApi()
        }
    }

    private fun callLoginApi() {
        val requestApi = apiProject.api.loginUser(viewOpt.apiRequest as Requests.RequestLogin)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> viewOpt.showProgressDialog() }
                .doFinally { viewOpt.hideProgressDialog() }
                .subscribe({ response -> viewOpt.getApiSuccess(response as ResponseApi) },
                        { e -> viewOpt.getApiFailure(e?.message ?: "") })
    }
}