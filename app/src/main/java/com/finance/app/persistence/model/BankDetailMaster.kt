package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import motobeans.architecture.retrofit.response.Response
import java.io.Serializable

@Entity
class BankDetailMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: Response.BankDetailList? = null
    var storageType: String = "EMPLOYMENT"
    var editable: Boolean? = null}
