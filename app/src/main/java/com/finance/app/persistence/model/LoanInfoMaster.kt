package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class LoanInfoMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var draftDetailID: Int? = null
    var loanApplicationObj: LoanInfoObj? = null
    var userID: Int? = null
    var storageType: String? = null
    var loanApplicationID:Int? =null
}