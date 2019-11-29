package com.finance.app.persistence.model

import androidx.room.PrimaryKey
import java.io.Serializable

class AssetLiability : Serializable {

    @PrimaryKey
    var financerName: String = ""
    var existingObligationID: Int? = 0
    var loanAccountNumber: String? = ""
    var loanAmount: Int? = 0
    var tenure: Int? = 0
    var balanceTenure: Int? = 0
    var emiAmount: Int? = 0
    var numberOfBouncesInLastSixMonth: Int? = 0
    var numberOfBouncesInLastNineMonth: Int? = 0
    var bounseEmiPaidInSameMonth: Int? = 0
    var createdOn: String? = ""
    var createdBy: String? = ""
    var lastModifiedOn: String? = ""
    var lastModifiedBy: String? = ""
    var borrowerNameTypeDetailID: Int? = 0
    var loanOwnershipTypeDetailID: Int? = 0
    var obligateTypeDetailID: Int? = 0
    var loanTypeTypeDetailID: Int? = 0
    var repaymentBankTypeDetailID: Int? = 0
    var applicantID: Int? = 0
    var loanApplicationID: Int? = 0
    var active: Boolean? = true
}