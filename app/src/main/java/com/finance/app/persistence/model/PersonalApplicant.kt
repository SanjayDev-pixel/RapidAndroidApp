package com.finance.app.persistence.model

data class PersonalApplicants(
        val addressDetailList: ArrayList<AddressDetail>,
        val age: Int = 25,
        val applicantID: Int = 123556,
        val casteTypeDetailID: Int = 32158,
        val contactDetail: ContactDetail = ContactDetail(),
        val dateOfBirth: String = "",
        val detailQualificationTypeDetailID: Int = 123,
        val dobProofImagePath: String = "",
        val dobProofTypeDetailID: Int = 324,
        val entityID: Int = 32184,
        val fatherFirstName: String = "",
        val fatherLastName: String = "",
        val fatherMiddleName: String = "",
        val firstName: String = " ",
        val genderTypeDetailID: Int = 32151,
        val isMainApplicant: Boolean = true,
        val lastName: String = "",
        val loanApplicationID: Int = 32213,
        val mainApplicant: Boolean = false,
        val maritialStatusTypeDetailID: Int = 13251,
        val middleName: String = "",
        val motherFirstName: String = "",
        val motherLastName: String = "",
        val motherMiddleName: String = " ",
        val nationalityTypeDetailID: Int = 2315,
        val numberOfDependents: Int = 1,
        val numberOfEarningMembers: Int = 20,
        val numberOfFamilyMembersChildren: Int = 2,
        val numberOfFamilyMembersOthers: Int = 5,
        val presentAccommodationTypeDetailID: Int = 12313,
        val qualificationTypeDetailID: Int = 513218,
        val religionTypeDetailID: Int = 231156,
        val spouseFirstName: String = " ",
        val spouseLastName: String = "",
        val spouseMiddleName: String = ""
)

data class AddressDetail(
        val address1: String = "",
        val address2: String = "",
        val addressID: String = "",
        val addressProof: String = "",
        val addressTypeDetailID: Any = 132131654,
        val cityID: Any = 215658,
        val cityName: Any = "",
        val districtID: Any = 321,
        val entityID: Any = 12,
        val landmark: String = "",
        val rentAmount: Any = 100000,
        val residenceTypeTypeDetailID: Any = 564,
        val zip: Any = 132554
)

data class ContactDetail(
        val contactID: Int = 21365,
        val contactTypeDetailID: Any = 16113,
        val email: String = "",
        val enitiyID: Any = 3215,
        val fax: Any = 12452,
        val mobile: Any = 984161123
)