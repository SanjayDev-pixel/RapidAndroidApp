package com.finance.app.persistence.model

class PersonalApplicantsModel {
    var addressDetailList: ArrayList<AddressDetail>? = ArrayList()
    var age: Int? = 0
    var alternateContact: String? = ""
    var applicantID: Int? = 0
    var casteTypeDetailID: Int? = 0
    var contactDetail: ContactDetail? = ContactDetail()
    var dateOfBirth: String? = ""
    var leadApplicantNumber: String? = ""
    var detailQualificationTypeDetailID: Int? = 0
    var dobProofImagePath: String? = ""
    var dobProofTypeDetailID: Int? = 0
    var entityID: Int? = 0
    var fatherFirstName: String? = ""
    var fatherLastName: String? = ""
    var fatherMiddleName: String? = ""
    var firstName: String? = ""
    var genderTypeDetailID: Int? = 0
    var isMainApplicant:Boolean? = false
    var lastName: String? = ""
    var loanApplicationID: Int? = 0
    var maritialStatusTypeDetailID: Int? = 0
    var middleName: String? = ""
    var motherFirstName: String? = ""
    var motherLastName: String? = ""
    var motherMiddleName: String? = ""
    var nationalityTypeDetailID: Int? = 0
    var numberOfDependents: String? = ""
    var numberOfEarningMembers: Int? = 0
    var numberOfFamilyMembersChildren: Int? = 0
    var numberOfFamilyMembersOthers: Int? = 0
    var presentAccommodationTypeDetailID: Int? = 0
    var qualificationTypeDetailID: Int? = 0
    var religionTypeDetailID: Int? = 0
    var relationshipTypeDetailId: Int? = 0
    var spouseFirstName: String? = ""
    var spouseLastName: String? = ""
    var spouseMiddleName: String? = ""
    var incomeConsidered: Boolean? = false
    var livingStandardTypeDetailId: Int? = 0
}
