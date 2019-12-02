package com.finance.app.persistence.model

import java.io.Serializable

class ReferencesList : Serializable {
    var referenceDetails: ArrayList<ReferenceModel>? = ArrayList()
    var leadApplicantNumber: String = ""
    var isMainApplicant:Boolean? = true

}