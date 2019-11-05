package com.finance.app.persistence.model

import java.io.Serializable

class DropdownMaster : Serializable {
    var typeDetailCode: String? = null
    var typeMasterID: Int? = null
    var typeDetailID: Int? = null
    var refTypeDetailID: Int? = null
    var typeMasterName: String? = null
    var typeMasterDisplayText: String? = null
    var typeMasterLogicalCode: String? = null
    var typeDetailLogicalCode: String? = null
    var sequence: String? = null
    var typeDetailDescription: String? = null
}