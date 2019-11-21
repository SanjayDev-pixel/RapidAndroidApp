package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class AssetLiabilityMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: AssetLiabilityModel? = AssetLiabilityModel()
    var storageType: String = "LIABILITY_AND_ASSET"
    var editable: Boolean? = null
}
