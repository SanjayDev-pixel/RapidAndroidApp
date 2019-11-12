package com.finance.app.persistence.model

import motobeans.architecture.retrofit.request.Requests

class EmploymentApplicantsModel {
    var addressBean: AddressDetail?=null
    var allEarningMembers: Int? = null
    var applicantID: Int?= null
    var businessSetupTypeDetailID: Int? = null
    var businessVinatgeInYear: Int? = null
    var companyName: String? = null
    var isMainApplicant = false
    var constitutionTypeDetailID: Int? = null
    var dateOfIncorporation: String? = null
    var dateOfJoining: String? = null
    var documents: ArrayList<Requests.Document>? = null
    var employeeID: String? = null
    var employmentTypeDetailID: Int? = null
    var gstRegistration: String? = null
    var industryTypeDetailID: Int? = null
    var loanApplicationID: Int? = null
    var occupationDetailID: Int? = null
    var occupationalDetailID: Int? = null
    var officialMailID: String? = null
    var leadApplicantNumber: Int? = null
    var profileSegmentTypeDetailID: Int? = null
    var retirementAge: Int? = null
    var sectorTypeDetailID: Int? = null
    var subProfileTypeDetailID: Int? = null
    var totalExperience: String? = null
    var designation: String? = null
}
