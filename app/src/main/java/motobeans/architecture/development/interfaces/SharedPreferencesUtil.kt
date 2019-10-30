package motobeans.architecture.development.interfaces

import com.finance.app.model.Modals
import com.finance.app.model.Modals.ApplicantPersonal
import com.finance.app.persistence.model.AllLeadMaster
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response

interface SharedPreferencesUtil {
    fun saveLoginData(response: Response.ResponseLogin?): Boolean
    fun saveLoanInfoData(request:Requests.LoanInfoObj?): Boolean
    fun getLoginData(): Response.ResponseLogin?
    fun getLoanInfoData(): Requests.LoanInfoObj?
    fun isLogin(): Boolean
    fun savePersonalInfoForApplicants(applicants:ApplicantPersonal)
    fun getPersonalInfoForApplicants(): ApplicantPersonal
    fun getUserToken(): String?
    fun getUserName(): String?
    fun setPropertySelection(value: String)
    fun saveLeadDetail(lead:AllLeadMaster)
    fun getLeadId():Int
    fun getLeadNum():String
    fun getPropertySelection(): Boolean
    fun setIncomeConsideration(value: String)
    fun getIncomeConsideration(): Boolean
    fun getUserBranches(): ArrayList<Response.UserBranches>?
    fun getRolePrivilege():Response.RolePrivileges?
    fun getNavMenuItem(): HashMap<String, Int>?
    fun clearAll()
    fun getCoApplicant(): ArrayList<Modals.ApplicantTab>
    fun getCoApplicantsPosition():Int
}