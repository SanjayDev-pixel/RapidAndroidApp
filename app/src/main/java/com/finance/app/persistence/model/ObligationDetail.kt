package com.finance.app.persistence.model

import java.io.Serializable

class ObligationDetail : Serializable {
    var loanAmount: Int? = null
    var tenure: Int? = null
    var balanceTenure: Int? = null
    var emiAmount: Int? = null
    var numberOfBouncesInLastSixMonth: Int? = null
    var numberOfBouncesInLastNineMonth: Int? = null
    var financerName: String? = ""
    var loanAccountNumber: String? = null
    var borrowerNameTypeDetailID: Int? = 0
    var bounseEmiPaidInSameMonth: Int? = null
    var loanOwnershipTypeDetailID: Int? = 0
    var obligateTypeDetailID: Int? = 0
    var loanTypeTypeDetailID: Int? = 0
    var repaymentBankTypeDetailID: Int? = 0
}