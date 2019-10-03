package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response

/**
 * Created by munishkumarthakur on 31/12/17.
 */
interface LoanApplicationConnector {

    interface LoanInfo : ReusableView {
        val loanInfoRequest: Requests.RequestLoanInfo
        fun getLoanInfoSuccess(value: Response.ResponseLoanInfo)
        fun getLoanInfoFailure(msg: String)
    }

    interface ViewPersonalInfo : ReusableView {
//        val personalInfoRequest: Requests.RequestPersonalInfo
        fun getPersonalInfoSuccess(value: Response.ResponsePersonalInfo)
        fun getPersonalInfoFailure(msg: String)
    }

    interface PresenterOpt : ReusableNetworkConnector
}