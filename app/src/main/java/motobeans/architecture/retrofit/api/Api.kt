package motobeans.architecture.retrofit.api

import com.finance.app.persistence.model.LoanApplicationRequest
import io.reactivex.Observable
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.request.Requests.RequestSample
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.retrofit.response.Response.ResponseSample
import retrofit2.http.*

interface Api {
    @POST("temp1/")
    fun postTempApi(@Body request: RequestSample): Observable<ResponseSample>

    @POST("api/v1/auth/")
    fun loginUser(@Body request: Requests.RequestLogin): Observable<Response.ResponseLogin>

    @POST("api/v1/lead/")
    fun <T> addLead(@Body request: T): Observable<Response.ResponseAddLead>

    @GET("api/v1/master/all/")
    fun getAllMasterValue(): Observable<Response.ResponseAllMasterDropdown>

    @GET("api/v1/master/loan-product-purpose/")
    fun getLoanProduct(): Observable<Response.ResponseLoanProduct>

    @GET("api/v1/master/branchID/{branchId}/channelType/{channelType}/employeeID/{employeeId}/")
    fun sourceChannelPartnerName(@Path("branchId") branchId: String, @Path("channelType") channelType: String, @Path("employeeId") employeeId: String): Observable<Response.ResponseSourceChannelPartnerName>

    @GET("api/v1/pincode/{pinCode}/")
    fun getPinCodeDetail(@Path("pinCode") pinCode: String?): Observable<Response.ResponsePinCodeDetail>

    @Multipart
    @POST("api/v1/file/upload/")
    fun uploadDocument(@Part("document") document: String): Observable<Response.ResponseDocumentUpload>

    @GET("api/v1/lead/")
    fun getAllLeads(): Observable<Response.ResponseGetAllLeads>

    @GET("api/v1/master/states/")
    fun getStates(): Observable<Response.ResponseStatesDropdown>

    @GET("api/v1/master/state/{stateId}/district/")
    fun getDistricts(@Path("stateId") stateId: String): Observable<Response.ResponseDistrict>

    @GET("api/v1/master/district/{districtId}/city/")
    fun getCities(@Path("districtId") districtId: String): Observable<Response.ResponseCity>

    @GET("api/v1/master/prop-nature/{ownershipId}/{transactionId}")
    fun gettransactionCategory(@Path("ownershipId") ownershipId: String, @Path("transactionId") transactionId: String): Observable<Response.ResponsePropertyNature>

    @POST("api/v1/loan/application/draft/")
    fun postLoanApp(@Body requestPost: LoanApplicationRequest): Observable<Response.ResponseGetLoanApplication>

    @GET("api/v1/loan/application/draft/lead/{leadIdForApplicant}/type/{storageType}/")
    fun getLoanApp(@Path("leadIdForApplicant") leadId: String, @Path("storageType") storageType: String): Observable<Response.ResponseGetLoanApplication>

    @GET("api/v1/loan/applicant/{leadIdForApplicant}/")
    fun getCoApplicantsList(@Path("leadIdForApplicant") leadId: String): Observable<Response.ResponseCoApplicants>

    @POST("api/v1/common/contact/verification/mobile/get/otp")
    fun sendOTP(@Body requestPost: Requests.RequestSendOTP): Observable<Response.ResponseSendOTP>

    @POST("api/v1/common/contact/verification/mobile/verify/otp")
    fun verifyOTP(@Body requestPost: Requests.RequestVerifyOTP): Observable<Response.ResponseVerifyOTP>

}
