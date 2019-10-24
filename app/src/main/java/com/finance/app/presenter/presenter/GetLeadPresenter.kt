package com.finance.app.presenter.presenter

import com.finance.app.presenter.connector.GetAllLeadsConnector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.ApiProject
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class GetLeadPresenter(private val lead: GetAllLeadsConnector.ParticularLead) : GetAllLeadsConnector.PresenterOpt {

    @Inject
    lateinit var apiProject: ApiProject
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    init {
        ArchitectureApp.instance.component.inject(this)
    }

    override fun callNetwork(type: ConstantsApi) {
        if (type == ConstantsApi.CALL_GET_LEAD) {
            callGetLeadApi()
        }
    }

    private fun callGetLeadApi() {
        val requestApi = apiProject.api.getLead(leadId = lead.leadId)
        requestApi
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { lead.showProgressDialog() }
                .doFinally { lead.hideProgressDialog() }
                .subscribe({ response -> onGetLead(response) },
                        { e -> lead.getLeadFailure(e?.message ?: "") })
    }

    private fun onGetLead(response: Response.ResponseGetLead) {
        if (response.responseCode == "200") {
            lead.getLeadSuccess(response)
        } else {
            lead.getLeadFailure(response.responseMsg)
        }
    }
}