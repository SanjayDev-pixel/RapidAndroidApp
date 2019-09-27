package motobeans.architecture.development.implementation

import android.content.Context
import com.finance.app.R
import com.finance.app.persistence.model.PersonalApplicants
import com.google.gson.Gson
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.sharedPreferences.SharedPreferencesBean
import motobeans.architecture.sharedPreferences.SharedPreferencesCustom
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

/**
 * Created by munishkumarthakur on 04/11/17.
 */

class SharedPreferencesUtilImpl(private var context: Context) : SharedPreferencesUtil {

    override fun saveLoginData(response: Response.ResponseLogin?): Boolean {
        val objLogin = Gson().toJson(response)
        val objSPLogin = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LOGIN_DETAILS)
        objSPLogin.putString(SharedPreferencesBean.KEY_LOGIN_DETAILS, objLogin)

        return true
    }

    override fun getLoginData(): Response.ResponseLogin? {
        val obj_sp_login = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LOGIN_DETAILS)
        val loginJson = obj_sp_login.getString(SharedPreferencesBean.KEY_LOGIN_DETAILS)
        return Gson().fromJson(loginJson, Response.ResponseLogin::class.java)
    }

    override fun getUserToken(): String? {
        return getLoginData()?.responseObj?.token
    }

    override fun getUserName(): String? {
        return getLoginData()?.responseObj?.userDetails?.userBasicDetails?.userName
    }

    override fun isLogin(): Boolean {
        return getUserToken()?.exIsNotEmptyOrNullOrBlank() ?: false
    }

    override fun savePersonalInfoForApplicants(applicants: ArrayList<PersonalApplicants>) {
        val objPersonalApplicants = Gson().toJson(applicants)
        val objSPPersonalApplicants = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        objSPPersonalApplicants.putString(SharedPreferencesBean.KEY_PERSONAL_APPLICANTS, objPersonalApplicants)
    }

    override fun getPersonalInfoForApplicants(applicantNum: Int): PersonalApplicants {
        val objSpPersonalApplicants = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        val personalApplicantJson = objSpPersonalApplicants.getString(SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        return Gson().fromJson(personalApplicantJson, PersonalApplicants::class.java)
    }

    override fun getLoginPrivilege(): Response.RolePrivileges? {
        val privilegesList  = getLoginData()?.responseObj?.userDetails?.rolePrivilegesList
        for (privilege in privilegesList!!){
            if(privilege.moduleName == "Login"){
                return privilege
            }
        }
        return null
    }

    override fun getNavMenuItem(): HashMap<String, Int>? {
        val navItemList = hashMapOf<String, Int>()
        val loginSubModules = getLoginPrivilege()?.subModuleList
        for (module in loginSubModules!!){
            when(module.screenName){
                "Loan Information" -> navItemList[module.screenName] = R.drawable.loan_info_white
                "Personal" -> navItemList[module.screenName] = R.drawable.personal_info_white
                "Employment" -> navItemList[module.screenName] = R.drawable.employment_icon_white
//                "Income" -> navItemList[module.screenName] = R.drawable.income_icon_white
                "Bank Details" -> navItemList[module.screenName] = R.drawable.bank_icon_white
                "Liability & Asset" -> navItemList[module.screenName] = R.drawable.assest_details_white
                "Reference" -> navItemList[module.screenName] = R.drawable.reffrence_white
                "Property" -> navItemList[module.screenName] = R.drawable.property_icon_white
                "Document Checklist" -> navItemList[module.screenName] = R.drawable.document_checklist
            }
        }
        return navItemList
    }

    override fun clearAll() {
        try {
            for (element in SharedPreferencesBean.Array_KEY_SHARED_PREFERENCES) {
                val objSp = SharedPreferencesCustom(context, element)
                objSp.clearSharedPreferences()
            }
        } catch (e: Exception) {

        }
    }
}