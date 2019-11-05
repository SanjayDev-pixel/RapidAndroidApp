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

class EmploymentGetPresenter(private val employment: LoanApplicationConnector.GetEmployment) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_GET_EMPLOYMENT) {
            callGetEmploymentApi()
        }
    }

    private fun callGetEmploymentApi() {
        val requestApi = apiProject.api.getEmployment(employment.leadId)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { employment.showProgressDialog() }
                .doFinally { employment.hideProgressDialog() }
                .subscribe({ response -> onGetEmployment(response) },
                        { e -> employment.getEmploymentGetFailure(e?.message ?: "") })
    }

    private fun onGetEmployment(response: Response.ResponseGetEmployment) {
        if (response.responseCode == "200") {
            employment.getEmploymentGetSuccess(response)
        } else {
            employment.getEmploymentGetFailure(response.responseMsg)
        }
    }
}
