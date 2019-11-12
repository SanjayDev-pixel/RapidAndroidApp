package com.finance.app.persistence.model

class PersonalApplicantsModel {
    var addressDetailList: ArrayList<AddressDetail>? = ArrayList()
    var age: Int? = 0
    var alternateContact: String? = ""
    var applicantID: Int? = null
    var casteTypeDetailID: Int? = 0
    var contactDetail: ContactDetail? = ContactDetail()
    var dateOfBirth: String? = ""
    var leadApplicantNumber: String? = null
    var detailQualificationTypeDetailID: Int? = 0
    var dobProofImagePath: String? = ""
    var dobProofTypeDetailID: Int? = 0
    var entityID: Int? = null
    var fatherFirstName: String? = ""
    var fatherLastName: String? = ""
    var fatherMiddleName: String? = ""
    var firstName: String? = ""
    var genderTypeDetailID: Int? = 0
    var isMainApplicant = false
    var lastName: String? = ""
    var loanApplicationID: Int? = null
    var maritialStatusTypeDetailID: Int? = 0
    var middleName: String? = ""
    var motherFirstName: String? = ""
    var motherLastName: String? = ""
    var motherMiddleName: String? = ""
    var nationalityTypeDetailID: Int? = 0
    var numberOfDependents: Int? = 0
    var numberOfEarningMembers: Int? = 0
    var numberOfFamilyMembersChildren: Int? = null
    var numberOfFamilyMembersOthers: Int? = null
    var presentAccommodationTypeDetailID: Int? = 0
    var qualificationTypeDetailID: Int? = 0
    var religionTypeDetailID: Int? = 0
    var relationshipTypeDetailId: Int? = null
    var spouseFirstName: String? = " "
    var spouseLastName: String? = ""
    var spouseMiddleName: String? = ""
    var incomeConsidered: Boolean = false
    var livingStandardTypeDetailId: Int? = 0
}
