package com.finance.app.persistence.model

import java.io.Serializable
import java.text.DecimalFormat

class BankDetailBean : Serializable {
    var accountHolderName: String? = ""
    var accountNumber: String? = ""
    var accountTypeDetailID: Int? = null
    var accountTypeName: String? = ""
    var active: Boolean = true
    var applicantBankTransectionList: Any? = null
    var applicantID: Int? = 0
    var bankDetailID: Int? = null
    var bankName: String? = ""
    var bankNameTypeDetailID: Int? = null
    var bankStatementProof: String? = ""
    var firstName: String? = ""
    var loanApplicationID: Int? = 0
    var numberOfCredit: String? = ""
    var numberOfDebit: Int? = 0
    var salaryCreditTypeDetailID: Int? = null
    var transection: Int? = null
}
