package motobeans.architecture.development.implementation
import android.content.Context
import android.widget.EditText
import com.finance.app.databinding.*
import com.google.android.material.textfield.TextInputLayout
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

/**
 * Created by munishkumarthakur on 04/11/17.
 */

class FormValidationImpl(private val context: Context) : FormValidation {

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

        val currentAddress = binding.addressLayout.etCurrentAddress.text.toString()
        if (!currentAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etCurrentAddress.error = "Address can not be blank"
        }

        val permanentAddress = binding.addressLayout.etPermanentAddress.text.toString()
        if (!permanentAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etPermanentAddress.error = "Address can not be blank"
        }

        val currentLandmark = binding.addressLayout.etCurrentLandmark.text.toString()
        if (!currentLandmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etCurrentLandmark.error = "Landmark can not be blank"
        }

        val currentPinCode = binding.addressLayout.etCurrentPinCode.text.toString()
        if (!currentPinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etCurrentPinCode.error = "Pin code can not be blank"
        }

        val permanentPinCode = binding.addressLayout.etPermanentPinCode.text.toString()
        if (!permanentPinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etPermanentPinCode.error = "Pin code can not be blank"
        }

        val currentCity = binding.addressLayout.etCurrentCity.text.toString()
        if (!currentCity.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etCurrentCity.error = "City can not be blank"
        }

        val permanentCity = binding.addressLayout.etPermanentCity.text.toString()
        if (!permanentCity.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etPermanentCity.error = "City can not be blank"
        }

        val permanentStaying = binding.addressLayout.etPermanentStaying.text.toString()
        if (!permanentStaying.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etPermanentStaying.error = " This field is required"
        }

        val currentStaying = binding.addressLayout.etCurrentStaying.text.toString()
        if (!currentStaying.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etCurrentStaying.error = " This field is required"
        }

        return isValidForm(errorCount)
    }

    override fun validateLoanInformation(binding: FragmentLoanInformationBinding): Boolean {
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
        binding.addressLayout.etCurrentAddress.isEnabled = false
        binding.addressLayout.etPermanentAddress.isEnabled = false
        binding.addressLayout.etCurrentLandmark.isEnabled = false
        binding.addressLayout.etCurrentPinCode.isEnabled = false
        binding.addressLayout.etPermanentPinCode.isEnabled = false
        binding.addressLayout.etCurrentCity.isEnabled = false
        binding.addressLayout.etPermanentCity.isEnabled = false
        binding.addressLayout.etPermanentStaying.isEnabled = false
        binding.addressLayout.etCurrentStaying.isEnabled = false
        binding.addressLayout.spinnerPermanentResidenceType.isEnabled = false
        binding.addressLayout.spinnerPermanentState.isEnabled = false
        binding.addressLayout.spinnerPermanentDistrict.isEnabled = false
        binding.addressLayout.spinnerPermanentAddressProof.isEnabled = false
        binding.addressLayout.spinnerCurrentAddressProof.isEnabled = false
        binding.addressLayout.spinnerCurrentDistrict.isEnabled = false
        binding.addressLayout.spinnerCurrentState.isEnabled = false
        binding.addressLayout.cbSameAsCurrent.isClickable = false
        binding.addressLayout.etCurrentRentAmount.isEnabled = false
        binding.addressLayout.spinnerCurrentResidenceType.isEnabled = false
        binding.addressLayout.etPermanentLandmark.isEnabled = false
        binding.addressLayout.etPermanentRentAmount.isEnabled = false
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
        binding.btnAddApplicant.isEnabled = false
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
        binding.btnAddTransaction.isEnabled = false
        binding.ivUploadStatement.isClickable = false
    }

    override fun validateEmployment(binding: FragmentEmploymentBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateBankDetail(binding: FragmentBankDetailBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateAssetLiability(binding: FragmentAssetLiablityBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateReference(binding: FragmentReferenceBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateProperty(binding: FragmentPropertyBinding): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun validateTemp(binding: TempActivityBinding): Boolean {
        val errorCount = 0

        return isValidForm(errorCount)
    }

    private fun checkAndSetError(etToCheck: EditText, tilToSet: TextInputLayout,
                                 errorText: String): Int {
        var errorCount = 0
        val valToCheck = etToCheck.text.toString().trim { it <= ' ' }
        if (valToCheck.exIsNotEmptyOrNullOrBlank()) {
            tilToSet.isErrorEnabled = false
        } else {
            errorCount++
            tilToSet.isErrorEnabled = true
            tilToSet.error = errorText
        }

        return errorCount
    }

    private fun isValidForm(errorCount: Int): Boolean {
        return errorCount <= 0
    }

    private fun isPinValid(pin: String): Boolean {
        if (pin.length == 4 && pin.exIsNotEmptyOrNullOrBlank())
            return true
        return false
    }
}