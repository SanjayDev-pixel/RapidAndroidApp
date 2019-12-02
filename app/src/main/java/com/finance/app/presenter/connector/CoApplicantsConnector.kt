package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.response.Response

interface CoApplicantsConnector {

    interface CoApplicants : ReusableView {
        val leadId: String

        fun getCoApplicantsListSuccess(value: Response.ResponseCoApplicants)
        fun getCoApplicantsListFailure(msg: String)

    }
    interface PresenterOpt : ReusableNetworkConnector
}