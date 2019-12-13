package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class ReferenceMaster:Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: ReferencesList = ReferencesList()
    var storageType: String = "APPLICATION_REFERENCE"
    var editable: Boolean? = null
}
