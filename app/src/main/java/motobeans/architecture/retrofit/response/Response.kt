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
    data class ResponseAllSpinnerValue(val responseCode: String, val responseMsg: String, val responseObj: ResponseSpinner, val timeStamp: Long)
    data class ResponseSpinner(
            val ApprovalStatus: List<ApprovalStatu>,
            val Branch: List<Branch>,
            val ChannelPartnerName: List<ChannelPartnerName>,
            val ChannelType: List<ChannelType>,
            val EntityType: List<EntityType>,
            val Gender: List<Gender>,
            val LoanInformationInterestType: List<LoanInformationInterestType>,
            val LoanScheme: List<LoanScheme>,
            val LoanType: List<LoanType>,
            val OfficeType: List<OfficeType>,
            val ReferedBy: List<ReferedBy>,
            val Relationship: List<Relationship>,
            val SourcingChannelPartner: List<SourcingChannelPartner>,
            val TypeOfOrganisation: List<TypeOfOrganisation>
    )
    data class ApprovalStatu(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class Branch(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class ChannelPartnerName(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class ChannelType(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class EntityType(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class Gender(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class LoanInformationInterestType(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class LoanScheme(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class LoanType(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class OfficeType(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class ReferedBy(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class Relationship(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class SourcingChannelPartner(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
    data class TypeOfOrganisation(
            val refTypeDetailID: Int,
            val sequence: String,
            val typeDetailCode: String,
            val typeDetailDescription: String,
            val typeDetailID: Int,
            val typeDetailLogicalCode: String,
            val typeMasterDisplayText: String,
            val typeMasterID: Int,
            val typeMasterLogicalCode: String,
            val typeMasterName: String
    )
}
