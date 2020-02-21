import com.google.gson.annotations.SerializedName



data class RejectionList (

		@SerializedName("rejectionReason") val rejectionReason : String,
		@SerializedName("bName") val bName : String
)