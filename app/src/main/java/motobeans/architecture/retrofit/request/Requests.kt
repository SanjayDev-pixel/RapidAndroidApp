package motobeans.architecture.retrofit.request

object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val applicantAddress: String, val applicantContactNumber: String, val loanProductID: Int,
                              val applicantEmail: String, val applicantFirstName: String, val applicantLastName: String,
                              val applicantMiddleName: String, val branchID: Int)

    data class AddressBean(val address1: String, val address2: Any? = null, val addressProof: Int? = null, val cityID: Int, val cityName: String, val districtID: Int, val landmark: String, val rentAmount: Any? = null, val residenceTypeTypeDetailID: Any? = null, val zip: String)
    data class Document(val documentTypeDetailID: Int, val uploadedDocumentPath: String)
    data class RequestSendOTP(val applicantID: Int? = null, val leadID: Int, val mobile: String?)
    data class RequestVerifyOTP(val applicantID: Int? = null, val leadID: Int, val mobile: String, val otpValue: Int)
}