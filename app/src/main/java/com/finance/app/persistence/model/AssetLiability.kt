package com.finance.app.persistence.model

import androidx.room.PrimaryKey
import java.io.Serializable

class AssetLiability : Serializable {

    @PrimaryKey
    var financerName: String = " "
    var existingObligationID: Int? = null
    var loanAccountNumber: String? = " "
    var loanAmount: Int? = null
    var tenure: Int? = null
    var balanceTenure: Int? = null
    var emiAmount: Int? = null
    var numberOfBouncesInLastSixMonth: Int? = null
    var numberOfBouncesInLastNineMonth: Int? = null
    var bounseEmiPaidInSameMonth: Int? = null
    var createdOn: String? = null
    var createdBy: String? = null
    var lastModifiedOn: String? = null
    var lastModifiedBy: String? = null
    var borrowerNameTypeDetailID: Int? = null
    var loanOwnershipTypeDetailID: Int? = null
    var obligateTypeDetailID: Int? = null
    var loanTypeTypeDetailID: Int? = null
    var repaymentBankTypeDetailID: Int? = null
    var applicantID: Int? = null
    var loanApplicationID: Int? = null
    var active: Boolean? = true
}