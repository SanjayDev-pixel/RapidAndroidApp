package motobeans.architecture.development.implementation
import android.content.Context
import com.finance.app.databinding.*
import com.finance.app.persistence.model.LoanProductMaster
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

class FormValidationImpl(private val mContext: Context) : FormValidation {

    override fun validatePersonalInfo(binding: FragmentPersonalBinding): Boolean {
        var errorCount = 0
        val firstName = binding.basicInfoLayout.etFirstName.text.toString()
        if (!firstName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.basicInfoLayout.etFirstName.error = "Name can not be blank"
        }

        val dob = binding.basicInfoLayout.etDOB.text.toString()
        if (!dob.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.basicInfoLayout.etDOB.error = "DOB can not be blank"
        }

        val alternateContact = binding.basicInfoLayout.etAlternateNum.text.toString()
        if (!alternateContact.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.basicInfoLayout.etAlternateNum.error = "Alternate Number can not be blank"
        }

        val currentAddress = binding.personalAddressLayout.etCurrentAddress.text.toString()
        if (!currentAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentAddress.error = "Address can not be blank"
        }

        val currentLandmark = binding.personalAddressLayout.etCurrentLandmark.text.toString()
        if (!currentLandmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentLandmark.error = "Landmark can not be blank"
        }

        val currentPinCode = binding.personalAddressLayout.etCurrentPinCode.text.toString()
        if (!currentPinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentPinCode.error = "Pin code can not be blank"
        }

        val currentCity = binding.personalAddressLayout.etCurrentCity.text.toString()
        if (!currentCity.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentCity.error = "City can not be blank"
        }

        val currentStaying = binding.personalAddressLayout.etCurrentStaying.text.toString()
        if (!currentStaying.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentStaying.error = "Required field"
        }

        if (!binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
            errorCount.plus(checkPermanentAddressFields(binding))
        }

        return isValidForm(errorCount)
    }

    private fun checkPermanentAddressFields(binding: FragmentPersonalBinding): Int {
        var errorCount = 0
        val permanentAddress = binding.personalAddressLayout.etPermanentAddress.text.toString()
        if (!permanentAddress.exIsNotEmptyOrNullOrBlank()) {
            binding.personalAddressLayout.etPermanentAddress.error = "Address can not be blank"
            errorCount++
        }

        val permanentPinCode = binding.personalAddressLayout.etPermanentPinCode.text.toString()
        if (!permanentPinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etPermanentPinCode.error = "Pin code can not be blank"
        }

        val permanentCity = binding.personalAddressLayout.etPermanentCity.text.toString()
        if (!permanentCity.exIsNotEmptyOrNullOrBlank()) {
            binding.personalAddressLayout.etPermanentCity.error = "City can not be blank"
            errorCount++
        }

        val permanentStaying = binding.personalAddressLayout.etPermanentStaying.text.toString()
        if (!permanentStaying.exIsNotEmptyOrNullOrBlank()) {
            binding.personalAddressLayout.etPermanentStaying.error = "Required field"
            errorCount++
        }
        return errorCount
    }

    override fun validateLoanInformation(binding: FragmentLoanInformationBinding, loanProduct: LoanProductMaster?): Boolean {
        var errorCount = 0
        val loanAmount = binding.etAmountRequest.text.toString()
        if (!loanAmount.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAmountRequest.error = "Amount can not be blank"
        }

        val tenure = binding.etTenure.text.toString()
        if (!tenure.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etTenure.error = "Tenure can not be blank"
        }

        if (loanProduct != null) {
            if (tenure.toInt() > loanProduct.maxTenure) {
                errorCount++
                binding.etTenure.error = "Tenure more than the max value"
            }

            if (loanAmount.toInt() > loanProduct.maxAmount) {
                errorCount++
                binding.etAmountRequest.error = "Amount more than the max value"
            }
        } else {
            binding.spinnerLoanProduct.error = "loan Product Cannot be empty"
        }

        val emi = binding.etEmi.text.toString()
        if (!emi.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etEmi.error = "EMI can not be blank"
        }
        return isValidForm(errorCount)
    }

