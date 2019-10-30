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

class PersonalInfoPresenter(private val personalinfo: LoanApplicationConnector.PersonalInfo) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_PERSONAL_INFO) {
            callPersonalInfoApi()
        }
    }

    private fun callPersonalInfoApi() {
        val requestApi = apiProject.api.personalInfo(personalinfo.personalInfoRequest)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> personalinfo.showProgressDialog() }
                .doFinally { personalinfo.hideProgressDialog() }
                .subscribe({ response -> onPersonalInfo(response) },
                        { e -> personalinfo.getPersonalInfoFailure(e?.message ?: "") })
    }

    private fun onPersonalInfo(response: Response.ResponseLoanApplication) {
        if (response.responseCode == "200") {
            personalinfo.getPersonalInfoSuccess(response)
        } else {
            personalinfo.getPersonalInfoFailure(response.responseMsg)
        }
    }
}
