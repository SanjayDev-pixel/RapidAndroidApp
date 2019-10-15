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

class UpdateReferencePresenter(private val referenceDetail: LoanApplicationConnector.UpdateReference) : LoanApplicationConnector.PresenterOpt {

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
        val requestApi = apiProject.api.updateReference(referenceDetail.leadId, referenceDetail.requestUpdateReference)

        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { referenceDetail.showProgressDialog() }
                .doFinally { referenceDetail.hideProgressDialog() }
                .subscribe({ response -> onUpdateReference(response) },
                        { e -> referenceDetail.getUpdateReferenceFailure(e?.message ?: "") })
    }

    private fun onUpdateReference(response: Response.ResponseUpdateReference) {
        if (response.responseCode == "200") {
            referenceDetail.getUpdateReferenceSuccess(response)
        } else {
            referenceDetail.getUpdateReferenceFailure(response.responseMsg)
        }
    }
}
