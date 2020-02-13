package com.finance.app.persistence.model
import java.io.Serializable

class CardDetail:Serializable {
    var bankNameTypeDetailID: Int?=null
    var cardLimit: Int?=0
    var currentUtilization: Int?=0
    var lastPaymentDate: String?=""
    var obligateTypeDetail: Int?=0
}