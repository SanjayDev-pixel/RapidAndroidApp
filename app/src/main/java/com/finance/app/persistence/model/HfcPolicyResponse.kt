import com.google.gson.annotations.SerializedName



data class HfcPolicyResponse (

		@SerializedName("rejectionStatus") val rejectionStatus : String,
		@SerializedName("rejectionFlag") val rejectionFlag : Boolean,
		@SerializedName("deviationStatus") val deviationStatus : String,
		@SerializedName("deviationFlag") val deviationFlag : Boolean,
		@SerializedName("rejectionList") val rejectionList : List<RejectionList>,
		@SerializedName("deviationList") val deviationList : List<DeviationList>,
		@SerializedName("foir") val foir : Int,
		@SerializedName("loanEligibleAmount") val loanEligibleAmount : Int,
		@SerializedName("ltv") val ltv : Double,
		@SerializedName("ltvMutiplier") val ltvMutiplier : Int,
		@SerializedName("finalTenure") val finalTenure : Int,
		@SerializedName("finalEligibility") val finalEligibility : Double,
		@SerializedName("insrValue") val insrValue : Double,
		@SerializedName("proposedEMI") val proposedEMI : Double,
		@SerializedName("finalLTV") val finalLTV : Double,
		@SerializedName("finalFoir") val finalFoir : Double,
		@SerializedName("finalLoanOfferAmount") val finalLoanOfferAmount : Int,
		@SerializedName("finalLTV_WO_Insurance") val finalLTV_WO_Insurance : Int,
		@SerializedName("finalFoir_WO_Insurance") val finalFoir_WO_Insurance : Int,
		@SerializedName("response_reason") val response_reason : String
)