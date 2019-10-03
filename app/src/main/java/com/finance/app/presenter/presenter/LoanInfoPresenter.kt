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

class LoanInfoPresenter(private val LoanInfo: LoanApplicationConnector.LoanInfo) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_LOAN_INFO) {
            callLoanInfoApi()
        }
    }

    private fun callLoanInfoApi() {
        val requestApi = apiProject.api.loanInformation(LoanInfo.loanInfoRequest)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> LoanInfo.showProgressDialog() }
                .doFinally { LoanInfo.hideProgressDialog() }
                .subscribe({ response -> onLoanInfo(response) },
                        { e -> LoanInfo.getLoanInfoFailure(e?.message ?: "") })
    }

    private fun onLoanInfo(response: Response.ResponseLoanInfo) {
        if (response.responseCode == "201") {
            LoanInfo.getLoanInfoSuccess(response)
        } else {
            LoanInfo.getLoanInfoFailure(response.responseMsg)
        }
    }
}
