package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.PinCodeDetailConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class PinCodeDetailPresenter(private val pinCode: PinCodeDetailConnector.PinCode) : PinCodeDetailConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_PIN_CODE_DETAIL) {
            callPinCodeDetailApi()
        }
    }

    private fun callPinCodeDetailApi() {
        val requestApi = apiProject.api.getPinCodeDetail(pinCode.pinCode)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { pinCode.showProgressDialog() }
                .doFinally { pinCode.hideProgressDialog() }
                .subscribe({ response -> onPinCode(response) },
                        { e -> pinCode.getPinCodeFailure(e?.message ?: "") })
    }

    private fun onPinCode(response: Response.ResponsePinCodeDetail) {
        if (response.responseCode == "200") {
            pinCode.getPinCodeSuccess(response)
        } else {
            pinCode.getPinCodeFailure(response.responseMsg)
        }
    }
}