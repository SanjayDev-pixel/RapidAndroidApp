package com.finance.app.model

import android.graphics.Bitmap
import com.finance.app.persistence.model.PersonalApplicantsModel

class Modals {
    data class NavItems(val image: Int, val title: String)
    data class AddKyc(val idType: String, val idNum: String, val kycImage: Bitmap?, val issueDate: String, val expiryDate: String, val verifiedStatus: String)
    data class DropDownMaster(var typeMasterID: Int, var typeDetailID: Int, var refTypeDetailID: Int, var typeMasterName: String, var typeMasterDisplayText: String, var typeMasterLogicalCode: String,
                              var typeDetailCode: String, var typeDetailLogicalCode: String, var sequence: String, var typeDetailDescription: String)

    data class ApplicantPersonal(var personalApplicants: ArrayList<PersonalApplicantsModel>)

    data class BankApplicants(var companyName: String)

    data class EmploymentApplicants(var companyName: String)

    data class AssetLiabilityApplicants(var companyName: String)
}
