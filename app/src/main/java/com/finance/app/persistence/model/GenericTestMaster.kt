package com.finance.app.persistence.model

import androidx.room.PrimaryKey

open class GenericTestMaster<T> {

    @PrimaryKey
    open var leadID: Int = 0
    open var loanApplicationDraftDetailID: Int? = null
    open var draftData: T? = null
    open var storageType: String = ""
    open var editable: Boolean? = false
}