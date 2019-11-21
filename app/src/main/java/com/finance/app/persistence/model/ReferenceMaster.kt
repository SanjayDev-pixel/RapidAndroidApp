package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class ReferenceMaster:Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: BankDetail? = BankDetail()
    var storageType: String = "APPLICANT_REFERENCE_DETAIL"
    var editable: Boolean? = null
}
