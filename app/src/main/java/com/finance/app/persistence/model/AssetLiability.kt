package com.finance.app.persistence.model

import java.io.Serializable

class AssetLiability : Serializable {

    var ownershipTypeDetailID: Int? = 0
    var assetValue: Int? = 0
    var assetDetailsID: Int? = 0
    var assetDetailsTypeDetailID: Int? = 0
    var subTypeOfAssetTypeDetailID: Int? = 0
    var documentedProofTypeDetailID: Int? = 0
}