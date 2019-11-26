package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import motobeans.architecture.retrofit.response.Response
import java.io.Serializable
import java.util.*

@Entity
class LoanProductMaster : Serializable {

    @PrimaryKey
    var productID: Int = 0
    var fromDate: String? = null
    var loanPurposeList: ArrayList<Response.LoanPurpose>? = null
    var maxAmount: Int = 0
    var maxTenure: Int = 0
    var minAmount: Int = 0
    var minTenure: Int = 0
    var productName: String? = null
    var toDate: String? = null
}