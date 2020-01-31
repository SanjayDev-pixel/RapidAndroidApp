package com.finance.app.persistence.model

import java.io.Serializable

class EmploymentApplicantsModel:Serializable {
    var addressBean: AddressDetail? = null
    var allEarningMembers = false
    var applicantID: Int? = null
    var businessSetupTypeDetailID: Int? = null
    var businessVinatgeInYear: Int? = null
    var companyName: String? = null
    var isMainApplicant = false
    var constitutionTypeDetailID: Int? = null
    var dateOfIncorporation: String? = null
    var dateOfJoining: String? = null
    var employeeID: String? = null
    var employmentTypeDetailID: Int? = null
    var gstRegistration: String? = null
    var industryTypeDetailID: Int? = null
    var loanApplicationID: Int? = null
    var occupationDetailID: Int? = null
    var occupationalDetailID: Int? = null
    var officialMailID: String? = null
    var leadApplicantNumber: String? = null
    var incomeConsidered: Boolean = false
    var isPensioner: Boolean = false
    var profileSegmentTypeDetailID: Int? = null
    var retirementAge: Int? = null
    var sectorTypeDetailID: Int? = null
    var subProfileTypeDetailID: Int? = null
    var totalExperience: String? = null
    var designation: String? = null
    var incomeDetail: IncomeDetail? = null
}
