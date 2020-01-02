package motobeans.architecture.retrofit.response

import com.finance.app.persistence.model.*

object Response {
    data class ResponseSample(var isSuccess: Boolean, var status: Int, var message: String)
    data class ResponseAddLead(val error: String, val responseCode: String, val responseMsg: String,
                               val responseObj: Any? = null, val timeStamp: Long)

    data class ResponseLoginTest<T>(val responseCode: String, val responseMsg: String, val responseObj: T, val timeStamp: Long)

    data class ResponseLogin(val responseCode: String, val responseMsg: String, val responseObj: LoginObj, val timeStamp: Long)
    data class LoginObj(val token: String, val userDetails: UserDetails)
    data class UserDetails(val roleList: ArrayList<Role>, val rolePrivilegesList: ArrayList<RolePrivileges>, val userBasicDetails: UserBasicDetails, val userBranches: ArrayList<UserBranches>, val userSpecialPermissions: ArrayList<Any>)
    data class Role(val isActive: Boolean, val roleId: Int, val roleName: String, val rolePrivilegesCollection: Any, val rolePrivilegesList: Any, val userRoleCollection: Any)
    data class RolePrivileges(val moduleId: Int, val moduleName: String, val subModuleList: ArrayList<SubModule>)
    data class SubModule(val componentPrivileges: ArrayList<Any>, val isAdd: Boolean, val isApproved: Int, val isDelete: Boolean, val isEdit: Boolean, val isExport: Boolean,
                         val isPrint: Boolean, val isView: Boolean, val screenDisplayName: String, val screenId: Int, val screenName: String,
                         val screenSectionId: Int, val screenSectionName: String, val sequence: Int, val subModuleId: Int, val subModuleName: String)

    data class UserBasicDetails(val tablePrimaryID: Long, val userType: String, val entityID: Int, val neverPasswordExpired: Boolean, val password: Any, val passwordChangeRequired: Boolean, val roleEdited: Boolean,
                                val secuirtyAnswer1: String, val secuirtyAnswer2: String, val secuirtyQuestionId1: Int, val secuirtyQuestionId2: Int, val userId: Int, val userName: String)

    data class ResponseAllMasterDropdown(val responseCode: String, val responseMsg: String, val responseObj: AllMasterDropDown, val timeStamp: Long)
    data class ResponseSourceChannelPartnerName(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<ChannelPartnerName>, val timeStamp: Long)
    data class ChannelPartnerName(val branchID: Int, val channelTypeTypeDetailID: Int, val companyName: String, val dsaID: Int, val entityID: Int, val isActive: Boolean, val salesOfficerEmpID: Int)
    data class ResponseLoanProduct(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<LoanProductMaster>, val timeStamp: Long)
    data class LoanPurpose(val loanPurposeID: Int, val loanPurposeName: String)
    data class ResponsePinCodeDetail(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<PinCodeObj>?, val timeStamp: Long)

    data class PinCodeObj(val cityID: Int?, val cityName: String?, val districtID: Int?, val districtName: String?, val pincode: String?, val pincodeID: Int?, val stateID: Int?, val stateName: String?)

    data class ResponseDocumentUpload(val responseCode: String, val responseMsg: String, val responseObj: DocumentUploadObj, val timeStamp: Long)
    data class DocumentUploadObj(val applicationDocumentID: Any, val documentName: String, val documentType: Any, val documentTypeDetailID: Int, val uploadedDocumentPath: String)

    data class ResponseGetAllLeads(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<AllLeadMaster>, val timeStamp: Long)

    data class ResponseGetLoanApplication(val responseCode: String, val responseMsg: String, val responseObj: LoanApplicationGetObj?, val timeStamp: Long)
    data class LoanApplicationGetObj(val draftData: String?, val editable: Boolean?, val leadID: Int, val loanApplicationDraftDetailID: Int?, val storageType: String)
    data class ResponseStatesDropdown(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<StatesMaster>, val timeStamp: Long)
    data class ResponseCity(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<CityObj>?, val timeStamp: Long)
    data class CityObj(val cityID: Int, val cityName: String)
    data class ResponseDistrict(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<DistrictObj>?, val timeStamp: Long)
    data class DistrictObj(val districtID: Int, val districtName: String)
    data class ResponsePropertyNature(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<TransactionCategoryObj>, val timeStamp: Long)
    data class TransactionCategoryObj(val propertyNatureTransactionCategory: String, val propertyNatureTransactionCategoryID: Int)
    data class ResponseCoApplicants(val responseCode: String, var responseMsg: String, val responseObj: ArrayList<CoApplicantsObj>, val timeStamp: Long)
    data class CoApplicantsObj(var applicantID: Int? = null, var entityID: Int? = null, var firstName: String? = null,
                               var incomeConsidered: Boolean? = null, var isMainApplicant: Boolean,
                               var lastName: String? = null, var leadApplicantNumber: String, var middleName: String? = null)

    data class ResponseSendOTP(val errorStack: Any?, val responseCode: String?, val responseMsg: String?, val responseObj: Any?, val timeStamp: Long?)
    data class ResponseVerifyOTP(val errorStack: Any?, val responseCode: String?, val responseMsg: String?, val responseObj: Any?, val timeStamp: Long?)
}
