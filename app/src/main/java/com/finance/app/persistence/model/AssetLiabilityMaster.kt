package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class AssetLiabilityMaster : Serializable {

    @PrimaryKey
    var applicantId: Int? = 0
    var applicantAssetDetailList: ArrayList<AssetDetail>? = null
    var applicantCreditCardDetailList: ArrayList<CardDetail>? = null
    var applicantExistingObligationList: ArrayList<ObligationDetail>? = null
}