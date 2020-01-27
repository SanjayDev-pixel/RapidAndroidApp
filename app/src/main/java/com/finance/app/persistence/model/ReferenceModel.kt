package com.finance.app.persistence.model

import java.io.Serializable

class ReferenceModel:Serializable {

    var addressBean: ReferenceAddressDetail? = ReferenceAddressDetail()
    var applicantReferenceDetailID: Int? = null
    var contactNumber: String? = ""
    var name: String? = ""
    var knowSince: String? = ""
    var address: String? = ""
    var serialNumber: Int = 0
    var applicantID: Int? = null
    var relationTypeDetailID: Int? = 0
    var occupationTypeDetailID: Int? = 0
    var active: Boolean? = true

}