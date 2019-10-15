package motobeans.architecture.retrofit.request

/**
 * Created by munishkumarthakur on 04/11/17.
 */
object Requests {
    data class RequestSample(var param1: String? = null, var param2: String? = null)
    data class RequestLogin(val company: Company, val password: String, val username: String)
    data class Company(val companyId: Int, val companySlug: String)
    data class RequestAddLead(val applicantAddress: String, val applicantContactNumber: String, val loanTypeDetailID: Int,
                              val applicantEmail: String, val applicantFirstName: String, val applicantLastName: String,
                              val applicantMiddleName: String, val branchID: Int)

    data class RequestLoanInfo(val draftDetailID: Int?, val leadID: Int?, val loanApplicationObj: LoanInfoObj,
                               val storageTypeID: Int?, val userID: Int?)

    data class LoanInfoObj(val affordableEMI: Int?, val channelPartnerDsaID: Int?, val creditOfficerEmpID: Int?,
                           val creditOfficerEmpName: String?, val interestTypeTypeDetailID: Int?, val leadID: Int?,
                           val loanAmountRequest: Int?, val loanApplicationID: Int?, val loanPurposeID: Int?,
                           val loanSchemeTypeDetailID: Int?, val logginUserEntityID: Int?, val productID: Int?,
                           val ruleEngineResponse: String?, val salesOfficerEmpID: Int?, val salesOfficerEmpName: String?,
                           val sourcingChannelPartnerTypeDetailID: Int?, val tenure: Int?)

    data class RequestUpdateReference(val address: String = "Address", val addressBean: AddressBean, val applicantID: Int? = null,
                                      val applicantReferenceDetailID: Int? = null, val contactNumber: String,
                                      val knowSince: String, val name: String, val occupationTypeDetailID: Int? = null,
                                      val relationTypeDetailID: Int?, val serialNumber: Int? = null)

    data class AddressBean(val address1: String, val address2: String? = null, val addressProof: Int? = null, val cityID: Int?,
                           val cityName: String, val districtID: Int?, val landmark: String, val rentAmount: Int? = null,
                           val residenceTypeTypeDetailID: Int? = null, val zip: String)

}