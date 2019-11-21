package com.finance.app.persistence.model

import java.text.DecimalFormat

class BankDetailBean {
    var accountHolderName: String? = ""
    var accountNumber: Int? = null
    var accountTypeDetailID: Int? = null
    var active: Boolean = true
    var applicantBankTransectionList: Any? = null
    var applicantID: Int? = null
    var averageBankBalance: DecimalFormat? = null
    var averageDebitBalance: DecimalFormat? = null
    var bankDetailID: Int? = null
    var bankNameTypeDetailID: Int? = null
    var bankStatementProof: String? = ""
    var bulkEntryAmount: DecimalFormat? = null
    var firstName: String? = ""
    var inwardChequeReturn: DecimalFormat? = null
    var loanApplicationID: Int? = null
    var netMonthlyCredit: DecimalFormat? = null
    var numberOfCredit: Int? = null
    var numberOfDebit: Int? = null
    var outwardChequeReturn: DecimalFormat? = null
    var salaryCreditTypeDetailID: Int? = null
    var transection: Int? = null
}
