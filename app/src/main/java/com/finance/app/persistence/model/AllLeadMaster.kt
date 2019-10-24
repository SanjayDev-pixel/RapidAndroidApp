package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
class AllLeadMaster : Serializable {

    @PrimaryKey
    var key: String = UUID.randomUUID().toString()
    var actualCompletionDate: String? = null
    var applicantAddress: String? = ""
    var applicantAlternativeContactNumber: String? = null
    var applicantContactNumber: String? = ""
    var applicantEmail: String? = ""
    var applicantFirstName: String? = ""
    var applicantLastName: String? = ""
    var applicantMiddleName: String? = ""
    var branchID: String? = null
    var createdOn: String? = ""
    var currentStatus: String? = ""
    var dsaID: String? = null
    var estimatedCompletionDate: String? = null
    var isConvertedToLoanApplication = false
    var lastModifiedOn: Long? = 0
    var leadFailedReason: String? = ""
    var leadID: Int? = null
    var leadNumber: String? = ""
    var leadOwnershipEntityID: String? = null
    var leadReference: String? = null
    var leadSourceTypeDetailID: Int? = null
    var loanApplicationID: Int? = null
    var loanProductID: Int? = null
    var loanProductName: String? = ""
    var remarks: String? = ""
    var status: String? = ""
}