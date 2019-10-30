package com.finance.app.presenter.connector

import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response

interface LoanApplicationConnector {

    interface LoanInfo : ReusableView {
        val loanInfoRequest: Requests.RequestLoanInfo
        fun getLoanInfoSuccess(value: Response.ResponseLoanInfo)
        fun getLoanInfoFailure(msg: String)
    }

    interface PersonalInfo : ReusableView {
        val personalInfoRequest: Requests.RequestPersonalInfo
        fun getPersonalInfoSuccess(value: Response.ResponseLoanApplication)
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

    interface Employment : ReusableView {
        val employmentRequest: Requests.RequestEmployment
        fun getEmploymentSuccess(value: Response.ResponseEmployment)
        fun getEmploymentFailure(msg: String)
    }

    interface BankDetail : ReusableView {
        val bankDetailRequest: Requests.RequestBankDetail
        fun getBankDetailSuccess(value: Response.ResponseBankDetail)
        fun getBankDetailFailure(msg: String)
    }

    interface PresenterOpt : ReusableNetworkConnector
}