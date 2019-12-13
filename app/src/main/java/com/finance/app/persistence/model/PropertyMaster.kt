package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import motobeans.architecture.retrofit.response.Response
import java.io.Serializable

@Entity
class PropertyMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: PropertyModel? = PropertyModel()
    var storageType: String = "APPLICATION_PROPERTY"
    var editable: Boolean? = null
}
