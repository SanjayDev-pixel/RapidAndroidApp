package motobeans.architecture.development.implementation
import android.content.Context
import com.finance.app.R
import com.finance.app.model.Modals
import com.google.gson.Gson
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.sharedPreferences.SharedPreferencesBean
import motobeans.architecture.sharedPreferences.SharedPreferencesCustom
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

class SharedPreferencesUtilImpl(private var context: Context) : SharedPreferencesUtil {

    override fun saveLoginData(response: Response.ResponseLogin?): Boolean {
        val objLogin = Gson().toJson(response)
        val objSPLogin = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LOGIN_DETAILS)
        objSPLogin.putString(SharedPreferencesBean.KEY_LOGIN_DETAILS, objLogin)
        return true
    }

    override fun getLoginData(): Response.ResponseLogin? {
        val objSpLogin = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LOGIN_DETAILS)
        val loginJson = objSpLogin.getString(SharedPreferencesBean.KEY_LOGIN_DETAILS)
        return Gson().fromJson(loginJson, Response.ResponseLogin::class.java)
    }

    override fun getUserToken(): String?{
        return getLoginData()?.responseObj?.token
    }

    override fun getUserName(): String? {
        return getLoginData()?.responseObj?.userDetails?.userBasicDetails?.userName
    }

    override fun isLogin(): Boolean {
        return getUserToken().exIsNotEmptyOrNullOrBlank()
    }

    override fun setPropertySelection(value: String) {
        val propertySelection = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PROPERTY_SELECTION)
        propertySelection.putString(SharedPreferencesBean.KEY_PROPERTY_SELECTION,value)
    }

    override fun getPropertySelection(): Boolean {
        val spPropertySelection = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PROPERTY_SELECTION)
        val propertySelection = spPropertySelection.getString(SharedPreferencesBean.KEY_PROPERTY_SELECTION)
        return propertySelection == "Yes"
    }

    override fun setIncomeConsideration(value: String) {
        val incomeConsider = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_INCOME_CONSIDER)
        incomeConsider.putString(SharedPreferencesBean.KEY_INCOME_CONSIDER,value)
    }

    override fun getIncomeConsideration(): Boolean {
        val spIncomeConsider = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_INCOME_CONSIDER)
        val incomeConsider = spIncomeConsider.getString(SharedPreferencesBean.KEY_INCOME_CONSIDER)
        return incomeConsider == "Yes"
    }

    override fun savePersonalInfoForApplicants(applicants: Modals.ApplicantPersonal) {
        val objPersonalApplicants = Gson().toJson(applicants)
        val objSPPersonalApplicants = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        objSPPersonalApplicants.putString(SharedPreferencesBean.KEY_PERSONAL_APPLICANTS, objPersonalApplicants)
    }

    override fun getPersonalInfoForApplicants(): Modals.ApplicantPersonal {
        val objSpPersonalApplicants = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        val personalApplicantJson = objSpPersonalApplicants.getString(SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        return Gson().fromJson(personalApplicantJson, Modals.ApplicantPersonal::class.java)
    }

    override fun setCoApplicantsPosition(position: String) {
        val coApplicantPosition = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_CO_APPLICANT_POSITION)
        coApplicantPosition.putString(SharedPreferencesBean.KEY_CO_APPLICANT_POSITION, position)
    }

    override fun getCoApplicantsPosition(): Int {
        val spCoApplicantPosition = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_CO_APPLICANT_POSITION)
        return spCoApplicantPosition.getString(SharedPreferencesBean.KEY_CO_APPLICANT_POSITION)!!.toInt()
    }

    override fun getRolePrivilege(): Response.RolePrivileges? {
        val privilegesList = getLoginData()?.responseObj?.userDetails?.
                rolePrivilegesList
        for (privilege in privilegesList!!){
            if(privilege.moduleName == "Login"){
                return privilege
            }
        }
        return null
    }

    override fun getNavMenuItem(): LinkedHashMap<String, Int>? {
        val navItemList = linkedMapOf<String, Int>()
        val loginSubModules = getRolePrivilege()?.subModuleList
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
                "Document Checklist" -> navItemList[module.screenName] = R.drawable.checklist
            }
        }
        return navItemList
    }

    override fun getUserBranches(): ArrayList<Response.UserBranches>? {
        return getLoginData()?.responseObj?.userDetails?.userBranches
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