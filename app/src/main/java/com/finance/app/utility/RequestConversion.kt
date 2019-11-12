package com.finance.app.utility

import com.finance.app.persistence.model.EmploymentMaster
import com.finance.app.persistence.model.LoanApplicationRequest
import com.finance.app.persistence.model.LoanInfoMaster
import com.finance.app.persistence.model.PersonalInfoMaster
import com.google.gson.Gson

class RequestConversion {
    companion object {
        val gson = Gson()
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
}