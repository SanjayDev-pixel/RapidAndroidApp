package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.response.Response

/**
 * Created by munishkumarthakur on 31/12/17.
 */
interface AllSpinnerValueConnector {

    interface ViewOpt : ReusableView {

        fun getAllSpinnerValueSuccess(value: Response.ResponseAllSpinnerValue)
        fun getAllSpinnerValueFailure(msg: String)

    }
    interface PresenterOpt : ReusableNetworkConnector
}