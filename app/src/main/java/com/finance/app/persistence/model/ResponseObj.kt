package com.finance.app.persistence.model

import java.io.Serializable

class ResponseObj:Serializable {

    var ruleEngineResponseID : Int? = null
    var acticoStatusTypeDetailID : Int?=null
    var deviceType : String?=null
    var eligibleLoanAmount : Int?=null
    var eligibleLoanAmountWithoutINSR : Int?=null
    var finalFOIR : Double?=null
    var finalLTV : Int?=null
    var foirNorm : Double?=null
    var foirNormvarue : Int?=null
    var foirWithoutINSR : Double?=null
    var insr : Double?=null
    var loanOfferAmount : Int?=null
    var ltvNorm : Int?=null
    var ltvNormvarue : Int?=null
    var ltvWithoutINSR : Int?=null
    var outputType : String?=null
    var proposedEmi : Double?=null
    var proposedEmiWithoutINSR : Double?=null
    var proposedTenor : Int?=null
    var ruleEngineResponse : RuleEngineResponse= RuleEngineResponse()
    var sequence : String=""
    var loanApplicationID : Int?=null
    var requestedLoanAmount : Int?=null
    var requestedTenure : String =""

}