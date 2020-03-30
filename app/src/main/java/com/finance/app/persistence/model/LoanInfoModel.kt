package com.finance.app.persistence.model

import java.io.Serializable

class LoanInfoModel:Serializable {
    var affordableEMI: Double? = 0.0
    var applicationNumber: String? = null
    var channelPartnerNameTypeDetailID: Int? = null
    var channelPartnerDsaID: Int? = null
    var creditOfficerEmpID: Int? = null
    var creditOfficerEmpName: String? = ""
    var interestTypeTypeDetailID: Int? = null
    var leadID: Int? = 0
    var loanAmountRequest: Int = 0
    var loanApplicationID: Int? = null
    var loanPurposeID: Int? = null
    var loanSchemeTypeDetailID: Int? = null
    var logginUserEntityID: Int? = null
    var productID: Int? = null
    var ruleEngineResponse: String? = ""
    var salesOfficerEmpID: Int? = null
    var salesOfficerEmpName: String? = ""
    var sourcingChannelPartnerTypeDetailID: Int? = null
    var tenure: Int? = 0
    var processingFeePercentage: Double? = null
    var isPropertySelected: Boolean? = false
    var channelPartnerName: String?= null
}
