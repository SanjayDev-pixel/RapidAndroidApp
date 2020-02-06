package com.finance.app.persistence.model

import java.io.Serializable

class BankDetailList : Serializable {
    var applicantBankDetails: ArrayList<BankDetailModel> = ArrayList()
}