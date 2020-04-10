package com.finance.app.persistence.model

import java.io.Serializable

class PropertyModel:Serializable {
    var leadID: Int? = null
    var applicantContactNumber: String? = null
    var branchID: String? = null
    var leadNumber: String? = null
    var loanApplicationID: Int? = null
    var isFirstProperty :Boolean? = null
    var distanceFromBranch: String? = null
    var landmark: String? = null
    var distanceFromExistingResidence: String? = null
    var propertyAreaSquareFt: Int? = null
    var mvOfProperty: String? = null
    var stateID: Int? = null
    var cityID: Int? = null
    var districtID: Int? = null
    var agreementValue: Double ? = null
    var alreadyOwnedPropertyTypeDetailID: Int? = null
    var applicationPropertyDetail: Int ?= null
    var cashOCRValue: Double ? = null
    var natureOfPropertyTransactionTypeDetailID: Int? = null
    var numberOfTenants: Int ? = null
    var occupiedByTypeDetailID: Int? = null
    var ocrValue: Double ? = null
    var ownershipTypeDetailID: Int? = null
    var pinCode: String? = null
    var propertyAddress: String? = null
    var propertyNatureOfTransactionCategoryTypeDetailID: Int? = null
    var tenantNocAvailableTypeDetailID: Int? = null
    var unitTypeTypeDetailID: Int? = null
    var leadApplicantNumber: String? = null
    var isMainApplicant:Boolean? = null
    var stateName : String? = null
    var cityName : String? = null
    var districtName : String? = null
    var propertyNatureOfTransactionCategoryTypeDetailName :String? = null
    var propertyTypeDetailID: Int? = null
    var transactionTypeDetailID: Int? = null


}