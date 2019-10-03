package com.finance.app.presenter.connector
import motobeans.architecture.retrofit.response.Response

interface AllSpinnerValueConnector {
    interface ViewOpt : ReusableView {

        fun getAllSpinnerValueSuccess(value: Response.ResponseAllMasterValue)
        fun getAllSpinnerValueFailure(msg: String)
    }
    interface PresenterOpt : ReusableNetworkConnector
}