package com.finance.app.persistence.model

import java.io.Serializable

class BankDetailModel:Serializable {
    var applicantBankDetailsBean: ArrayList<BankDetailBean> = ArrayList()
    var applicantID: Int? = null
    var firstName: String? = null
    var isMainApplicant: Boolean? = null
    var leadApplicantNumber: String? = null
}
