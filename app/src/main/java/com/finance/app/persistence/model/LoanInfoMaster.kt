package com.finance.app.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finance.app.others.AppEnums
import java.io.Serializable

@Entity
class LoanInfoMaster : Serializable {

    @PrimaryKey
    var leadID: Int = 0
    var loanApplicationDraftDetailID: Int? = null
    var draftData: LoanInfoModel? = null
    var storageType: String = AppEnums.FormType.LOANINFO.type
    var editable: Boolean? = false

}