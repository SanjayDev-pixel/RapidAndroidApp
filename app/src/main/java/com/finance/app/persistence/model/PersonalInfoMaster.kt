package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PersonalInfoMaster : Serializable, GenericTestMaster<PersonalApplicantList>() {

    @PrimaryKey
    override var leadID: Int = 0
    override var loanApplicationDraftDetailID: Int? = null
    override var draftData: PersonalApplicantList? = PersonalApplicantList()
    override var storageType: String = "APPLICANT_PERSONAL"
    override var editable: Boolean? = null
}