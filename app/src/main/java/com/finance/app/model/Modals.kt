package com.finance.app.model

import android.graphics.Bitmap

class Modals {
    data class NavItems(val image: Int, val title: String)
    data class AddKyc(val idType: String, val idNum: String, val kycImage: Bitmap?, val issueDate: String, val expiryDate: String, val verifiedStatus: String)
    data class DropDownMaster(var typeMasterID: Int, var typeDetailID: Int, var refTypeDetailID: Int, var typeMasterName: String, var typeMasterDisplayText: String, var typeMasterLogicalCode: String,
                              var typeDetailCode: String, var typeDetailLogicalCode: String, var sequence: String, var typeDetailDescription: String)
}
