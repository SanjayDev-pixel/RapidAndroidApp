package motobeans.architecture.development.interfaces

import com.finance.app.model.Modals
import com.finance.app.model.Modals.ApplicantPersonal
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.LoanInfoModel
import motobeans.architecture.retrofit.response.Response

interface SharedPreferencesUtil {
    fun saveLoginData(response: Response.ResponseLogin?): Boolean
    fun saveLoanInfoData(request: LoanInfoModel?): Boolean
    fun getLoginData(): Response.ResponseLogin?
    fun getLoanInfoData(): LoanInfoModel?
    fun isLogin(): Boolean
    fun savePersonalInfoForApplicants(applicants:ApplicantPersonal)
    fun getPersonalInfoForApplicants(): ApplicantPersonal
    fun getUserToken(): String?
    fun getUserName(): String?
    fun setPropertySelection(value: String)
    fun saveLeadDetail(lead:AllLeadMaster)
    fun getLeadId():String?
    fun getLeadDetail():AllLeadMaster
    fun getUserId():String?
    fun getEmpId(): String?
    fun getLoanAppID():Int?
    fun getLeadNum():String
    fun getPropertySelection(): Boolean
    fun setIncomeConsideration(value: String)
    fun getIncomeConsideration(): Boolean
    fun getUserBranches(): List<Response.UserBranches>?
    fun getRolePrivilege():Response.RolePrivileges?
    fun getNavMenuItem(): List<AppEnums.ScreenLoanInfo>?
    fun clearAll()
    fun getCoApplicant(): ArrayList<Modals.ApplicantTab>
    fun getCoApplicantsPosition():Int
}