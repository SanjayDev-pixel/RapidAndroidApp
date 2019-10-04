package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.response.Response

interface SourceChannelPartnerNameConnector {
    interface ViewOpt : ReusableView {
        val dsaId: String
        fun getSourceChannelPartnerNameSuccess(value: Response.ResponseSourceChannelPartnerName)
        fun getSourceChannelPartnerNameFailure(msg: String)
    }

    interface PresenterOpt : ReusableNetworkConnector
}