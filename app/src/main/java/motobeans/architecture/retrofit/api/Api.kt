package motobeans.architecture.retrofit.api

import io.reactivex.Observable
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.request.Requests.RequestSample
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.retrofit.response.Response.ResponseSample
import org.json.JSONObject
import retrofit2.http.*

interface Api {
    @POST("temp1/")
    fun postTempApi(@Body request: RequestSample): Observable<ResponseSample>

    @GET("temp2/{userId}")
    fun getTempApi(@Path("userId") userId: String?): Observable<ResponseSample>

    @POST
    fun dummyApi(@Url url: String): Observable<JSONObject>

    @POST("api/v1/auth/")
    fun loginUser(@Body request: Requests.RequestLogin): Observable<Response.ResponseLogin>

    @POST("api/v1/loan/applicant/loan/information/")
    fun loanInfo(@Body request: Requests.RequestLoanInfo): Observable<Response.ResponseLoanInfo>

    @POST("api/v1/loan/application/applicant/personal/detail/")
    fun personalInfo(@Body request: Requests.RequestPersonalInfo): Observable<Response.ResponseLoanApplication>

    @POST("api/v1/lead/")
    fun addLead(@Body request: Requests.RequestAddLead): Observable<Response.ResponseAddLead>

    @GET("api/v1/master/all/")
    fun getAllMasterValue(): Observable<Response.ResponseAllMasterValue>

    @GET("api/v1/master/loan-product-purpose/")
    fun getLoanProduct(): Observable<Response.ResponseLoanProduct>

    @GET("api/v1/master/branchID/{branchId}/channelType/{channelType}/employeeID/{employeeId}/")
    fun sourceChannelPartnerName(@Path("branchId") branchId: String, @Path("channelType") channelType: String, @Path("employeeId") employeeId: String): Observable<Response.ResponseSourceChannelPartnerName>

    @PUT("api/v1/loan/application/reference/detail/draft/{leadId}/")
    fun updateReference(@Path("leadId") leadId: String?, @Body requestUpdate: ArrayList<Requests.RequestUpdateReference>): Observable<Response.ResponseUpdateReference>

    @GET("api/v1/pincode/{pinCode}/")
    fun getPinCodeDetail(@Path("pinCode") pinCode: String?): Observable<Response.ResponsePinCodeDetail>

    @POST("api/v1/loan/application/employment/")
    fun employment(@Body request: Requests.RequestEmployment): Observable<Response.ResponseEmployment>

    @POST("api/v1/loan/application/applicant/bank-details/")
    fun bankDetail(@Body request: Requests.RequestBankDetail): Observable<Response.ResponseBankDetail>

    @Multipart
    @POST("api/v1/file/upload/")
    fun uploadDocument(@Part("document") document: String): Observable<Response.ResponseDocumentUpload>

    @GET("api/v1/lead/")
    fun getAllLeads(): Observable<Response.ResponseGetAllLeads>
}