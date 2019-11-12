package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PersonalInfoMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: PersonalApplicantList = PersonalApplicantList()
    var storageType: String = "APPLICANT_PERSONAL"
    var editable: Boolean? = null
}