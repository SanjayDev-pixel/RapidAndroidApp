package motobeans.architecture.development.interfaces

import com.finance.app.persistence.model.PersonalApplicants
import motobeans.architecture.retrofit.response.Response

/**
 * Created by munishkumarthakur on 04/11/17.
 */
interface SharedPreferencesUtil {
    fun saveLoginData(response: Response.ResponseLogin?): Boolean
    fun getLoginData(): Response.ResponseLogin?
    fun isLogin(): Boolean
    fun savePersonalInfoForApplicants(applicants:ArrayList<PersonalApplicants>)
    fun getPersonalInfoForApplicants(applicantNum:Int):PersonalApplicants
    fun getUserToken(): String?
    fun getUserName(): String?
    fun getUserBranches(): ArrayList<Response.UserBranches>?
    fun getLoginPrivilege():Response.RolePrivileges?
    fun getNavMenuItem(): HashMap<String, Int>?
    // Clear All saved data from Shared Presence
    fun clearAll()
}