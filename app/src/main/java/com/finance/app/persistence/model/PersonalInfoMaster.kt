package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finance.app.others.AppEnums
import java.io.Serializable

@Entity
class PersonalInfoMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: PersonalApplicantList = PersonalApplicantList()
    var storageType: String = AppEnums.FormType.PERSONALINFO.type
    var editable: Boolean? = null
}