package motobeans.architecture.retrofit.request

/**
 * Created by munishkumarthakur on 04/11/17.
 */
object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val actualCompletionDate: Any, val applicantAddress: String, val applicantAlternativeContactNumber: Any, val applicantContactNumber: String, val applicantEmail: String,
                              val applicantFirstName: String, val applicantLastName: String, val applicantMiddleName: String, val branchID: Int, val convertedToLoanApplication: Any, val currentStatus: String, val dsaID: Int,
                              val estimatedCompletionDate: Any, val leadFailedReason: Any, val leadID: Any, val leadNumber: Any, val leadOwnershipEntityID: Any, val leadReference: Any, val leadSourceTypeDetailID: Any, val loanTypeDetailID: Int, val remarks: String)
    data class RequestPersonalInfo(
            val addressDetailList: ArrayList<AddressDetail>,
            val age: Int,
            val applicantID: Int,
            val casteTypeDetailID: Int,
            val contactDetail: ContactDetail,
            val dateOfBirth: Any,
            val detailQualificationTypeDetailID: Any,
            val dobProofImagePath: Any,
            val dobProofTypeDetailID: Any,
            val entityID: Any,
            val fatherFirstName: Any,
            val fatherLastName: Any,
            val fatherMiddleName: Any,
            val firstName: String,
            val genderTypeDetailID: Any,
            val isMainApplicant: Any,
            val lastName: Any,
            val loanApplicationID: Any,
            val mainApplicant: Any,
            val maritialStatusTypeDetailID: Any,
            val middleName: Any,
            val motherFirstName: Any,
            val motherLastName: Any,
            val motherMiddleName: Any,
            val nationalityTypeDetailID: Any,
            val numberOfDependents: Any,
            val numberOfEarningMembers: Any,
            val numberOfFamilyMembersChildren: Any,
            val numberOfFamilyMembersOthers: Any,
            val presentAccommodationTypeDetailID: Any,
            val qualificationTypeDetailID: Any,
            val religionTypeDetailID: Any,
            val spouseFirstName: Any,
            val spouseLastName: Any,
            val spouseMiddleName: Any
    )

    data class AddressDetail(
            val address1: String,
            val address2: Any,
            val addressID: Any,
            val addressProof: Any,
            val addressTypeDetailID: Any,
            val cityID: Any,
            val cityName: Any,
            val districtID: Any,
            val entityID: Any,
            val landmark: Any,
            val rentAmount: Any,
            val residenceTypeTypeDetailID: Any,
            val zip: Any
    )

    data class ContactDetail(
            val contactID: Any,
            val contactTypeDetailID: Any,
            val email: String,
            val enitiyID: Any,
            val fax: Any,
            val mobile: Any
    )}