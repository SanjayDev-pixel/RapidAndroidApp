package com.finance.app.presenter.connector
import motobeans.architecture.retrofit.response.Response

interface AllMasterValueConnector {
    interface ViewOpt : ReusableView {

        fun getAllMasterValueSuccess(value: Response.ResponseAllMasterValue)
        fun getAllSpinnerValueFailure(msg: String)
    }
    interface PresenterOpt : ReusableNetworkConnector
}