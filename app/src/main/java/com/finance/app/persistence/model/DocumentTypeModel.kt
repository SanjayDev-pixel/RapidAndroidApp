package com.finance.app.persistence.model

import java.io.Serializable

class DocumentTypeModel : Serializable {
    var documentID: Int? = null
    var documentName: String? = null
    var uploadedDocumentPath: String? = null
    var documentCodeTypeDetailID: Int? = null
    var documentCodeTypeDetail: String? = null
    var documentSubTypeDetailID: Int? = null
    var documentSubTypeDetail: String? = null
    var documentSubTypeDetailDisplayText: String? = null

    override fun toString(): String {
        return documentSubTypeDetailDisplayText.toString()
    }
}