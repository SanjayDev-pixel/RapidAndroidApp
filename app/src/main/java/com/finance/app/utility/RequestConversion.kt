package com.finance.app.utility

import com.finance.app.persistence.model.*
import com.google.gson.Gson

class RequestConversion {

    fun <Vish> getRequest(master: Vish): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        master as LoanInfoMaster
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun loanInfoRequest(master: LoanInfoMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun personalInfoRequest(master: PersonalInfoMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun employmentRequest(master: EmploymentMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun bankRequest(master: BankDetailMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun referenceRequest(master: ReferenceMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun propertyRequest(master: PropertyMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }

    fun assetLiabilityRequest(master: AssetLiabilityMaster): LoanApplicationRequest {
        val request = LoanApplicationRequest()
        request.draftData = Gson().toJson(master.draftData)
        request.leadID = master.leadID
        request.storageType = master.storageType
        return request
    }
}