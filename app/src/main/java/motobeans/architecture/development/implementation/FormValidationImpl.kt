package motobeans.architecture.development.implementation

import com.finance.app.databinding.*
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.persistence.model.StatesMaster
import com.finance.app.utility.CurrencyConversion
import com.finance.app.view.customViews.CustomSpinnerView
import com.google.android.material.textfield.TextInputEditText
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

class FormValidationImpl : FormValidation {

    override fun validateLogin(binding: ActivityLoginBinding): Boolean {
        val userName = binding.etUserName.text.toString()
        val pwd = binding.etPassword.text.toString()
        val error = when {
            !userName.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etUserName)
            !pwd.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etPassword)
            else -> 0
        }
        return isValidForm(error)
    }

    override fun validatePersonalInfo(binding: FragmentPersonalBinding): Boolean {
        var errorCount = 0
        val firstName = binding.basicInfoLayout.etFirstName.text.toString()
        val dob = binding.basicInfoLayout.etDOB.text.toString()
        val currentAddress = binding.personalAddressLayout.etCurrentAddress.text.toString()
        val currentLandmark = binding.personalAddressLayout.etCurrentLandmark.text.toString()
        val currentStaying = binding.personalAddressLayout.etCurrentStaying.text.toString()
        val email = binding.basicInfoLayout.etEmail.text.toString()
        val age = binding.basicInfoLayout.etAge.text.toString()
        val mobile = binding.basicInfoLayout.etMobile.text.toString()

        if(age.exIsNotEmptyOrNullOrBlank()){
            if (age.toInt() !in 99 downTo 14) {
                errorCount++
                binding.basicInfoLayout.etAge.error = "Invalid Age"
            }
        }

        if (!currentStaying.exIsNotEmptyOrNullOrBlank() || currentStaying == "" || currentStaying.toFloat() > 99) {
            errorCount++
            binding.personalAddressLayout.etCurrentStaying.error = "Required field missing or Invalid Entry"
        }

        if(!binding.personalAddressLayout.customCurrentZipAddressView.validateAndHandleError()) {
            errorCount++
        }

        if (!binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
            errorCount.plus(checkPermanentAddressFields(binding))
        }

        val fieldError = when {
            !currentLandmark.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.personalAddressLayout.etCurrentLandmark)
            !dob.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.basicInfoLayout.etDOB)
            !firstName.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.basicInfoLayout.etFirstName)
            !currentAddress.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.personalAddressLayout.etCurrentAddress)
            !isValidEmail(email) -> setFieldError(binding.basicInfoLayout.etEmail)
            !isValidMobile(mobile) -> setFieldError(binding.basicInfoLayout.etMobile)
            else -> 0
        }

        return isValidForm(fieldError)
    }

    private fun checkPermanentAddressFields(binding: FragmentPersonalBinding): Int {
        var errorCount = 0
        val permanentAddress = binding.personalAddressLayout.etPermanentAddress.text.toString()
        val permanentStaying = binding.personalAddressLayout.etPermanentStaying.text.toString()
        val rentAmount = binding.personalAddressLayout.etPermanentRentAmount.text.toString()

        if(!binding.personalAddressLayout.customPermanentZipAddressView.validateAndHandleError()) {
            errorCount++
        }

        return errorCount.plus(when {
            !permanentAddress.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.personalAddressLayout.etPermanentAddress)
            !permanentStaying.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.personalAddressLayout.etPermanentStaying)
            !rentAmount.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.personalAddressLayout.etPermanentRentAmount)
            else -> 0
        })
    }

    override fun validateLoanInformation(binding: FragmentLoanInformationBinding, loanProduct: LoanProductMaster?): Boolean {
        var errorCount = 0
        val loanAmount = CurrencyConversion().convertToNormalValue(binding.etAmountRequest.text.toString())
        val emi = binding.etEmi.text.toString()
        val tenure = binding.etTenure.text.toString()

        if (loanProduct != null && tenure != "" && loanAmount != "") {
            if (tenure.toInt() > loanProduct.maxTenure || tenure.toInt() < loanProduct.minTenure) {
                errorCount++
                binding.etTenure.error = "Range:${loanProduct.minTenure} - ${loanProduct.maxTenure}"
            }

            if (loanAmount.toInt() > loanProduct.maxAmount || loanAmount.toInt() < loanProduct.minAmount) {
                errorCount++
                binding.etAmountRequest.error = "Range:${loanProduct.minAmount} - ${loanProduct.maxAmount}"
            }
        }

        if (!loanAmount.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAmountRequest.error = "Amount can not be blank"
        }

        if (!tenure.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etTenure.error = "Tenure can not be blank"
        }

        if (!emi.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etEmi.error = "EMI can not be blank"
        } else if (loanAmount != "" && emi.toInt() > loanAmount.toInt()) {
            errorCount++
            binding.etEmi.error = "EMI cannot be greater than loan amount"
        }
        return isValidForm(errorCount)
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

    override fun validateSalaryEmployment(salaryBinding: LayoutSalaryBinding): Boolean {
        var errorCount = 0
        val companyName = salaryBinding.etCompanyName.text.toString()
        val sector = salaryBinding.spinnerSector.selectedItem as DropdownMaster?
        val industry = salaryBinding.spinnerIndustry.selectedItem as DropdownMaster?
        val employmentType = salaryBinding.spinnerEmploymentType.selectedItem as DropdownMaster?
        val designation = salaryBinding.etDesignation.text.toString()
        val totalExp = salaryBinding.etTotalExperience.text.toString()
        val retirementAge = salaryBinding.etRetirementAge.text.toString()
        val grossIncome = salaryBinding.etGrossIncome.text.toString()
        val deduction = salaryBinding.etDeduction.text.toString()
        val employeeId = salaryBinding.etEmployeeId.text.toString()

        if (!companyName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etCompanyName.error = "Company can not be blank"
        }

        if (!designation.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etDesignation.error = "Designation can not be blank"
        }

        if (!totalExp.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etTotalExperience.error = "Experience can not be blank"
        }

        if (!retirementAge.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etRetirementAge.error = "Retirement age can not be blank"
        }

        if (!grossIncome.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etGrossIncome.error = "Required Field"
        }

        if (!deduction.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etDeduction.error = "Required Field"
        }

        if (!employeeId.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etEmployeeId.error = "Required Field"
        }

        if (sector == null) {
            errorCount++
            salaryBinding.spinnerSector.error = "Required Field"
        }

        if (industry == null) {
            errorCount++
            salaryBinding.spinnerIndustry.error = "Required Field"
        }

        if (employmentType == null) {
            errorCount++
            salaryBinding.spinnerEmploymentType.error = "Required Field"
        }

        errorCount.plus(validateAddress(salaryBinding.layoutAddress))
        return isValidForm(errorCount)
    }

    override fun validateSenpEmployment(senpBinding: LayoutSenpBinding): Boolean {
        var errorCount = 0
        val businessName = senpBinding.etBusinessName.text.toString()
        if (!businessName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            senpBinding.etBusinessName.error = "Business name can not be blank"
        }

        val gstVatRegistration = senpBinding.etGstRegistration.text.toString()
        if (!gstVatRegistration.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            senpBinding.etGstRegistration.error = "Registration can not be blank"
        }

        val incorporationDate = senpBinding.etIncorporationDate.text.toString()
        if (!incorporationDate.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            senpBinding.etIncorporationDate.error = "Incorporation Date can not be blank"
        }

        val businessVintage = senpBinding.etBusinessVintage.text.toString()
        if (!businessVintage.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            senpBinding.etBusinessVintage.error = "Business Vintage can not be blank"
        }

        val constitution = senpBinding.spinnerConstitution.selectedItem as DropdownMaster?
        val industry = senpBinding.spinnerIndustry.selectedItem as DropdownMaster?
        val businessSetUp = senpBinding.spinnerBusinessSetUpType.selectedItem as DropdownMaster?

        if (constitution == null) {
            errorCount++
            senpBinding.spinnerConstitution.error = "Required Field"
        }
        if (industry == null) {
            errorCount++
            senpBinding.spinnerIndustry.error = "Required Field"
        }
        if (businessSetUp == null) {
            errorCount++
            senpBinding.spinnerBusinessSetUpType.error = "Required Field"
        }
        errorCount.plus(validateAddress(senpBinding.layoutAddress))
        return isValidForm(errorCount)
    }

    private fun validateAddress(binding: LayoutEmploymentAddressBinding): Int {
        var errorCount = 0
        val state = binding.spinnerState.selectedItem as StatesMaster?
        val district = binding.spinnerDistrict.selectedItem as Response.DistrictObj?
        val city = binding.spinnerCity.selectedItem as Response.CityObj?

        if (state == null) {
            errorCount++
            binding.spinnerState.error = "Required Field"
        }
        if (district == null) {
            errorCount++
            binding.spinnerDistrict.error = "Required Field"
        }
        if (city == null) {
            errorCount++
            binding.spinnerCity.error = "Required Field"
        }

        val pin = binding.etPinCode.text.toString()
        if (!pin.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etPinCode.error = "Required Field"
        }

        val officeAddress = binding.etAddress.text.toString()
        if (!officeAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAddress.error = "Required Field"
        }

        val landmark = binding.etLandmark.text.toString()
        if (!landmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etLandmark.error = "Required Field"
        }

        val contact = binding.etContactNum.text.toString()
        if (!isValidMobile(contact)) {
            errorCount++
            binding.etContactNum.error = "Invalid Mobile"
        }

        return errorCount
    }

    override fun validateBankDetail(binding: FragmentBankDetailBinding): Boolean {
        var errorCount = 0
        val salaryCredit = binding.spinnerSalaryCredit.selectedItem as DropdownMaster?

        if (salaryCredit == null) {
            errorCount++
            binding.spinnerSalaryCredit.error = "Required Field"
        }

        val accountNum = binding.etAccountNum.text.toString()
        if (!accountNum.exIsNotEmptyOrNullOrBlank() || accountNum.length < 6) {
            errorCount++
            binding.etAccountNum.error = "Account Num not valid"
        }
        val accountHolderName = binding.etAccountHolderName.text.toString()
        if (!accountHolderName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAccountHolderName.error = "Account holder name can not be blank"
        }

        return isValidForm(errorCount)
    }

    override fun validateBankDetail(binding: DialogBankDetailFormBinding): Boolean {
        var errorCount = 0

        val bankName = binding.spinnerBankName.selectedItem as DropdownMaster?
        if (bankName == null) {
            errorCount++
            binding.spinnerBankName.error = "Required Field"
        }

        val accountType = binding.spinnerAccountType.selectedItem as DropdownMaster?
        if (accountType == null) {
            errorCount++
            binding.spinnerAccountType.error = "Required Field"
        }

        val salaryCredit = binding.spinnerSalaryCredit.selectedItem as DropdownMaster?
        if (salaryCredit == null) {
            errorCount++
            binding.spinnerSalaryCredit.error = "Required Field"
        }

        val accountNum = binding.etAccountNum.text.toString()
        if (!accountNum.exIsNotEmptyOrNullOrBlank() || accountNum.length < 6) {
            errorCount++
            binding.etAccountNum.error = "Account Num not valid"
        }
        val accountHolderName = binding.etAccountHolderName.text.toString()
        if (!accountHolderName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAccountHolderName.error = "Account holder name can not be blank"
        }

        return isValidForm(errorCount)
    }

    override fun validateReference(binding: FragmentReferenceBinding): Boolean {
        var errorCount = 0
        val name = binding.etName.text.toString()
        val relation = binding.spinnerRelation.selectedItem as DropdownMaster?
        val occupation = binding.spinnerOccupation.selectedItem as DropdownMaster?
        val state = binding.referenceAddressLayout.spinnerState.selectedItem as StatesMaster?
        val district = binding.referenceAddressLayout.spinnerDistrict.selectedItem as Response.DistrictObj?
        val city = binding.referenceAddressLayout.spinnerCity.selectedItem as Response.CityObj?

        if (relation == null) {
            errorCount++
            binding.spinnerRelation.error = "Required Field"
        }

        if (occupation == null) {
            errorCount++
            binding.spinnerOccupation.error = "Required Field"
        }

        if (district == null) {
            errorCount++
            binding.referenceAddressLayout.spinnerDistrict.error = "Required Field"
        }
        if (city == null) {
            errorCount++
            binding.referenceAddressLayout.spinnerCity.error = "Required Field"
        }
        if (state == null) {
            errorCount++
            binding.referenceAddressLayout.spinnerState.error = "Required Field"
        }

        if (!name.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etName.error = "Name can not be blank"
        }

        val contact = binding.referenceAddressLayout.etContactNum.text.toString()
        if (!contact.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etContactNum.error = "Contact can not be blank"
        }

        val address = binding.referenceAddressLayout.etAddress.toString()
        if (!address.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.referenceAddressLayout.etAddress.error = "Address can not be blank"
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

        return isValidForm(errorCount)
    }

    override fun validateProperty(binding: FragmentPropertyInfoBinding): Boolean {
        var errorCount = 0
        val unitType = binding.spinnerUnitType.selectedItem as DropdownMaster?
        val ownership = binding.spinnerOwnership.selectedItem as DropdownMaster?
        val ownedProperty = binding.spinnerOwnedProperty.selectedItem as DropdownMaster?
        val propertyTransaction = binding.spinnerPropertyNature.selectedItem as DropdownMaster?
        val tenantNoc = binding.spinnerTenantNocAvailable.selectedItem as DropdownMaster?
        val occupiedBy = binding.spinnerOccupiedBy.selectedItem as DropdownMaster?
        val state = binding.spinnerState.selectedItem as StatesMaster?
        val district = binding.spinnerDistrict.selectedItem as Response.DistrictObj?
        val city = binding.spinnerCity.selectedItem as Response.CityObj?

        if (unitType == null) {
            errorCount++
            binding.spinnerUnitType.error = "Required Field"
        }

        if (propertyTransaction == null) {
            errorCount++
            binding.spinnerPropertyNature.error = "Required Field"
        }
        if (occupiedBy == null) {
            errorCount++
            binding.spinnerOccupiedBy.error = "Required Field"
        }

        if (tenantNoc == null) {
            errorCount++
            binding.spinnerTenantNocAvailable.error = "Required Field"
        }
        if (ownership == null) {
            errorCount++
            binding.spinnerOwnership.error = "Required Field"
        }

        if (ownedProperty == null) {
            errorCount++
            binding.spinnerOwnedProperty.error = "Required Field"
        }

        if (district == null) {
            errorCount++
            binding.spinnerDistrict.error = "Required Field"
        }
        if (city == null) {
            errorCount++
            binding.spinnerCity.error = "Required Field"
        }
        if (state == null) {
            errorCount++
            binding.spinnerState.error = "Required Field"
        }
        val tenants = binding.etNumOfTenants.text.toString()
        if (!tenants.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etNumOfTenants.error = "Required Field"
        }

        val cashOcr = binding.etCashOcr.text.toString()
        if (!cashOcr.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etCashOcr.error = "Required Field"
        }
        val ocr = binding.etOcr.text.toString()
        if (!ocr.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etOcr.error = "Required Field"
        }
        val propertyMv = binding.etMvProperty.text.toString()
        if (!propertyMv.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etMvProperty.error = "Required Field"
        }
        val propertyArea = binding.etPropertyArea.text.toString()
        if (!propertyArea.exIsNotEmptyOrNullOrBlank() || propertyArea.toInt() <= 0) {
            errorCount++
            binding.etPropertyArea.error = "Invalid Value"
        }

        val agreementValue = binding.etAgreementValue.text.toString()
        if (!agreementValue.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAgreementValue.error = "Required Field"
        }

        if (CurrencyConversion().convertToNormalValue(ocr).toDouble() > CurrencyConversion().convertToNormalValue(cashOcr).toDouble()) {
            errorCount++
            binding.etOcr.error = "Cannot be grater than cash OCR"
        }

        if (CurrencyConversion().convertToNormalValue(propertyMv).toDouble() < CurrencyConversion().convertToNormalValue(agreementValue).toDouble()) {
            errorCount++
            binding.etAgreementValue.error = "Cannot be grater than MV of property"
        }

        if (CurrencyConversion().convertToNormalValue(ocr).toDouble() > CurrencyConversion().convertToNormalValue(propertyMv).toDouble() ||
                CurrencyConversion().convertToNormalValue(ocr).toDouble() > CurrencyConversion().convertToNormalValue(agreementValue).toDouble()) {
            errorCount++
            binding.etOcr.error = "Cannot be grater than MV of Property or agreement value"
        }

        val pin = binding.etPinCode.text.toString()
        if (!pin.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etPinCode.error = "Required Field"
        }

        val propertyAddress = binding.etPropertyAddress.text.toString()
        if (!propertyAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etPropertyAddress.error = "Required Field"
        }

        val landmark = binding.etLandmark.text.toString()
        if (!landmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etLandmark.error = "Required Field"
        }

        return isValidForm(errorCount)
    }

    override fun validateAssetLiabilityForm(binding: FragmentAssetLiablityBinding): Boolean {
        var errorCount = 0
        if (!validateAssets(binding) || !validateCards(binding.layoutCreditCard) || !validateObligations(binding.layoutObligations))
            errorCount++
        return isValidForm(errorCount)
    }

    override fun validateAssets(binding: FragmentAssetLiablityBinding): Boolean {
        val assetType = binding.spinnerAssetType.selectedItem as DropdownMaster?
        val assetSubType = binding.spinnerAssetSubType.selectedItem as DropdownMaster?
        val ownership = binding.spinnerOwnership.selectedItem as DropdownMaster?
        val documentProof = binding.spinnerDocumentProof.selectedItem as DropdownMaster?
        val value = binding.etValue.text.toString()

        val errorCount = when {
            !value.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etValue)
            assetSubType == null -> setSpinnerError(binding.spinnerAssetSubType)
            assetType == null -> setSpinnerError(binding.spinnerAssetType)
            ownership == null -> setSpinnerError(binding.spinnerOwnership)
            documentProof == null -> setSpinnerError(binding.spinnerDocumentProof)
            else -> 0
        }
        return isValidForm(errorCount)
    }

    override fun validateCards(binding: LayoutCreditCardDetailsBinding): Boolean {
        val bankName = binding.spinnerBankName.selectedItem as DropdownMaster?
        val obligate = binding.spinnerObligate.selectedItem as DropdownMaster?
        val cardLimit = binding.etCreditCardLimit.text.toString()
        val utilization = binding.etCurrentUtilization.text.toString()

        val spinnerError = when {
            bankName == null -> setSpinnerError(binding.spinnerBankName)
            obligate == null -> setSpinnerError(binding.spinnerObligate)
            else -> 0
        }

        val fieldError = when {
            !utilization.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etCurrentUtilization)
            !cardLimit.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etCreditCardLimit)
            CurrencyConversion().convertToNormalValue(cardLimit).toDouble() < CurrencyConversion().convertToNormalValue(utilization).toDouble() ->
                setFieldError(binding.etCurrentUtilization)
            else -> 0
        }
        val errorCount = spinnerError + fieldError
        return isValidForm(errorCount)
    }

    override fun validateObligations(binding: LayoutObligationBinding): Boolean {
        val obligate = binding.spinnerObligate.selectedItem as DropdownMaster?
        val loanType = binding.spinnerLoanType.selectedItem as DropdownMaster?
        val repaymentBank = binding.spinnerRepaymentBank.selectedItem as DropdownMaster?
        val ownership = binding.spinnerLoanOwnership.selectedItem as DropdownMaster?
        val financierName = binding.etFinancierName.text.toString()
        val accountNum = binding.etAccountNum.text.toString()
        val balanceTenure = binding.etBalanceTenure.text.toString()
        val tenure = binding.etTenure.text.toString()
        val emiAmount = binding.etEmiAmount.text.toString()
        val bouncesIn6 = binding.etBouncesInLastSixMonths.text.toString()
        val bouncesIn9 = binding.etBouncesInLastNineMonths.text.toString()

        val spinnerError = when {
            loanType == null -> setSpinnerError(binding.spinnerLoanType)
            obligate == null -> setSpinnerError(binding.spinnerObligate)
            repaymentBank == null -> setSpinnerError(binding.spinnerRepaymentBank)
            ownership == null -> setSpinnerError(binding.spinnerLoanOwnership)
            else -> 0
        }

        val fieldError = when {
            !financierName.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etFinancierName)
            !accountNum.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etAccountNum)
            !tenure.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etTenure)
            !balanceTenure.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etBalanceTenure)
            !emiAmount.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etEmiAmount)
            !bouncesIn6.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etBouncesInLastSixMonths)
            !bouncesIn9.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etBouncesInLastNineMonths)
            else -> 0
        }
        val errorCount = spinnerError + fieldError
        return isValidForm(errorCount)
    }

    override fun validateAddLead(binding: ActivityLeadCreateBinding): Boolean {
        val area = binding.etArea.text.toString()
        val name = binding.etApplicantFirstName.text.toString()
        val email = binding.etEmail.text.toString()
        val contact = binding.etContactNum.text.toString()

        val fieldError = (when {
            !area.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etArea)
            !name.exIsNotEmptyOrNullOrBlank() -> setFieldError(binding.etApplicantFirstName)
            !isValidMobile(contact) -> setFieldError(binding.etContactNum)
            !isValidEmail(email) -> setFieldError(binding.etEmail)
            else -> 0
        })

        return isValidForm(fieldError)
    }

    private fun setCustomSpinnerError(spinner: CustomSpinnerView<*>): Int {
        spinner.setError("Required Field")
        return 2
    }

    private fun setSpinnerError(spinner: MaterialSpinner): Int {
        spinner.error = "Required Field"
        return 2
    }

    private fun setFieldError(field: TextInputEditText): Int {
        field.error = "Invalid Input"
        return 2
    }

    override fun validateTemp(binding: TempActivityBinding): Boolean {
        val errorCount = 0
        return isValidForm(errorCount)
    }

    private fun isValidMobile(phone: String): Boolean {
        return if (phone.exIsNotEmptyOrNullOrBlank()) {
            return android.util.Patterns.PHONE.matcher(phone).matches()
        } else true
    }

    private fun isValidEmail(email: String): Boolean {
        return if (email.exIsNotEmptyOrNullOrBlank()) {
            (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        } else true
    }

    private fun isValidForm(errorCount: Int): Boolean {
        return errorCount <= 0
    }
}
