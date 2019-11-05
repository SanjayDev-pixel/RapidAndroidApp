package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.LoanApplicationConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class PersonalInfoGetPresenter(private val personalinfo: LoanApplicationConnector.GetPersonalInfo) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_GET_PERSONAL_INFO) {
            callPersonalInfoGetApi()
        }
    }

    private fun callPersonalInfoGetApi() {
        val requestApi = apiProject.api.getPersonalInfo(personalinfo.leadId)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> personalinfo.showProgressDialog() }
                .doFinally { personalinfo.hideProgressDialog() }
                .subscribe({ response -> onGetPersonalInfo(response) },
                        { e -> personalinfo.getPersonalGetInfoFailure(e?.message ?: "") })
    }

    private fun onGetPersonalInfo(response: Response.ResponseGetPersonalInfo) {
        if (response.responseCode == "200") {
            personalinfo.getPersonalGetInfoSuccess(response)
        } else {
            personalinfo.getPersonalGetInfoFailure(response.responseMsg)
        }
    }
}
