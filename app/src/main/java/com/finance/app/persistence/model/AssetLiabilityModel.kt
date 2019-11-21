package com.finance.app.persistence.model

import java.io.Serializable

class AssetLiabilityModel : Serializable {

    var applicantAssetLiabilityList: ArrayList<AssetLiability>? = null
    var applicantCreditCardDetailList: ArrayList<CardDetail>? = null
    var applicantExistingObligationList: ArrayList<ObligationDetail>? = null
}