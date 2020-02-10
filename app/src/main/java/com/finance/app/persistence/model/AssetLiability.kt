package com.finance.app.persistence.model

import java.io.Serializable

class AssetLiability : Serializable {

    var ownershipTypeDetailID: Int? = null
    var assetValue: Int? = 0
    var assetDetailsID: Int? = 0
    var assetDetailsTypeDetailID: Int? = null
    var subTypeOfAssetTypeDetailID: Int? = null
    var documentedProofTypeDetailID: Int? = null
}