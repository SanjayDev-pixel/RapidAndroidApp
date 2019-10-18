package motobeans.architecture.retrofit.request

/**
 * Created by munishkumarthakur on 04/11/17.
 */
object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val applicantAddress: String, val applicantContactNumber: String, val loanTypeDetailID: Int,
                              val applicantEmail: String, val applicantFirstName: String, val applicantLastName: String,
                              val applicantMiddleName: String, val branchID: Int)

    data class RequestLoanInfo(val draftDetailID: Int?, val leadID: Int?, val loanApplicationObj: LoanInfoObj,
                               val storageTypeID: Int?, val userID: Int?)

    data class LoanInfoObj(val affordableEMI: Int?, val channelPartnerDsaID: Int?, val creditOfficerEmpID: Int?,
                           val creditOfficerEmpName: String?, val interestTypeTypeDetailID: Int?, val leadID: Int?,
                           val loanAmountRequest: Int?, val loanApplicationID: Int?, val loanPurposeID: Int?,
                           val loanSchemeTypeDetailID: Int?, val logginUserEntityID: Int?, val productID: Int?,
                           val ruleEngineResponse: String?, val salesOfficerEmpID: Int?, val salesOfficerEmpName: String?,
                           val sourcingChannelPartnerTypeDetailID: Int?, val tenure: Int?)

    data class RequestUpdateReference(val address: String = "Address", val addressBean: AddressBean, val applicantID: Int? = null,
                                      val applicantReferenceDetailID: Int? = null, val contactNumber: String,
                                      val knowSince: String, val name: String, val occupationTypeDetailID: Int? = null,
                                      val relationTypeDetailID: Int?, val serialNumber: Int? = null)

    data class AddressBean(val address1: String, val address2: String? = null, val addressProof: Int? = null, val cityID: Int?,
                           val cityName: String, val districtID: Int?, val landmark: String, val rentAmount: Int? = null,
                           val residenceTypeTypeDetailID: Int? = null, val zip: String)

    data class RequestEmployment(val applicantDetails: ArrayList<EmploymentDetail>, val leadID: Int, val loanApplicationID: Int? = null)

    data class EmploymentDetail(val addressBean: EmploymentAddressBean, val allEarningMembers: Int? = null, val applicantID: Int?,
                                val businessSetupTypeDetailID: Int? = null, val businessVinatgeInYear: Int? = null, val companyName: String? = null,
                                val constitutionTypeDetailID: Int? = null, val dateOfIncorporation: String? = null, val dateOfJoining: String? = null,
                                val documents: ArrayList<Document>? = null, val employeeID: String? = null, val employmentTypeDetailID: Int? = null,
                                val gstRegistration: String? = null, val industryTypeDetailID: Int? = null, val loanApplicationID: Int? = null,
                                val occupationDetailID: Int? = null, val occupationalDetailID: Int? = null, val officialMailID: String? = null,
                                val profileSegmentTypeDetailID: Int? = null, val retirementAge: Int? = null, val sectorTypeDetailID: Int? = null,
                                val subProfileTypeDetailID: Int? = null, val totalExperience: String? = null, val designation: String? = null)

    data class EmploymentAddressBean(val address1: String, val address2: String? = null, val addressProof: Int? = null, val cityID: Int?,
                                     val cityName: String, val districtID: Int? = null, val landmark: String? = null, val rentAmount: Int? = null,
                                     val residenceTypeTypeDetailID: Int? = null, val zip: String? = null)

    data class Document(val documentTypeDetailID: Int, val uploadedDocumentPath: String)
}