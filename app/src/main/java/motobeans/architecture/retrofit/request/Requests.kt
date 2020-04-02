package motobeans.architecture.retrofit.request

object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val applicantAddress: String, val applicantContactNumber: String, val loanProductID: Int?,
                              val applicantEmail: String, val applicantFirstName: String, val applicantLastName: String,
                              val applicantMiddleName: String, val branchID: Int?, val channelPartnerID: Int?, val sourcingChannelPartnerTypeDetailCode: String?, val amountRequest: Float?)

    data class AddressBean(val address1: String, val address2: Any? = null, val addressProof: Int? = null, val cityID: Int, val cityName: String, val districtID: Int, val landmark: String, val rentAmount: Any? = null, val residenceTypeTypeDetailID: Any? = null, val zip: String)
    data class Document(val documentTypeDetailID: Int, val uploadedDocumentPath: String)
    data class RequestSendOTP(val applicantID: Int? = null, val leadID: Int, val mobile: String?)
    data class RequestVerifyOTP(val applicantID: Int? = null, val leadID: Int?, val mobile: String, val otpValue: Int)
    data class RequestCallUpdate(val leadID: Int, val customerFollowUpStatusTypeDetailId: Int? = null, val leadTypeDetailId: Int? = null, val leadRejectionReasonTypeDetailId: Int? = null, val meetingDate: String? = null, val notificationTypeDetailId: Int? = null, val messageShared: String? = null)
    data class RequestFinalSubmit(val leadID: Int)
    data class RequestFollowUp(val leadID: Int)
    data class RequestKYC(val leadID: Int?, val leadApplicantNumber: String?)
    data class RequestDocumentList(val codeId: Int)
    data class RequestUploadedDocumentList(val codeId: Int, val leadId: Int)
    data class RequestDocumentDownloadableLink(val DocumentId: Int)
    data class RequestSubmittedLead(val leadID: Int)
    data class RequestEditLead(val leadID:Int?,val applicantFirstName: String,val applicantMiddleName:String,val applicantLastName : String,val applicantContactNumber:String, val applicantAlternativeContactNumber:String?,val applicantEmail:String,val applicantAddress:String,val remarks:String?,val loanProductID:Int?,val branchID: Int?,val amountRequest:Float? )
    data class RequestKycDetail(val leadID: Int,val leadApplicantNumber:String)
}