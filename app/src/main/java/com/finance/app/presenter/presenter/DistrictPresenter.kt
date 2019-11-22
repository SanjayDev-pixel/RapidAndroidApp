package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.DistrictCityConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class DistrictPresenter(private val district: DistrictCityConnector.District) : DistrictCityConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_DISTRICT) {
                callDistrictApi()
        }
    }

    private fun callDistrictApi() {
        val requestApi = apiProject.api.getDistricts(district.stateId)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { district.showProgressDialog() }
                .doFinally { district.hideProgressDialog() }
                .subscribe({ response -> onDistrict(response) },
                        { e -> district.getDistrictFailure(e?.message ?: "") })
    }

    private fun onDistrict(response: Response.ResponseDistrict) {
        if (response.responseCode == "200") {
            district.getDistrictSuccess(response)
        } else {
            district.getDistrictFailure(response.responseMsg)
        }
    }
}