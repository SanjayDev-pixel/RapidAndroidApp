package motobeans.architecture.retrofit.request

import com.finance.app.persistence.model.BankDetailMaster

object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val applicantAddress: String, val applicantContactNumber: String, val loanProductID: Int,
                              val applicantEmail: String, val applicantFirstName: String, val applicantLastName: String,
                              val applicantMiddleName: String, val branchID: Int)

    data class RequestPostReference(val active: Boolean, val address: String, val addressBean: AddressBean,
                                    val applicantID: Int?, val applicantReferenceDetailID: Int, val contactNumber: String,
                                    val knowSince: String, val name: String, val occupationTypeDetailID: Int?,
                                    val relationTypeDetailID: Int?, val serialNumber: Int)

    data class AddressBean(val address1: String, val address2: String? = null, val addressID: Int?, val addressProof: Int,
                           val cityID: Int, val cityName: String, val districtID: Int, val entityID: Int?,
                           val landmark: String?, val rentAmount: Int, val residenceTypeTypeDetailID: Int, val zip: String)

    data class RequestPostEmployment(val applicantDetails: ArrayList<EmploymentDetail>, val leadID: Int, val loanApplicationID: Int? = null)

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
                                 val loanApplicationObj: BankDetailMaster, val storageTypeID: Any? = null, val userID: Int? = null)

    data class BankDetail(val applicantBankDetailsBean: ArrayList<ApplicantBankDetailsBean>, val applicantID: Int? = null, val firstName: String? = null, val leadApplicantNumber: String)
    data class ApplicantBankDetailsBean(val accountHolderName: String, val accountNumber: Long, val accountTypeDetailID: Int? = null,
                                        val applicantBankTransectionList: List<ApplicantBankTransection>? = null, val applicantID: Int? = null,
                                        val averageBankBalance: Any? = null, val averageDebitBalance: Any? = null, val bankDetailID: Int? = null,
                                        val bankNameTypeDetailID: Int? = null, val bankStatementProof: Any? = null, val bulkEntryAmount: Any? = null,
                                        val firstName: String? = null, val inwardChequeReturn: Any? = null, val loanApplicationID: Int? = null,
                                        val netMonthlyCredit: Any? = null, val numberOfCredit: Int, val numberOfDebit: Any? = null,
                                        val outwardChequeReturn: Any? = null, val salaryCreditTypeDetailID: Int? = null, val transection: Any? = null)

    data class ApplicantBankTransection(val applicantBankTransectionID: Int)

    data class RequestPostPersonalInfo(val applicantDetails: ArrayList<ApplicantDetail>, val draftDetailID: Int, val leadID: Int, val storageTypeID: Any? = null, val userID: Any? = null)

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

    data class RequestPostAssetLiability(val applicantAssetDetailList: ArrayList<ApplicantAssetDetail>, val applicantCreditCardDetailList: ArrayList<ApplicantCreditCardDetail>, val applicantExistingObligationList: ArrayList<ApplicantExistingObligation>, val applicantId: Int)

    data class ApplicantAssetDetail(val assetDetailsTypeDetailID: Int, val assetValue: Int, val documentedProof: Int, val ownership: String, val subTypeOfAssetTypeDetailID: Int)

    data class ApplicantCreditCardDetail(val bankNameTypeDetailID: Int, val cardLimit: Int, val currentUtilization: Int, val lastPaymentDate: String, val obligateTypeDetail: Int)

    data class ApplicantExistingObligation(val balanceTenure: Int, val borrowerNameTypeDetailID: Int, val bounseEmiPaidInSameMonth: Int, val emiAmount: Int,
                                           val financerName: String, val loanAccountNumber: String, val loanAmount: Int, val loanOwnershipTypeDetailID: Int, val loanTypeTypeDetailID: Int,
                                           val numberOfBouncesInLastNineMonth: Int, val numberOfBouncesInLastSixMonth: Int, val obligateTypeDetailID: Int, val repaymentBankTypeDetailID: Int, val tenure: Int)

    data class RequestNewDraft(val draftData: String, val leadID: Int, val loanApplictionDraftDetailID: Any, val storageType: String)
        data class RequestPostLoanApplication(var draftData: String, val editable: Boolean? = null, var leadID: Int,
                                          val loanApplictionDraftDetailID: Int? = null, var storageType: String)
}