package com.finance.app.persistence.model


import java.io.Serializable

class HfcPolicyResponse :Serializable {

    var rejectionStatus : String=""
    var rejectionFlag : Boolean= false
    var deviationStatus : String=""
    var deviationFlag : Boolean= false
    var rejectionList : ArrayList<RejectionList> = ArrayList()
    var deviationList : ArrayList<DeviationList> = ArrayList()
    var foir : Double?=null
    var loanEligibleAmount : Int?=null
    var ltv : Int?=null
    var ltvMutiplier : Int?=null
    var finalTenure : Int?=null
    var finalEligibility : Int?=null
    var insrvarue : Double?=null
    var proposedEMI : Double?=null
    var finalLTV : Int?=null
    var finalFoir : Double?=null
    var finalLoanOfferAmount : Int?=null
    var finalLTV_WO_Insurance : Int?=null
    var finalFoir_WO_Insurance : Double?=null
    var finalEligibleLoan_Amount_WO_Insurance : Int?=null
    var finalProposedAmount_WO_Insurance : Double?=null
}