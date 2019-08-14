package motobeans.architecture.development.interfaces

import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.retrofit.response.Response.ResponseSample

/**
 * Created by munishkumarthakur on 04/11/17.
 */
interface SharedPreferencesUtil {
    fun saveLoginData(response: Response.ResponseLogin?): Boolean
    fun getLoginData(): ResponseSample?
    fun isLogin(): Boolean
    fun getUserId(): String?
    fun getUserName(): String?

    // Clear All saved data from Shared Presence
    fun clearAll()
}