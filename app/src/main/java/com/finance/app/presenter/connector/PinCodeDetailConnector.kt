package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.response.Response

interface PinCodeDetailConnector {

    interface PinCode : ReusableView {
        val pinCode: String

        fun getPinCodeSuccess(value: Response.ResponsePinCodeDetail)
        fun getPinCodeFailure(msg: String)

    }
    interface PresenterOpt : ReusableNetworkConnector
}