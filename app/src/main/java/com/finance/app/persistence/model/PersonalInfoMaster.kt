package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PersonalInfoMaster : Serializable {

    @PrimaryKey
    var leadID: Int? = 0
    var applicantDetails: ArrayList<PersonalApplicantsModel>? = null
    var draftDetailID: Int? = null
    var storageTypeID: Int? = null
    var userID: Int? = null
    var loanApplicationID: Int? = null
}