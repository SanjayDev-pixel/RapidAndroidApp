package motobeans.architecture.retrofit.api

import com.finance.app.persistence.model.*
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

    @POST("api/v1/loan/application/draft/")
    fun newDraftPost(@Body request: Requests.RequestNewDraft): Observable<Response.ResponseGetLoanApplication>

    @POST("api/v1/loan/applicant/loan/information/")
    fun postLoanInfo(@Body requestPost: LoanInfoMaster): Observable<Response.ResponseLoanApplication>

    @GET("api/v1/loan/applicant/loan/information/{leadId}")
    fun getLoanInfo(@Path("leadId") leadId: String): Observable<Response.ResponseGetLoanInfo>

    @POST("api/v1/loan/application/applicant/personal/detail/")
    fun postPersonalInfo(@Body requestPost: PersonalInfoMaster): Observable<Response.ResponseLoanApplication>

    @GET("api/v1/loan/application/applicant/personal/detail/{leadId}")
    fun getPersonalInfo(@Path("leadId") leadId: String): Observable<Response.ResponseGetPersonalInfo>

    @POST("api/v1/lead/")
    fun addLead(@Body request: Requests.RequestAddLead): Observable<Response.ResponseAddLead>

    @GET("api/v1/master/all/")
    fun getAllMasterValue(): Observable<Response.ResponseAllMasterDropdown>

    @GET("api/v1/master/loan-product-purpose/")
    fun getLoanProduct(): Observable<Response.ResponseLoanProduct>

    @GET("api/v1/master/branchID/{branchId}/channelType/{channelType}/employeeID/{employeeId}/")
    fun sourceChannelPartnerName(@Path("branchId") branchId: String, @Path("channelType") channelType: String, @Path("employeeId") employeeId: String): Observable<Response.ResponseSourceChannelPartnerName>

    @PUT("api/v1/loan/application/reference/detail/draft/{leadId}/")
    fun postReference(@Path("leadId") leadId: String?, @Body requestPost: ArrayList<ReferenceMaster>): Observable<Response.ResponseLoanApplication>

    @GET("api/v1/pincode/{pinCode}/")
    fun getPinCodeDetail(@Path("pinCode") pinCode: String?): Observable<Response.ResponsePinCodeDetail>

    @POST("api/v1/loan/application/postEmployment/")
    fun postEmployment(@Body requestPost: Requests.RequestPostEmployment): Observable<Response.ResponseLoanApplication>

    @POST("api/v1/loan/obligation/card/asset/reference/detail/draft/{leadId}")
    fun postAssetLiability(@Path("leadId") leadId: String?, @Body request: ArrayList<AssetLiabilityMaster>): Observable<Response.ResponseLoanApplication>

    @POST("api/v1/loan/application/applicant/bank-details/")
    fun postBankDetail(@Body request: BankDetailMaster): Observable<Response.ResponseLoanApplication>

    @Multipart
    @POST("api/v1/file/upload/")
    fun uploadDocument(@Part("document") document: String): Observable<Response.ResponseDocumentUpload>

    @GET("api/v1/loan/application/applicant/bank-details?leadID={leadId}&loanApplicationID=&bankDetailID=0")
    fun getBankDetail(@Path("leadId") leadId: String): Observable<Response.ResponseGetBankDetail>


    @GET("api/v1/lead/")
    fun getAllLeads(): Observable<Response.ResponseGetAllLeads>

    @GET("api/v1/lead/")
    fun getEmployment(@Path("leadId") leadId: String): Observable<Response.ResponseGetEmployment>

    @GET("api/v1/master/states/")
    fun getStates(): Observable<Response.ResponseStatesDropdown>

    @GET("api/v1/master/state/{stateId}/district/")
    fun getDistricts(@Path("stateId") stateId: String): Observable<Response.ResponseDistrict>

    @GET("api/v1/master/district/{districtId}/city/")
    fun getCities(@Path("districtId") districtId: String): Observable<Response.ResponseCity>

    @GET("api/v1/master/prop-nature/{ownershipId}/{transactionId}")
    fun getPropertyNature(@Path("ownershipId") ownershipId: String, @Path("transactionId") transactionId: String): Observable<Response.ResponsePropertyNature>

    @POST("api/v1/loan/application/draft/")
    fun postLoanApp(@Body requestPost: LoanApplicationRequest): Observable<Response.ResponseGetLoanApplication>

    @GET("api/v1/loan/application/draft/lead/{leadId}/type/{storageType}/")
    fun getLoanApp(@Path("leadId") leadId: String, @Path("storageType") storageType: String): Observable<Response.ResponseGetLoanApplication>

    @GET("api/v1/loan/applicant/{leadId}/")
    fun getCoApplicantsList(@Path("leadId") leadId: String): Observable<Response.ResponseCoApplicants>

}