    override fun disablePersonalFields(binding: FragmentPersonalBinding) {
        binding.spinnerIdentificationType.isEnabled = false
        binding.etIdNum.isEnabled = false
        binding.etExpiryDate.isEnabled = false
        binding.etIssueDate.isEnabled = false
        binding.btnSaveAndContinue.isEnabled = false
        binding.ivUploadKyc.isClickable = false
        binding.btnAddKYC.isEnabled = false
        binding.spinnerVerifiedStatus.isEnabled = false
        binding.basicInfoLayout.ivUploadDobProof.isClickable = false
        binding.basicInfoLayout.btnGetOTP.isEnabled = false
        binding.basicInfoLayout.btnVerifyOTP.isEnabled = false
        binding.basicInfoLayout.etAge.isEnabled = false
        binding.basicInfoLayout.otpView.isEnabled = false
        binding.basicInfoLayout.etEmail.isEnabled = false
        binding.basicInfoLayout.etFatherFirstName.isEnabled = false
        binding.basicInfoLayout.etFatherMiddleName.isEnabled = false
        binding.basicInfoLayout.etFatherLastName.isEnabled = false
        binding.basicInfoLayout.etFirstName.isEnabled = false
        binding.basicInfoLayout.etLastName.isEnabled = false
        binding.basicInfoLayout.etMiddleName.isEnabled = false
        binding.basicInfoLayout.etSpouseFirstName.isEnabled = false
        binding.basicInfoLayout.etSpouseMiddleName.isEnabled = false
        binding.basicInfoLayout.etSpouseLastName.isEnabled = false
        binding.basicInfoLayout.spinnerReligion.isEnabled = false
        binding.basicInfoLayout.spinnerCaste.isEnabled = false
        binding.basicInfoLayout.etNumOfDependent.isEnabled = false
        binding.basicInfoLayout.spinnerDobProof.isEnabled = false
        binding.basicInfoLayout.spinnerDetailQualification.isEnabled = false
        binding.basicInfoLayout.spinnerMaritalStatus.isEnabled = false
        binding.basicInfoLayout.spinnerNationality.isEnabled = false
        binding.basicInfoLayout.spinnerQualification.isEnabled = false
        binding.basicInfoLayout.etMobile.isEnabled = false
        binding.basicInfoLayout.spinnerGender.isEnabled = false
        binding.basicInfoLayout.etDOB.isEnabled = false
        binding.basicInfoLayout.etAlternateNum.isEnabled = false
        binding.personalAddressLayout.etCurrentAddress.isEnabled = false
        binding.personalAddressLayout.etPermanentAddress.isEnabled = false
        binding.personalAddressLayout.etCurrentLandmark.isEnabled = false
        binding.personalAddressLayout.etCurrentPinCode.isEnabled = false
        binding.personalAddressLayout.etPermanentPinCode.isEnabled = false
        binding.personalAddressLayout.etCurrentCity.isEnabled = false
        binding.personalAddressLayout.etPermanentCity.isEnabled = false
        binding.personalAddressLayout.etPermanentStaying.isEnabled = false
        binding.personalAddressLayout.etCurrentStaying.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentResidenceType.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentState.isEnabled = false
//        binding.personalAddressLayout.spinnerPermanentDistrict.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentAddressProof.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentAddressProof.isEnabled = false
//        binding.personalAddressLayout.spinnerCurrentDistrict.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentState.isEnabled = false
        binding.personalAddressLayout.cbSameAsCurrent.isClickable = false
        binding.personalAddressLayout.etCurrentRentAmount.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentResidenceType.isEnabled = false
        binding.personalAddressLayout.etPermanentLandmark.isEnabled = false
        binding.personalAddressLayout.etPermanentRentAmount.isEnabled = false
    }

    override fun disableEmploymentFields(binding: FragmentEmploymentBinding) {
        binding.spinnerProfileSegment.isEnabled = false
        binding.spinnerSubProfile.isEnabled = false
    }

