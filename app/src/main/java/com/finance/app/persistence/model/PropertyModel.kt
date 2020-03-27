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
    var propertyAreaSquareFt: Int? = null
    var mvOfProperty: String = ""
    var stateID: Int? = null
    var cityID: Int? = null
    var districtID: Int? = null
    var agreementValue: Double? = null
    var alreadyOwnedPropertyTypeDetailID: Int? = null
    var applicationPropertyDetail: Int?= null
    var cashOCRValue: Double? = null
    var natureOfPropertyTransactionTypeDetailID: Int? = null
    var numberOfTenants: Int? =null
    var occupiedByTypeDetailID: Int? = null
    var ocrValue: Double? = null
    var ownershipTypeDetailID: Int? = null
    var pinCode: String = ""
    var propertyAddress: String = ""
    var propertyNatureOfTransactionCategoryTypeDetailID: Int? = null
    var tenantNocAvailableTypeDetailID: Int? = null
    var unitTypeTypeDetailID: Int? = null
    var leadApplicantNumber: String = ""
    var isMainApplicant:Boolean? = true
    var stateName : String?=""
    var cityName : String? =""
    var districtName : String? =""
    var propertyNatureOfTransactionCategoryTypeDetailName :String? =""

}