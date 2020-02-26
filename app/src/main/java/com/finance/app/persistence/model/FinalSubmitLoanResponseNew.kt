package com.finance.app.persistence.model

import java.io.Serializable

class FinalSubmitLoanResponseNew:Serializable {
    var responseCode : Int?=null
    var responseMsg : String=""
    var errorStack : String=""
    var timeStamp : Int?=null
    var responseObj : ResponseObj= ResponseObj()



}