package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finance.app.others.AppEnums
import java.io.Serializable

@Entity
class ReferenceMaster:Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: ReferencesList = ReferencesList()
    var storageType: String = AppEnums.FormType.REFERENCE.type
    var editable: Boolean? = null
}
