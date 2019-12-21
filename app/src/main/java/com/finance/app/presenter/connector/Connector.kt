package com.finance.app.presenter.connector

/**
 * Created by munishkumarthakur on 31/12/17.
 */
class Connector {

    interface ViewOpt<Request, Response> : ReusableView {
        val apiRequest: Request

        fun getApiSuccess(value: Response)
    }

    interface PresenterOpt
}