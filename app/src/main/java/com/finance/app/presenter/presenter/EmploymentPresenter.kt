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

class EmploymentPresenter(private val employment: LoanApplicationConnector.Employment) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_EMPLOYMENT) {
            callEmploymentApi()
        }
    }

    private fun callEmploymentApi() {
        val requestApi = apiProject.api.saveEmployment(employment.employmentRequest)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { employment.showProgressDialog() }
                .doFinally { employment.hideProgressDialog() }
                .subscribe({ response -> onEmployment(response) },
                        { e -> employment.getEmploymentFailure(e?.message ?: "") })
    }

    private fun onEmployment(response: Response.ResponseEmployment) {
        if (response.responseCode == "200") {
            employment.getEmploymentSuccess(response)
        } else {
            employment.getEmploymentFailure(response.responseMsg)
        }
    }
}
