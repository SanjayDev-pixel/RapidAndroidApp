package motobeans.architecture.development.implementation

import android.content.Context
import com.finance.app.model.Modals
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.LoanInfoModel
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

    override fun getUserToken(): String? {
        return getLoginData()?.responseObj?.token
    }

    override fun getUserName(): String? {
        return getLoginData()?.responseObj?.userDetails?.userBasicDetails?.userName
    }

    override fun getUserId(): String? {
        return getLoginData()?.responseObj?.userDetails?.userBasicDetails?.userId.toString()
    }

    override fun getEmpId(): String? {
        return getLoginData()?.responseObj?.userDetails?.userBasicDetails?.tablePrimaryID.toString()
    }

    override fun getUserBranches(): List<Response.UserBranches>? {

        val userBranches = ArrayList<Response.UserBranches>()

        val userSelectTextBranch = Response.UserBranches(branchID = -1, branchName = "Branch")
        userBranches.add(userSelectTextBranch)

        getLoginData()?.responseObj?.userDetails?.userBranches?.let {
            userBranches.addAll(getLoginData()!!.responseObj.userDetails.userBranches)
        }
        return userBranches
    }

    override fun isLogin(): Boolean {
        return getUserToken().exIsNotEmptyOrNullOrBlank()
    }

    override fun saveLoanInfoData(request: LoanInfoModel?): Boolean {
        val objLoanInfo = Gson().toJson(request)
        val objSPLoanInfo = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LOAN_INFO)
        objSPLoanInfo.putString(SharedPreferencesBean.KEY_LOAN_INFO, objLoanInfo)
        return true
    }

    override fun getLoanInfoData(): LoanInfoModel? {
        val objSpLoanInfo = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LOAN_INFO)
        val loanInfo = objSpLoanInfo.getString(SharedPreferencesBean.KEY_LOAN_INFO)
        return Gson().fromJson(loanInfo, LoanInfoModel::class.java)
    }

    override fun setPropertySelection(value: String) {
        val propertySelection = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PROPERTY_SELECTION)
        propertySelection.putString(SharedPreferencesBean.KEY_PROPERTY_SELECTION, value)
    }

    override fun getPropertySelection(): Boolean {
        val spPropertySelection = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PROPERTY_SELECTION)
        val propertySelection = spPropertySelection.getString(SharedPreferencesBean.KEY_PROPERTY_SELECTION)
        return propertySelection == "Yes"
    }

    override fun setIncomeConsideration(value: String) {
        val incomeConsider = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_INCOME_CONSIDER)
        incomeConsider.putString(SharedPreferencesBean.KEY_INCOME_CONSIDER, value)
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

    override fun saveLeadDetail(lead: AllLeadMaster) {
        val objLead = Gson().toJson(lead)
        val objSPLead = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LEAD_DETAIL)
        objSPLead.putString(SharedPreferencesBean.KEY_LEAD_DETAIL, objLead)
    }

    override fun getLeadDetail(): AllLeadMaster {
        val objSpLead = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadJson = objSpLead.getString(SharedPreferencesBean.KEY_LEAD_DETAIL)
        return Gson().fromJson(leadJson, AllLeadMaster::class.java)

    }

    override fun getLeadId(): String? {
        val objSpLead = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadJson = objSpLead.getString(SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadDetail = Gson().fromJson(leadJson, AllLeadMaster::class.java)
        return leadDetail.leadID.toString()
    }

    override fun getLeadNum(): String {
        val objSpLead = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadJson = objSpLead.getString(SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadDetail = Gson().fromJson(leadJson, AllLeadMaster::class.java)
        return leadDetail.leadNumber!!
    }

    override fun getLoanAppID(): Int? {
        val objSpLead = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadJson = objSpLead.getString(SharedPreferencesBean.KEY_LEAD_DETAIL)
        val leadDetail = Gson().fromJson(leadJson, AllLeadMaster::class.java)
        return leadDetail.loanApplicationID
    }

    override fun getCoApplicant(): ArrayList<Modals.ApplicantTab> {
        val objSpPersonalApplicants = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        val personalApplicantJson = objSpPersonalApplicants.getString(SharedPreferencesBean.KEY_PERSONAL_APPLICANTS)
        val personalApplicants = Gson().fromJson(personalApplicantJson, Modals.ApplicantPersonal::class.java)
        val applicants: ArrayList<Modals.ApplicantTab> = ArrayList()
        for (applicant in personalApplicants.personalApplicants) {
            if (applicant.incomeConsidered!!) {
                val coApplicant = Modals.ApplicantTab(applicant.firstName, applicant.incomeConsidered!! )
                applicants.add(coApplicant)
            }
        }
        return applicants
    }

    override fun getCoApplicantsPosition(): Int {
        val spCoApplicantPosition = SharedPreferencesCustom(context, SharedPreferencesBean.KEY_CO_APPLICANT_POSITION)
        return spCoApplicantPosition.getString(SharedPreferencesBean.KEY_CO_APPLICANT_POSITION)!!.toInt()
    }

    override fun getRolePrivilege(): Response.RolePrivileges? {
        val privilegesList = getLoginData()?.responseObj?.userDetails?.rolePrivilegesList
        for (privilege in privilegesList!!) {
            if (privilege.moduleName == "Login") {
                return privilege
            }
        }
        return null
    }

    override fun getNavMenuItem(): List<AppEnums.ScreenLoanInfo>? {
        val navItemList = ArrayList<AppEnums.ScreenLoanInfo>()
        val loginSubModules = getRolePrivilege()?.subModuleList
        for (module in loginSubModules!!) {

            val screenData = AppEnums.ScreenLoanInfo.getData(module.screenName)
            if (!(navItemList.contains(screenData)) && screenData != AppEnums.ScreenLoanInfo.DEFAULT) {
                navItemList.add(screenData)
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