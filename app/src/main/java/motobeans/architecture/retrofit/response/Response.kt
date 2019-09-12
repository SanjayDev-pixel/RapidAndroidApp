package motobeans.architecture.retrofit.response

object Response {

    data class ResponseSample(var isSuccess: Boolean, var status: Int, var message: String)
    data class ResponseLogin(val responseCode: String, val responseMsg: String, val responseObj: ResponseObj, val timeStamp: Long)
    data class ResponseObj(val token: String, val userDetails: UserDetails)
    data class UserDetails(val roleList: List<Role>, val rolePrivilegesList: List<RolePrivileges>, val userBasicDetails: UserBasicDetails)
    data class UserBasicDetails(val neverPasswordExpired: Boolean, val password: Any,
            val passwordChangeRequired: Boolean, val roleEdited: Boolean, val secuirtyAnswer1: String, val secuirtyAnswer2: String, val secuirtyQuestionId1: Int,
            val secuirtyQuestionId2: Int, val userId: Int, val userName: String)
    data class RolePrivileges(
            val add: Boolean,
            val delete: Boolean,
            val edit: Boolean,
            val export: Boolean,
            val isApproved: Int,
            val moduleId: Int,
            val moduleName: String,
            val print: Boolean,
            val roleId: Int,
            val roleName: Any,
            val screenId: Int,
            val screenName: String,
            val screenSectionId: Int,
            val screenSectionName: String,
            val subModuleId: Int,
            val subModuleName: String,
            val userId: Int,
            val view: Boolean
    )
    data class Role(val roleId: Int, val roleName: String, val rolePrivilegesCollection: Any, val rolePrivilegesList: Any, val userRoleCollection: Any)
    data class ResponseAddLead(val error: Any, val responseCode: String, val responseMsg: String, val responseObj: Any, val timeStamp: Int)
}
