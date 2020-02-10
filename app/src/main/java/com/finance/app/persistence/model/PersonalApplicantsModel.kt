package com.finance.app.persistence.model

import java.io.Serializable

class PersonalApplicantsModel : Serializable {
    var addressDetailList: ArrayList<AddressDetail>? = ArrayList()
    var age: Int? = 0
    var alternateContact: String? = ""
    var applicantID: Int = 0
    var casteTypeDetailID: Int? = null
    var contactDetail: ContactDetail? = ContactDetail()
    var applicantKycList: ArrayList<KYCDetail>? = ArrayList()
    var dateOfBirth: String? = ""
    var leadApplicantNumber: String? = ""
    var detailQualificationTypeDetailID: Int? = null
    var dobProofImagePath: String? = ""
    var dobProofTypeDetailID: Int? = null
    var entityID: Int? = 0
    var fatherFirstName: String? = ""
    var fatherLastName: String? = ""
    var fatherMiddleName: String? = ""
    var firstName: String? = ""
    var genderTypeDetailID: Int? = null
    var isMainApplicant: Boolean = false
    var lastName: String? = ""
    var loanApplicationID: Int? = 0
    var maritialStatusTypeDetailID: Int? = null
    var middleName: String? = ""
    var motherFirstName: String? = ""
    var motherLastName: String? = ""
    var motherMiddleName: String? = ""
    var nationalityTypeDetailID: Int? = null
    var numberOfDependents: Int? = 0
    var numberOfEarningMembers: Int? = 0
    var numberOfFamilyMembersChildren: Int? = 0
    var numberOfFamilyMembersOthers: Int? = 0
    var presentAccommodationTypeDetailID: Int? = null
    var qualificationTypeDetailID: Int? = null
    var religionTypeDetailID: Int? = null
    var relationshipTypeDetailId: Int? = null
    var spouseFirstName: String? = ""
    var spouseLastName: String? = ""
    var spouseMiddleName: String? = ""
    var incomeConsidered: Boolean = false
    var livingStandardTypeDetailId: Int? = null
}
