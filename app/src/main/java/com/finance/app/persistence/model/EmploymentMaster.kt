package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import motobeans.architecture.retrofit.response.Response
import java.io.Serializable

@Entity
class EmploymentMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var draftDetailID: Int? = null
    var applicantDetails: ArrayList<Response.EmploymentApplicantDetail>? = null
    var loanApplicationID: Int? = null
    var storageTypeID: Int? = null
}