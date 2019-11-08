package com.finance.app.presenter.connector

import com.finance.app.persistence.model.BankDetailMaster
import com.finance.app.persistence.model.LoanInfoMaster
import com.finance.app.persistence.model.PersonalInfoMaster
import com.finance.app.persistence.model.ReferenceMaster
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response

interface LoanApplicationConnector {

    interface PostLoanInfo : ReusableView {
        val loanInfoRequestPost: LoanInfoMaster
        fun getLoanInfoPostSuccess(value: Response.ResponseLoanApplication)
        fun getLoanInfoPostFailure(msg: String)
    }

    interface GetLoanInfo : ReusableView {
        val leadId: String
        fun getLoanInfoGetSuccess(value: Response.ResponseGetLoanInfo)
        fun getLoanInfoGetFailure(msg: String)
    }

    interface PostPersonalInfo : ReusableView {
        val personalInfoRequestPost: PersonalInfoMaster
        fun getPersonalPostInfoSuccess(value: Response.ResponseLoanApplication)
        fun getPersonalPostInfoFailure(msg: String)
    }

    interface GetPersonalInfo : ReusableView {
        val leadId: String
        fun getPersonalGetInfoSuccess(value: Response.ResponseGetPersonalInfo)
        fun getPersonalGetInfoFailure(msg: String)
    }

    interface PostEmployment : ReusableView {
        val employmentRequestPost: Requests.RequestPostEmployment
        fun getEmploymentPostSuccess(value: Response.ResponseLoanApplication)
        fun getEmploymentPostFailure(msg: String)
    }

    interface GetEmployment : ReusableView {
        val leadId: String
        fun getEmploymentGetSuccess(value: Response.ResponseGetEmployment)
        fun getEmploymentGetFailure(msg: String)
    }

    interface PostBankDetail : ReusableView {
        val bankDetailRequest: BankDetailMaster
        fun getBankDetailPostSuccess(value: Response.ResponseLoanApplication)
        fun getBankDetailPostFailure(msg: String)
    }

    interface GetBankDetail : ReusableView {
        val leadId: String
        fun getBankDetailGetSuccess(value: Response.ResponseGetBankDetail)
        fun getBankDetailGetFailure(msg: String)
    }

    interface PostAssetLiability : ReusableView {
        val leadId: String
        val requestAssetLiability: ArrayList<ReferenceMaster>
        fun getAssetLiabilityPostSuccess(value: Response.ResponseLoanApplication)
        fun getAssetLiabilityPostFailure(msg: String)
    }

    interface GetAssetLiability : ReusableView {
        val leadId: String
        fun getAssetLiabilityGetSuccess(value: Response.ResponseLoanApplication)
        fun getAssetLiabilityGetFailure(msg: String)
    }

    interface PostReference : ReusableView {
        val leadId: String
        val requestPostReference: ArrayList<ReferenceMaster>
        fun getReferencePostSuccess(value: Response.ResponseLoanApplication)
        fun getReferencePostFailure(msg: String)
    }

    interface GetReference : ReusableView {
        val leadId: String
        fun getReferenceGetSuccess(value: Response.ResponseGetReference)
        fun getReferenceGetFailure(msg: String)
    }

    interface PresenterOpt : ReusableNetworkConnector
}