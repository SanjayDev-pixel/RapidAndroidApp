package com.finance.app.persistence.model

import java.io.Serializable

class BankDetailList : Serializable {
    var applicantDetails: ArrayList<BankDetailModel>? = ArrayList()
}