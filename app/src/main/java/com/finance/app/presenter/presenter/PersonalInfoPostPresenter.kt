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

class PersonalInfoPostPresenter(private val personalInfo: LoanApplicationConnector.PostPersonalInfo) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_POST_PERSONAL_INFO) {
            callPersonalInfoPostApi()
        }
    }

    private fun callPersonalInfoPostApi() {
        val requestApi = apiProject.api.postPersonalInfo(personalInfo.personalInfoRequestPost)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> personalInfo.showProgressDialog() }
                .doFinally { personalInfo.hideProgressDialog() }
                .subscribe({ response -> onPostPersonalInfo(response) },
                        { e -> personalInfo.getPersonalPostInfoFailure(e?.message ?: "") })
    }

    private fun onPostPersonalInfo(response: Response.ResponseLoanApplication) {
        if (response.responseCode == "200") {
            personalInfo.getPersonalPostInfoSuccess(response)
        } else {
            personalInfo.getPersonalPostInfoFailure(response.responseMsg)
        }
    }
}