    override fun disableAssetLiabilityFields(binding: FragmentAssetLiablityBinding) {
        binding.spinnerAssetType.isEnabled = false
        binding.spinnerAssetSubType.isEnabled = false
        binding.spinnerOwnership.isEnabled = false
        binding.spinnerDocumentProof.isEnabled = false
        binding.etValue.isEnabled = false
        binding.btnAddAsset.isEnabled = false
        binding.layoutCreditCard.spinnerBankName.isEnabled = false
        binding.layoutCreditCard.etCreditCardLimit.isEnabled = false
        binding.layoutCreditCard.etLastPaymentDate.isEnabled = false
        binding.layoutCreditCard.etCurrentUtilization.isEnabled = false
        binding.layoutCreditCard.spinnerObligate.isEnabled = false
        binding.layoutCreditCard.btnAddCreditCard.isEnabled = false
        binding.layoutObligations.spinnerLoanOwnership.isEnabled = false
        binding.layoutObligations.spinnerObligate.isEnabled = false
        binding.layoutObligations.spinnerLoanType.isEnabled = false
        binding.layoutObligations.etLoanAmount.isEnabled = false
        binding.layoutObligations.etFinancierName.isEnabled = false
        binding.layoutObligations.etTenure.isEnabled = false
        binding.layoutObligations.etBalanceTenure.isEnabled = false
        binding.layoutObligations.spinnerRepaymentBank.isEnabled = false
        binding.layoutObligations.etEmiAmount.isEnabled = false
        binding.layoutObligations.etBouncesInLastNineMonths.isEnabled = false
        binding.layoutObligations.etBouncesInLastSixMonths.isEnabled = false
        binding.layoutObligations.spinnerEmiPaidInSameMonth.isEnabled = false
        binding.layoutObligations.etDisbursementDate.isEnabled = false
        binding.layoutObligations.btnAddObligation.isEnabled = false
    }

    override fun disableBankDetailFields(binding: FragmentBankDetailBinding) {
        binding.spinnerBankName.isEnabled = false
        binding.spinnerAccountType.isEnabled = false
        binding.spinnerSalaryCredit.isEnabled = false
        binding.etAccountNum.isEnabled = false
        binding.etAccountHolderName.isEnabled = false
        binding.ivUploadStatement.isClickable = false
    }

    override fun validateSalaryEmployment(binding: LayoutSalaryBinding): Boolean {
        var errorCount = 0
        val companyName = binding.etCompanyName.text.toString()
        if (!companyName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etCompanyName.error = "Company can not be blank"
        }

        val designation = binding.etDesignation.text.toString()
        if (!designation.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etDesignation.error = "Designation can not be blank"
        }

        val totalExp = binding.etTotalExperience.text.toString()
        if (!totalExp.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etTotalExperience.error = "Experience can not be blank"
        }

        val retirementAge = binding.etRetirementAge.text.toString()
        if (!retirementAge.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etRetirementAge.error = "Retirement age can not be blank"
        }

        val officeAddress = binding.etOfficeAddress.text.toString()
        if (!officeAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etOfficeAddress.error = "Office address age can not be blank"
        }

        val landmark = binding.etLandmark.text.toString()
        if (!landmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etLandmark.error = "Landmark can not be blank"
        }

        val pin = binding.etPinCode.text.toString()
        if (!pin.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etPinCode.error = "Pin Code can not be blank"
        }

        val district = binding.etDistrict.text.toString()
        if (!district.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etDistrict.error = "District can not be blank"
        }

        val state = binding.etState.text.toString()
        if (!state.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etState.error = "State can not be blank"
        }
        return isValidForm(errorCount)
    }

    override fun validateSenpEmployment(binding: LayoutSenpBinding): Boolean {
        var errorCount = 0
        val businessName = binding.etBusinessName.text.toString()
        if (!businessName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etBusinessName.error = "Business name can not be blank"
        }

        val gstVatRegistration = binding.etGstRegistration.text.toString()
        if (!gstVatRegistration.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etGstRegistration.error = "Registration can not be blank"
        }

        val incorporationDate = binding.etIncorporationDate.text.toString()
        if (!incorporationDate.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etIncorporationDate.error = "Incorporation Date can not be blank"
        }

        val businessVintage = binding.etBusinessVintage.text.toString()
        if (!businessVintage.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etBusinessVintage.error = "Business Vintage can not be blank"
        }

        val businessAddress = binding.etBusinessAddress.text.toString()
        if (!businessAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etBusinessAddress.error = "Business address age can not be blank"
        }

        val landmark = binding.etLandmark.text.toString()
        if (!landmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etLandmark.error = "Landmark can not be blank"
        }

        val pin = binding.etPinCode.text.toString()
        if (!pin.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etPinCode.error = "Pin Code can not be blank"
        }

        val district = binding.etDistrict.text.toString()
        if (!district.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etDistrict.error = "District can not be blank"
        }

        val state = binding.etState.text.toString()
        if (!state.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etState.error = "State can not be blank"
        }

        return isValidForm(errorCount)
    }

