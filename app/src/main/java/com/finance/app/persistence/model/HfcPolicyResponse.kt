import java.io.Serializable


class HfcPolicyResponse : Serializable {

    var rejectionStatus: String? = null
    var rejectionFlag: Boolean = false
    var deviationStatus: String? = ""
    var deviationFlag: Boolean = false
    var rejectionList: ArrayList<RejectionList> = ArrayList()
    var deviationList: ArrayList<DeviationList> = ArrayList()
    var foir: Int? = null
    var loanEligibleAmount: Int? = null
    var ltv: Double? = null
    var ltvMutiplier: Int? = null
    var finalTenure: Int? = null
    var finalEligibility: Double? = null
    var insrvarue: Double? = null
    var proposedEMI: Double? = null
    var finalLTV: Double? = null
    var finalFoir: Double? = null
    var finalLoanOfferAmount: Int? = null
    var finalLTV_WO_Insurance: Int? = null
    var finalFoir_WO_Insurance: Int? = null
    var response_reason: String = ""

}
