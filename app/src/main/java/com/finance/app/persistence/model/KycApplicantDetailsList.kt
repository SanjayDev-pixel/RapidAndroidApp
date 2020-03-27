package com.finance.app.persistence.model

import java.io.Serializable

class KycApplicantDetailsList: Serializable {
    var leadApplicantNumber: Int? =null
    var kycAadharZipInlineDataList: ArrayList<KycAadharZipInlineDataList> = ArrayList()
}