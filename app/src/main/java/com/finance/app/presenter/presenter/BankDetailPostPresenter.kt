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

class BankDetailPostPresenter(private val postBankDetail: LoanApplicationConnector.PostBankDetail) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_BANK_DETAIL_POST) {
                callBankDetailPostApi()
        }
    }

    private fun callBankDetailPostApi() {
        val requestApi = apiProject.api.postBankDetail(postBankDetail.bankDetailRequest)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { postBankDetail.showProgressDialog() }
                .doFinally { postBankDetail.hideProgressDialog() }
                .subscribe({ response -> onBankDetailPost(response) },
                        { e -> postBankDetail.getBankDetailPostFailure(e?.message ?: "") })
    }

    private fun onBankDetailPost(response: Response.ResponseLoanApplication) {
        if (response.responseCode == "200") {
            postBankDetail.getBankDetailPostSuccess(response)
        } else {
            postBankDetail.getBankDetailPostFailure(response.responseMsg)
        }
    }
}