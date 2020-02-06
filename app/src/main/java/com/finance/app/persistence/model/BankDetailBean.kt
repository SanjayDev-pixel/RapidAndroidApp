package com.finance.app.persistence.model

import java.io.Serializable
import java.text.DecimalFormat

class BankDetailBean : Serializable {
    var accountHolderName: String? = ""
    var accountNumber: String? = ""
    var accountTypeDetailID: Int? = 0
    var accountTypeName: String? = ""
    var active: Boolean = true
    var applicantBankTransectionList: Any? = null
    var applicantID: Int? = 0
    var averageBankBalance: DecimalFormat? = null
    var averageDebitBalance: DecimalFormat? = null
    var bankDetailID: Int? = null
    var bankName: String? = ""
    var bankNameTypeDetailID: Int? = 0
    var bankStatementProof: String? = ""
    var bulkEntryAmount: DecimalFormat? = null
    var firstName: String? = ""
    var inwardChequeReturn: DecimalFormat? = null
    var loanApplicationID: Int? = 0
    var netMonthlyCredit: DecimalFormat? = null
    var numberOfCredit: String? = ""
    var numberOfDebit: Int? = 0
    var outwardChequeReturn: DecimalFormat? = null
    var salaryCreditTypeDetailID: Int? = 0
    var transection: Int? = null
}
