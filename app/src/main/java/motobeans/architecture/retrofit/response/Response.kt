package motobeans.architecture.retrofit.response

import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.LoanProductMaster

object Response {
    data class ResponseSample(var isSuccess: Boolean, var status: Int, var message: String)
    data class UserBranches(val branchId: Int, val branchName: String)
    data class ResponseLogin(val responseCode: String, val responseMsg: String,
                             val responseObj: ResponseObj, val timeStamp: Long)
    data class ResponseObj(val token: String, val userDetails: UserDetails)
    data class UserDetails(val roleList: ArrayList<Role>, val rolePrivilegesList: ArrayList<RolePrivileges>,
                           val userBasicDetails: UserBasicDetails, val userBranches: ArrayList<UserBranches>,
                           val userSpecialPermissions: ArrayList<Any>)

    data class Role(val isActive: Boolean, val roleId: Int, val roleName: String,
                    val rolePrivilegesCollection: Any, val rolePrivilegesList: Any,
                    val userRoleCollection: Any)

    data class RolePrivileges(val moduleId: Int, val moduleName: String, val subModuleList: ArrayList<SubModule>)
    data class SubModule(val componentPrivileges: ArrayList<Any>, val isAdd: Boolean, val isApproved: Int,
                         val isDelete: Boolean, val isEdit: Boolean, val isExport: Boolean,
                         val isPrint: Boolean, val isView: Boolean, val screenDisplayName: String,
                         val screenId: Int, val screenName: String, val screenSectionId: Int,
                         val screenSectionName: String, val sequence: Int, val subModuleId: Int,
                         val subModuleName: String)

    data class UserBasicDetails(val entityID: Int, val neverPasswordExpired: Boolean,
                                val password: Any, val passwordChangeRequired: Boolean, val roleEdited: Boolean,
                                val secuirtyAnswer1: String, val secuirtyAnswer2: String, val secuirtyQuestionId1: Int,
                                val secuirtyQuestionId2: Int, val userId: Int, val userName: String)

    data class ResponseAddLead(val error: String, val responseCode: String, val responseMsg: String,
                               val responseObj: Any, val timeStamp: Int)

    data class ResponseAllMasterValue(val responseCode: String, val responseMsg: String,
                                      val responseObj: AllMasterDropDown, val timeStamp: Long)

    class ResponsePersonalInfo(val draftDetailID: Int, val leadID: Int, val loanApplicationObj: LoanApplicationObj, val storageTypeID: Any, val userID: Any)
    data class LoanApplicationObj(val affordableEMI: Int, val channelPartnerDsaID: Int, val creditOfficerEmpID: Int, val creditOfficerEmpName: String, val interestTypeTypeDetailID: Int, val leadID: Int,
                                  val loanAmountRequest: Int, val loanApplicationID: Int, val loanPurposeID: Int, val loanSchemeTypeDetailID: Int, val logginUserEntityID: Int, val productID: Int,
                                  val ruleEngineResponse: String, val salesOfficerEmpID: Int, val salesOfficerEmpName: String, val sourcingChannelPartnerTypeDetailID: Int, val tenure: Int)
    data class ResponseLoanInfo(val responseCode: String, val responseMsg: String, val responseObj: Any, val timeStamp: Long)
    data class ResponseSourceChannelPartnerName(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<ChannelPartnerName>, val timeStamp: Long)
    data class ChannelPartnerName(val branchID: Int, val channelTypeTypeDetailID: Int, val companyName: String, val dsaID: Int, val entityID: Int, val isActive: Boolean, val salesOfficerEmpID: Int)
    data class ResponseLoanProduct(val responseCode: String, val responseMsg: String, val responseObj: ArrayList<LoanProductMaster>, val timeStamp: Long)
    data class LoanPurpose(val loanPurposeID: Int, val loanPurposeName: String)
}
