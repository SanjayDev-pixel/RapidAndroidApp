package motobeans.architecture.retrofit.api

import io.reactivex.Observable
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.request.Requests.RequestSample
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.retrofit.response.Response.ResponseSample
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * Created by munishkumarthakur on 04/11/17.
 */

interface Api {
    @POST("temp1/")
    fun postTempApi(@Body request: RequestSample): Observable<ResponseSample>

    @GET("temp2/{userId}")
    fun getTempApi(@Path("userId") userId: String?): Observable<ResponseSample>

    @POST
    fun dummyApi(@Url url: String): Observable<JSONObject>

    @POST("api/v1/auth/")
    fun loginUser(@Body request: Requests.RequestLogin): Observable<Response.ResponseLogin>

    @POST("api/v1/lead/")
    fun addLead(@Body request: Requests.RequestAddLead): Observable<Response.ResponseAddLead>

    @GET("api/v1/master/all/")
    fun getAllSpinnerValue(): Observable<Response.ResponseAllSpinnerValue>

}