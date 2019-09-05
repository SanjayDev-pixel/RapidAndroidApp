package motobeans.architecture.development.implementation

import android.content.Context
import com.google.gson.Gson
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.sharedPreferences.SharedPreferencesBean
import motobeans.architecture.sharedPreferences.SharedPreferencesCustom
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

/**
 * Created by munishkumarthakur on 04/11/17.
 */

class SharedPreferencesutilImpl(context: Context) : SharedPreferencesUtil {

    private var context: Context

    init {
        this.context = context
    }

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

    override fun clearAll() {
        try {
            for (element in SharedPreferencesBean.Array_KEY_SHARED_PREFERENCES) {
                val obj_sp = SharedPreferencesCustom(context,
                        element)
                obj_sp.clearSharedPreferences()
            }
        } catch (e: Exception) {

        }

    }

}