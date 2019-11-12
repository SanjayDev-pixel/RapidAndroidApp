package com.finance.app.persistence.model

import java.io.Serializable

class LoanApplicationRequest : Serializable {

    var draftData: String = ""
    var editable: Boolean? = null
    var leadID: Int = 0
    var loanApplictionDraftDetailID: Int? = null
    var storageType: String? = null
}