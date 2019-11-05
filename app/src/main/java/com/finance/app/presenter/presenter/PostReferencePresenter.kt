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

class PostReferencePresenter(private val referenceDetail: LoanApplicationConnector.PostReference) : LoanApplicationConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_UPDATE_REFERENCE) {
            callUpdateReferenceApi()
        }
    }

    private fun callUpdateReferenceApi() {
        val requestApi = apiProject.api.postReference(referenceDetail.leadId, referenceDetail.requestPostReference)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { referenceDetail.showProgressDialog() }
                .doFinally { referenceDetail.hideProgressDialog() }
                .subscribe({ response -> onUpdateReference(response) },
                        { e -> referenceDetail.getReferencePostFailure(e?.message ?: "") })
    }

    private fun onUpdateReference(response: Response.ResponseLoanApplication) {
        if (response.responseCode == "200") {
            referenceDetail.getReferencePostSuccess(response)
        } else {
            referenceDetail.getReferencePostFailure(response.responseMsg)
        }
    }
}
