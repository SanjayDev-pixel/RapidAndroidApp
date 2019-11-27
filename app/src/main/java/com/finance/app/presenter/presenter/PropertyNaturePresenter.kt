package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.PropertyNatureConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class PropertyNaturePresenter(private val propertyNature: PropertyNatureConnector.PropertyNature) : PropertyNatureConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_PROPERTY_NATURE) {
            callPropertyNatureApi()
        }
    }

    private fun callPropertyNatureApi() {
        val requestApi = apiProject.api.getPropertyNature(propertyNature.ownershipId, propertyNature.transactionId)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { propertyNature.showProgressDialog() }
                .doFinally { propertyNature.hideProgressDialog() }
                .subscribe({ response -> onPropertyNature(response) },
                        { e -> propertyNature.getPropertyNatureFailure(e?.message ?: "") })
    }

    private fun onPropertyNature(response: Response.ResponsePropertyNature) {
        if (response.responseCode == "200") {
            propertyNature.getPropertyNatureSuccess(response)
        } else {
            propertyNature.getPropertyNatureFailure(response.responseMsg)
        }
    }
}