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

class LoanInfoPostPresenter(private val PostLoanInfo: LoanApplicationConnector.PostLoanInfo) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_LOAN_INFO_POST) {
            callLoanInfoPostApi()
        }
    }

    private fun callLoanInfoPostApi() {
        val requestApi = apiProject.api.postLoanInfo(PostLoanInfo.loanInfoRequestPost)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> PostLoanInfo.showProgressDialog() }
                .doFinally { PostLoanInfo.hideProgressDialog() }
                .subscribe({ response -> onLoanInfoPost(response) },
                        { e -> PostLoanInfo.getLoanInfoPostFailure(e?.message ?: "") })
    }

    private fun onLoanInfoPost(responsePost: Response.ResponsePostLoanInfo) {
        if (responsePost.responseCode == "201") {
            PostLoanInfo.getLoanInfoPostSuccess(responsePost)

        } else {
            PostLoanInfo.getLoanInfoPostFailure(responsePost.responseMsg)
        }
    }
}
