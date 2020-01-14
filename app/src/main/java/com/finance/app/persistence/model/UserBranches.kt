package com.finance.app.persistence.model

import com.finance.app.presenter.connector.Ispinner
import java.io.Serializable

class UserBranches : Serializable, Ispinner {
    var branchName: String? = ""
    var branchID: Int = 0
    override fun getCompareValue(): String {
        return "$branchID"
    }

    override fun toString(): String {
        return "$branchName"
    }
}