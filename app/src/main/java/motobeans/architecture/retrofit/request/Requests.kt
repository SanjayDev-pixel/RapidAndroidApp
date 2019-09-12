package motobeans.architecture.retrofit.request

/**
 * Created by munishkumarthakur on 04/11/17.
 */
object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val actualCompletionDate: Any, val applicantAddress: String, val applicantAlternativeContactNumber: Any, val applicantContactNumber: String, val applicantEmail: String,
                              val applicantFirstName: String, val applicantLastName: String, val applicantMiddleName: String, val branchID: Int, val convertedToLoanApplication: Any, val currentStatus: String, val dsaID: Int,
                              val estimatedCompletionDate: Any, val leadFailedReason: Any, val leadID: Any, val leadNumber: Any, val leadOwnershipEntityID: Any, val leadReference: Any, val leadSourceTypeDetailID: Any, val loanTypeDetailID: Int, val remarks: String)
}