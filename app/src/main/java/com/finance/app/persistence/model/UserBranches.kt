package com.finance.app.persistence.model

import java.io.Serializable

class UserBranches : Serializable {
    var branchName: String? = ""
    var branchID: Int = 0

    override fun toString(): String {
        return "$branchName"
    }
}