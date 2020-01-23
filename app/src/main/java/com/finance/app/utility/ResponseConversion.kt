package com.finance.app.utility

import com.finance.app.persistence.model.*
import com.google.gson.Gson
import motobeans.architecture.retrofit.response.Response

class ResponseConversion {

    companion object {
        val gson = Gson()
    }

    fun toLoanMaster(response: Response.LoanApplicationGetObj): LoanInfoMaster {
        val master = LoanInfoMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, LoanInfoModel::class.java)
        return master
    }

    fun toPersonalMaster(response: Response.LoanApplicationGetObj): PersonalInfoMaster {
        val master = PersonalInfoMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, PersonalApplicantList::class.java)
        return master
    }

    fun toEmploymentMaster(response: Response.LoanApplicationGetObj): EmploymentMaster {
        val master = EmploymentMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, EmploymentApplicantList::class.java)
        return master
    }

    fun toBankDetailMaster(response: Response.LoanApplicationGetObj): BankDetailMaster {
        val master = BankDetailMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, BankDetailList::class.java)
        return master
    }

    fun toPropertyMaster(response: Response.LoanApplicationGetObj): PropertyMaster {
        val master = PropertyMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, PropertyModel::class.java)
        return master
    }

    fun toReferenceMaster(response: Response.LoanApplicationGetObj): ReferenceMaster {
        val master = ReferenceMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, ReferencesList::class.java)
        return master
    }

    fun toAssetLiabilityMaster(response: Response.LoanApplicationGetObj): AssetLiabilityMaster {
        val master = AssetLiabilityMaster()
        master.leadID = response.leadID
        master.draftData = gson.fromJson(response.draftData, AssetLiabilityList::class.java)
        return master
    }

}