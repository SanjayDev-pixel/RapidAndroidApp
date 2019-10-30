package motobeans.architecture.retrofit.request

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
                                val businessSetupTypeDetailID: Int? = null, val businessVinatgeInYear: Int? = null,
                                val companyName: String? = null, val constitutionTypeDetailID: Int? = null,
                                val dateOfIncorporation: String? = null, val dateOfJoining: String? = null,
                                val documents: ArrayList<Document>? = null, val employeeID: String? = null,
                                val employmentTypeDetailID: Int? = null, val gstRegistration: String? = null,
                                val industryTypeDetailID: Int? = null, val loanApplicationID: Int? = null,
                                val occupationDetailID: Int? = null, val occupationalDetailID: Int? = null,
                                val officialMailID: String? = null, val leadApplicantNumber: Int? = null,
                                val profileSegmentTypeDetailID: Int? = null, val retirementAge: Int? = null,
                                val sectorTypeDetailID: Int? = null, val subProfileTypeDetailID: Int? = null,
                                val totalExperience: String? = null, val designation: String? = null)
    data class EmploymentAddressBean(val address1: String, val address2: String? = null, val addressProof: Int? = null, val cityID: Int?,
                                     val cityName: String, val districtID: Int? = null, val landmark: String? = null, val rentAmount: Int? = null,
                                     val residenceTypeTypeDetailID: Int? = null, val zip: String? = null)
    data class Document(val documentTypeDetailID: Int, val uploadedDocumentPath: String)
    data class RequestBankDetail(val draftDetailID: Any? = null, val leadID: Int, val loanApplicationID: Any? = null,
                                 val loanApplicationObj: BankDetailObj, val storageTypeID: Any? = null, val userID: Int? = null)

    data class BankDetailObj(val bankDetailList: ArrayList<BankDetail>)
    data class BankDetail(val applicantBankDetailsBean: ArrayList<ApplicantBankDetailsBean>, val applicantID: Int? = null, val firstName: String? = null, val leadApplicantNumber: String)
    data class ApplicantBankDetailsBean(val accountHolderName: String, val accountNumber: Long, val accountTypeDetailID: Int? = null,
                                        val applicantBankTransectionList: List<ApplicantBankTransection>? = null, val applicantID: Int? = null,
                                        val averageBankBalance: Any? = null, val averageDebitBalance: Any? = null, val bankDetailID: Int? = null,
                                        val bankNameTypeDetailID: Int? = null, val bankStatementProof: Any? = null, val bulkEntryAmount: Any? = null,
                                        val firstName: String? = null, val inwardChequeReturn: Any? = null, val loanApplicationID: Int? = null,
                                        val netMonthlyCredit: Any? = null, val numberOfCredit: Int, val numberOfDebit: Any? = null,
                                        val outwardChequeReturn: Any? = null, val salaryCreditTypeDetailID: Int? = null, val transection: Any? = null)

    data class ApplicantBankTransection(val applicantBankTransectionID: Int)

    data class RequestPersonalInfo(val applicantDetails: ArrayList<ApplicantDetail>, val draftDetailID: Int, val leadID: Int, val storageTypeID: Any? = null, val userID: Any? = null)

    data class ApplicantDetail(val addressDetailList: ArrayList<AddressDetail>, val age: Int, val applicantID: Int, val casteTypeDetailID: Int,
                               val contactDetail: Any? = null, val dateOfBirth: String, val detailQualificationTypeDetailID: Int,
                               val dobProofImagePath: Any? = null, val dobProofTypeDetailID: Int, val entityID: Any? = null, val fatherFirstName: String,
                               val fatherLastName: String, val fatherMiddleName: String, val firstName: String, val genderTypeDetailID: Int,
                               val isMainApplicant: Boolean, val lastName: String, val loanApplicationID: Any? , val mainApplicant: Boolean,
                               val maritialStatusTypeDetailID: Int, val middleName: String, val motherFirstName: String, val motherLastName: String,
                               val motherMiddleName: String, val nationalityTypeDetailID: Int, val numberOfDependents: Int,
                               val numberOfEarningMembers: Int, val numberOfFamilyMembersChildren: Int, val numberOfFamilyMembersOthers: Int,
                               val presentAccommodationTypeDetailID: Int, val qualificationTypeDetailID: Int, val religionTypeDetailID: Int,
                               val spouseFirstName: String, val spouseLastName: String, val spouseMiddleName: String)

    data class AddressDetail(val address1: String, val address2: String, val addressID: Int, val addressProof: Int?, val addressTypeDetailID: Int,
                             val cityID: Int, val cityName: String?, val districtID: Int?, val entityID: Int, val landmark: String, val rentAmount: Int,
                             val residenceTypeTypeDetailID: Any, val zip: String)
}