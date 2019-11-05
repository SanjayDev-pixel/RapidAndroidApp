package com.finance.app.persistence.model
import java.io.Serializable

class CardDetail:Serializable {
    var active: Boolean =true
    var applicantID: Int?=null
    var bankNameTypeDetailID: Int?=null
    var cardLimit: Int?=null
    var createdBy: Int?=null
    var createdOn: String? =null
    var creditCardDetailID: Int?=null
    var currentUtilization: Int?=null
    var lastModifiedBy: Int?=null
    var lastModifiedOn: String?=null
    var lastPaymentDate: String?=null
    var loanApplicationID: Int?=null
    var obligateTypeDetail: Int?=null
}