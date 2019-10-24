package com.finance.app.presenter.presenter
import com.finance.app.presenter.connector.AddLeadConnector
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.presenter.connector.LoginConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class BankDetailPresenter(private val bankDetail: LoanApplicationConnector.BankDetail) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_BANK_DETAIL) {
                callBankDetailApi()
        }
    }

    private fun callBankDetailApi() {
        val requestApi = apiProject.api.bankDetail(bankDetail.bankDetailRequest)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { bankDetail.showProgressDialog() }
                .doFinally { bankDetail.hideProgressDialog() }
                .subscribe({ response -> onBankDetail(response) },
                        { e -> bankDetail.getBankDetailFailure(e?.message ?: "") })
    }

    private fun onBankDetail(response: Response.ResponseBankDetail) {
        if (response.responseCode == "200") {
            bankDetail.getBankDetailSuccess(response)
        } else {
            bankDetail.getBankDetailFailure(response.responseMsg)
        }
    }
}