package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response

interface LoanApplicationConnector {

    interface LoanInfo : ReusableView {
        val loanInfoRequest: Requests.RequestLoanInfo
        fun getLoanInfoSuccess(value: Response.ResponseLoanInfo)
        fun getLoanInfoFailure(msg: String)
    }

    interface ViewPersonalInfo : ReusableView {
//        val personalInfoRequest: Requests.RequestPersonalInfo
        fun getPersonalInfoSuccess(value: Response.ResponseSavePersonalInfo)
        fun getPersonalInfoFailure(msg: String)
    }

    interface UpdateReference : ReusableView {
        val leadId: String
        val requestUpdateReference: ArrayList<Requests.RequestUpdateReference>
        fun getUpdateReferenceSuccess(value: Response.ResponseUpdateReference)
        fun getUpdateReferenceFailure(msg: String)
    }

    interface SourceChannelPartnerName : ReusableView {
        val branchId: String
        val employeeId: String
        val channelTypeId: String
        fun getSourceChannelPartnerNameSuccess(value: Response.ResponseSourceChannelPartnerName)
        fun getSourceChannelPartnerNameFailure(msg: String)
    }

    interface PresenterOpt : ReusableNetworkConnector
}