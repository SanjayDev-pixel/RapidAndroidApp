import com.google.gson.annotations.SerializedName


data class DeviationList (

		@SerializedName("deviationCode") val deviationCode : Int,
		@SerializedName("bAppName") val bAppName : String,
		@SerializedName("head") val head : String,
		@SerializedName("deviation") val deviation : String,
		@SerializedName("multigatingFact") val multigatingFact : String,
		@SerializedName("aprvlAuth") val aprvlAuth : String,
		@SerializedName("visiable") val visiable : Int
)