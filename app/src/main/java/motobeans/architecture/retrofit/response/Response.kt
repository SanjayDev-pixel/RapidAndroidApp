package motobeans.architecture.retrofit.response

import com.finance.app.persistence.model.*
import java.text.DecimalFormat

object Response {
    data class ResponseSample(var isSuccess: Boolean, var status: Int, var message: String)
    data class ResponseAddLead(val error: String, val responseCode: String, val responseMsg: String,
                               val responseObj: Any? = null, val timeStamp: Long)

    data class ResponseLogin(val responseCode: String, val responseMsg: String, val responseObj: LoginObj, val timeStamp: Long)
    data class LoginObj(val token: String, val userDetails: UserDetails)
    data class UserDetails(val roleList: ArrayList<Role>, val rolePrivilegesList: ArrayList<RolePrivileges>, val userBasicDetails: UserBasicDetails, val userBranches: List<UserBranches>, val userSpecialPermissions: ArrayList<Any>)
    data class Role(val isActive: Boolean, val roleId: Int, val roleName: String, val rolePrivilegesCollection: Any, val rolePrivilegesList: Any, val userRoleCollection: Any)
    data class RolePrivileges(val moduleId: Int, val moduleName: String, val subModuleList: ArrayList<SubModule>)
    data class SubModule(val componentPrivileges: ArrayList<Any>, val isAdd: Boolean, val isApproved: Int, val isDelete: Boolean, val isEdit: Boolean, val isExport: Boolean,
                         val isPrint: Boolean, val isView: Boolean, val screenDisplayName: String, val screenId: Int, val screenName: String,
                         val screenSectionId: Int, val screenSectionName: String, val sequence: Int, val subModuleId: Int, val subModuleName: String)

    data class UserBasicDetails(val entityID: Int, val neverPasswordExpired: Boolean, val password: Any, val passwordChangeRequired: Boolean, val roleEdited: Boolean,
                                val secuirtyAnswer1: String, val secuirtyAnswer2: String, val secuirtyQuestionId1: Int, val secuirtyQuestionId2: Int, val userId: Int, val userName: String)

    data class UserBranches(val branchID: Int, val branchName: String)
    data class ResponseAllMasterValue(val responseCode: String, val responseMsg: String, val responseObj: AllMasterDropDown, val timeStamp: Long)

    data class ResponsePostPersonalInfo(val draftDetailID: Int, val leadID: Int, val loanInfoObj: LoanInfoObj, val storageTypeID: Any, val userID: Any)
    data class LoanInfoObj(val affordableEMI: Int, val channelPartnerDsaID: Int, val creditOfficerEmpID: Int, val creditOfficerEmpName: String, val interestTypeTypeDetailID: Int, val leadID: Int,
                           val loanAmountRequest: Int, val loanApplicationID: Int, val loanPurposeID: Int, val loanSchemeTypeDetailID: Int, val logginUserEntityID: Int, val productID: Int,
                           val ruleEngineResponse: String, val salesOfficerEmpID: Int, val salesOfficerEmpName: String, val sourcingChannelPartnerTypeDetailID: Int, val tenure: Int)

