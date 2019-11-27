package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.response.Response

interface PropertyNatureConnector {

    interface PropertyNature : ReusableView {
        val ownershipId: String
        val transactionId: String

        fun getPropertyNatureSuccess(value: Response.ResponsePropertyNature)
        fun getPropertyNatureFailure(msg: String)

    }

    interface PresenterOpt : ReusableNetworkConnector
}