package com.finance.app.persistence.model

import java.io.Serializable

class PropertyModel:Serializable {
    var leadID: Int? = null
    var applicantContactNumber: String? = ""
    var branchID: String? = null
    var leadNumber: String? = ""
    var loanApplicationID: Int? = null
    var isFirstProperty = false
    var distanceFromBranch: String = ""
    var landmark: String = ""
    var distanceFromExistingResidence: String = ""
    var propertyAreaSquareFt: Int = 0
    var mvOfProperty: String = ""
    var stateID: Int? = 0
    var cityID: Int? = 0
    var districtID: Int? = 0
    var agreementValue: Double = 0.0
    var alreadyOwnedPropertyTypeDetailID: Int? = null
    var applicationPropertyDetail: Int= 0
    var cashOCRValue: Double = 0.0
    var natureOfPropertyTransactionTypeDetailID: Int? = null
    var numberOfTenants: Int = 0
    var occupiedByTypeDetailID: Int? = null
    var ocrValue: Double = 0.0
    var ownershipTypeDetailID: Int? = 0
    var pinCode: String = ""
    var propertyAddress: String = ""
    var propertyNatureOfTransactionCategoryTypeDetailID: Int? = null
    var tenantNocAvailableTypeDetailID: Int? = null
    var unitTypeTypeDetailID: Int? = null
    var leadApplicantNumber: String = ""
    var isMainApplicant:Boolean? = true

}