    data class ResponsePostLoanInfo(val responseCode: String, val responseMsg: String, val responseObj: Any?, val timeStamp: Long)
    data class ResponseGetLoanInfo(val responseCode: String, val responseMsg: String, val responseObj: LoanInfoMaster?, val timeStamp: Long)
    data class ResponseGetLoanInfoObj(val draftDetailID: Any, val leadID: Any, val loanApplicationID: Any, val loanInfoObj: LoanInfoObj, val storageType: Any, val userID: Any)
    data class ResponseSourceChannelPartnerName(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<ChannelPartnerName>, val timeStamp: Long)
    data class ChannelPartnerName(val branchID: Int, val channelTypeTypeDetailID: Int, val companyName: String, val dsaID: Int, val entityID: Int, val isActive: Boolean, val salesOfficerEmpID: Int)
    data class ResponseLoanProduct(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<LoanProductMaster>, val timeStamp: Long)
    data class LoanPurpose(val loanPurposeID: Int, val loanPurposeName: String)
    data class ResponsePostReference(val responseCode: String, val responseMsg: String, val responseObj: Any, val timeStamp: Long)
    data class ResponsePinCodeDetail(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<PinCodeObj>, val timeStamp: Long)
    data class PinCodeObj(val cityID: Int, val cityName: String, val districtID: Int, val districtName: String, val pincode: String, val pincodeID: Int, val stateID: Int, val stateName: String)
    data class ResponseLoanApplication(val responseCode: String, val responseMsg: String, val responseObj: Any? = null, val timeStamp: Long)
    data class ResponseDocumentUpload(val responseCode: String, val responseMsg: String, val responseObj: DocumentUploadObj, val timeStamp: Long)
    data class DocumentUploadObj(val applicationDocumentID: Any, val documentName: String, val documentType: Any, val documentTypeDetailID: Int, val uploadedDocumentPath: String)
    data class ResponseGetBankDetail(val responseCode: String, val responseMsg: String, val responseObj: BankDetailMaster?, val timeStamp: Long)
    data class ResponseGetAllLeads(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<AllLeadMaster>, val timeStamp: Long)
    data class BankDetailList(val bankDetailList: ArrayList<BankDetail>?)
    data class BankDetail(val applicantBankDetailsBean: ArrayList<ApplicantBankDetailsBean>?, val applicantID: Int, val firstName: String, val leadApplicantNumber: String)
    data class ApplicantBankDetailsBean(
            val accountHolderName: String,
            val accountNumber: Int,
            val accountTypeDetailID: Int,
            var active: Boolean = true,
            val applicantBankTransectionList: Any? = null,
            val applicantID: Int,
            val averageBankBalance: DecimalFormat? = null,
            val averageDebitBalance: DecimalFormat? = null,
            val bankDetailID: Int? = null,
            val bankNameTypeDetailID: Int,
            val bankStatementProof: String? = null,
            val bulkEntryAmount: DecimalFormat? = null,
            val firstName: String,
            val inwardChequeReturn: DecimalFormat? = null,
            val loanApplicationID: Int?,
            val netMonthlyCredit: DecimalFormat? = null,
            val numberOfCredit: Int?,
            val numberOfDebit: Int? = null,
            val outwardChequeReturn: DecimalFormat? = null,
            val salaryCreditTypeDetailID: Int,
            var transection: Int? = null
    )

    data class ResponseGetReference(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<ReferenceMaster>, val timeStamp: Long)
    data class ResponseGetPersonalInfo(val responseCode: String, val responseMsg: String, val responseObj: PersonalInfoMaster, val timeStamp: Long)
    data class ResponseGetEmployment(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<EmploymentMaster>, val timeStamp: Long)
    data class EmploymentApplicantDetail(
            val addressBean: AddressBean,
            val allEarningMembers: Boolean,
            val applicantID: Int,
            val businessSetupTypeDetailID: Int,
            val businessVinatgeInYear: Int,
            val companyName: String,
            val constitutionTypeDetailID: Int,
            val dateOfIncorporation: String,
            val dateOfJoining: String,
            val designation: String,
            val employeeID: String,
            val employmentTypeDetailID: Int,
            val gstRegistration: String,
            val industryTypeDetailID: Int,
            val isActive: Boolean,
            val isPensioner: Boolean,
            val leadApplicantNumber: Any,
            val loanApplicationID: Int,
            val occupationalDetailID: Int,
            val officialMailID: String,
            val profileSegmentTypeDetailID: Int,
            val retirementAge: Int,
            val sectorTypeDetailID: Int,
            val subProfileTypeDetailID: Int,
            val totalExperience: String
    )

    data class AddressBean(
            val address1: String,
            val address2: String,
            val addressID: Int,
            val addressProof: Int,
            val addressTypeDetailID: Any,
            val cityID: Int,
            val cityName: String,
            val districtID: Int,
            val entityID: Int,
            val landmark: String,
            val rentAmount: Int,
            val residenceTypeTypeDetailID: Int,
            val zip: String
    )

    data class AlreadyOwnedPropertyTypeDetailID(val typeDetailID: Int)
    data class ApplicantID(val applicantID: Int)
    data class LoanApplicationID(val loanApplicationID: Int)
    data class OwnershipTypeDetailID(val typeDetailID: Int)
    data class PropertySelectedTypeDetail(val typeDetailID: Int)
    data class UnitTypeTypeDetailID(val typeDetailID: Int)
    data class ResponseGetStates(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<StatesMaster>, val timeStamp: Long)
}
