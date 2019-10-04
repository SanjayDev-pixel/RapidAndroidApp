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

        val currentPincode = binding.addressLayout.etCurrentPinCode.text.toString()
        if (!currentPincode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.addressLayout.etCurrentPinCode.error = "Pin code can not be blank"
        }

        val permanentPincode = binding.addressLayout.etPermanentPinCode.text.toString()
        if (!permanentPincode.exIsNotEmptyOrNullOrBlank()) {
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