    override fun validateBankDetail(binding: FragmentBankDetailBinding): Boolean {
        var errorCount = 0
        val bankName = binding.etAccountNum.text.toString()
        if (!bankName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAccountNum.error = "Account Num can not be blank"
        }

        val accountHolderName = binding.etAccountHolderName.text.toString()
        if (!accountHolderName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAccountHolderName.error = "Account holder name can not be blank"
        }

        val salaryCreditNum = binding.etSalaryCreditedInSixMonths.text.toString().toInt()
        if (salaryCreditNum > 6) {
            errorCount++
            binding.etSalaryCreditedInSixMonths.error = "This can not be more than 6"
        }
        return isValidForm(errorCount)
    }

    override fun validateReference(binding: FragmentReferenceBinding): Boolean {
        var errorCount = 0
        val name = binding.etName.text.toString()
        if (!name.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etName.error = "Name can not be blank"
        }

        val contact = binding.etContactNum.text.toString()
        if (!contact.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etContactNum.error = "Contact can not be blank"
        }

        val address = binding.referenceAddressLayout.etAddress1.toString()
        if (!address.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etAddress1.error = "Address can not be blank"
        }

        val landmark = binding.referenceAddressLayout.etLandmark.text.toString()
        if (!landmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etLandmark.error = "Landmark can not be blank"
        }

        val pinCode = binding.referenceAddressLayout.etPinCode.text.toString()
        if (!pinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etPinCode.error = "Pin code can not be blank"
        }

        val city = binding.referenceAddressLayout.etCity.text.toString()
        if (!city.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etCity.error = "City can not be blank"
        }

        val district = binding.referenceAddressLayout.etDistrict.text.toString()
        if (!district.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etDistrict.error = "District field is required"
        }
        return isValidForm(errorCount)
    }

    override fun validateAddLead(binding: ActivityAddLeadBinding): Boolean {
        var errorCount = 0

        val address = binding.etAddress.toString()
        if (!address.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAddress.error = "Address can not be blank"
        }

        val name = binding.etApplicantFirstName.text.toString()
        if (!name.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etApplicantFirstName.error = "Name can not be blank"
        }

        val email = binding.etEmail.text.toString()
        if (!isValidEmail(email)) {
            errorCount++
            binding.etEmail.error = "Invalid Email"
        }

        val mobile = binding.etContactNum.text.toString()
        if (isValidMobile(mobile)) {
            errorCount++
            binding.etContactNum.error = "Invalid Mobile Num"
        }

        val loanProduct = binding.spinnerLoanProduct.selectedItem as LoanProductMaster?
        if (loanProduct == null) {
            errorCount++
            binding.spinnerLoanProduct.error = "Select Loan"
        }

        val branch = binding.spinnerBranches.selectedItem as Response.UserBranches?
        if (branch == null) {
            errorCount++
            binding.spinnerBranches.error = "Select Branch"
        }

        return isValidForm(errorCount)
    }

    private fun isValidMobile(mobile: String): Boolean {
        if (mobile.exIsNotEmptyOrNullOrBlank()) {
            return android.util.Patterns.PHONE.matcher(mobile).matches()
        }
        return false
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.exIsNotEmptyOrNullOrBlank()) {
            return (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        }
        return false
    }

    override fun validateProperty(binding: FragmentPropertyBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateAssetLiability(binding: FragmentAssetLiablityBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateTemp(binding: TempActivityBinding): Boolean {
        val errorCount = 0
        return isValidForm(errorCount)
    }

    private fun isValidForm(errorCount: Int): Boolean {
        return errorCount <= 0
    }
}