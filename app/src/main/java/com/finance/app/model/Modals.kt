package com.finance.app.model

import android.graphics.Bitmap

class Modals {
    data class NavItems(val images: Int, val title: String)
    data class Spinner(val value: SpinnerValue)
    data class SpinnerValue(val name: String, val id: Int)
    data class AddKyc(val idType: String, val idNum: String, val kycImage: Bitmap?, val issueDate: String, val expiryDate: String, val verifiedStatus: String)

}
