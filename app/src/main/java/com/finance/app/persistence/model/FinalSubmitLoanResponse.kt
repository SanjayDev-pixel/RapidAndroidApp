package com.finance.app.persistence.model

import HfcPolicyResponse
import com.google.gson.annotations.SerializedName

data class FinalSubmitLoanResponse (

        @SerializedName("hfcPolicyResponse") val hfcPolicyResponse : HfcPolicyResponse,
        @SerializedName("action") val action : Boolean
)