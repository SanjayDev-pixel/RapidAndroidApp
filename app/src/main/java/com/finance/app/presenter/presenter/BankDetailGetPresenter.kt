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

class BankDetailGetPresenter(private val getBankDetail: LoanApplicationConnector.GetBankDetail) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_BANK_DETAIL_GET) {
                callBankDetailGetApi()
        }
    }

    private fun callBankDetailGetApi() {
        val requestApi = apiProject.api.getBankDetail(getBankDetail.leadId)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { getBankDetail.showProgressDialog() }
                .doFinally { getBankDetail.hideProgressDialog() }
                .subscribe({ response -> onBankDetailGet(response) },
                        { e -> getBankDetail.getBankDetailGetFailure(e?.message ?: "") })
    }

    private fun onBankDetailGet(response: Response.ResponseGetBankDetail) {
        if (response.responseCode == "200") {
            getBankDetail.getBankDetailGetSuccess(response)
        } else {
            getBankDetail.getBankDetailGetFailure(response.responseMsg)
        }
    }
}