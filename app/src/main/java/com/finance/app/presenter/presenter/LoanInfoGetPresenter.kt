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

class LoanInfoGetPresenter(private val getLoanInfo: LoanApplicationConnector.GetLoanInfo) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_LOAN_INFO_GET) {
            callLoanInfoGetApi()
        }
    }

    private fun callLoanInfoGetApi() {
        val requestApi = apiProject.api.getLoanInfo(getLoanInfo.leadId)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> getLoanInfo.showProgressDialog() }
                .doFinally { getLoanInfo.hideProgressDialog() }
                .subscribe({ response -> onLoanInfoGet(response) },
                        { e -> getLoanInfo.getLoanInfoGetFailure(e?.message ?: "") })
    }

    private fun onLoanInfoGet(responsePost: Response.ResponseGetLoanInfo) {
        if (responsePost.responseCode == "200") {
            getLoanInfo.getLoanInfoGetSuccess(responsePost)
        } else {
            getLoanInfo.getLoanInfoGetFailure(responsePost.responseMsg)
        }
    }
}
