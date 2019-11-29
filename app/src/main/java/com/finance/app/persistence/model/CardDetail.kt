package com.finance.app.persistence.model
import java.io.Serializable

class CardDetail:Serializable {
    var active: Boolean =true
    var applicantID: Int?=0
    var bankNameTypeDetailID: Int?=0
    var cardLimit: Int?=0
    var createdBy: Int?=0
    var createdOn: String? =""
    var creditCardDetailID: Int?=0
    var currentUtilization: Int?=0
    var lastModifiedBy: Int?=0
    var lastModifiedOn: String?=""
    var lastPaymentDate: String?=""
    var loanApplicationID: Int?=0
    var obligateTypeDetail: Int?=0
}