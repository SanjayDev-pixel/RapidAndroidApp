package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.AllMasterValueConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

/**
 * Created by munishkumarthakur on 31/12/17.
 */
class AllMasterValuePresenter(private val viewOpt: AllMasterValueConnector.ViewOpt) : AllMasterValueConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_ALL_MASTER_VALUE) {
                callAllSpinnerApi()
        }
    }

    private fun callAllSpinnerApi() {
        val requestApi = apiProject.api.getAllMasterValue()

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> viewOpt.showProgressDialog() }
                .doFinally { viewOpt.hideProgressDialog() }
                .subscribe({ response -> onAllSpinnerValue(response) },
                        { e -> viewOpt.getAllSpinnerValueFailure(e?.message ?: "") })
    }

    private fun onAllSpinnerValue(response: Response.ResponseAllMasterValue) {
        if (response.responseCode == "200") {
            viewOpt.getAllMasterValueSuccess(response)
        } else {
            viewOpt.getAllSpinnerValueFailure(response.responseMsg)
        }
    }
}