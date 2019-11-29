package com.finance.app.persistence.model

class BankDetailModel {
    var applicantBankDetailsBean: ArrayList<BankDetailBean> = ArrayList()
    var applicantID: Int? = null
    var firstName: String? = null
    var isMainApplicant: Boolean = false
    var leadApplicantNumber: String? = null
}
