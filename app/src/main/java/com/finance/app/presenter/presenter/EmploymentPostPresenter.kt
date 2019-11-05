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

class EmploymentPostPresenter(private val postEmployment: LoanApplicationConnector.PostEmployment) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_POST_EMPLOYMENT) {
            callPostEmploymentApi()
        }
    }

    private fun callPostEmploymentApi() {
        val requestApi = apiProject.api.postEmployment(postEmployment.employmentRequestPost)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { postEmployment.showProgressDialog() }
                .doFinally { postEmployment.hideProgressDialog() }
                .subscribe({ response -> onEmployment(response) },
                        { e -> postEmployment.getEmploymentPostFailure(e?.message ?: "") })
    }

    private fun onEmployment(response: Response.ResponseLoanApplication) {
        if (response.responseCode == "200") {
            postEmployment.getEmploymentPostSuccess(response)
        } else {
            postEmployment.getEmploymentPostFailure(response.responseMsg)
        }
    }
}
