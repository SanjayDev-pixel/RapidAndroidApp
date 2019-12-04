package motobeans.architecture.development.implementation
import android.content.Context
import com.finance.app.databinding.*
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.LoanProductMaster
import com.finance.app.persistence.model.StatesMaster
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.exIsNotEmptyOrNullOrBlank

class FormValidationImpl(private val mContext: Context) : FormValidation {

    override fun validatePersonalInfo(binding: FragmentPersonalBinding): Boolean {
        var errorCount = 0
        val firstName = binding.basicInfoLayout.etFirstName.text.toString()
        val dob = binding.basicInfoLayout.etDOB.text.toString()
        val currentAddress = binding.personalAddressLayout.etCurrentAddress.text.toString()
        val currentLandmark = binding.personalAddressLayout.etCurrentLandmark.text.toString()
        val currentPinCode = binding.personalAddressLayout.etCurrentPinCode.text.toString()
        val currentStaying = binding.personalAddressLayout.etCurrentStaying.text.toString()

        if (!firstName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.basicInfoLayout.etFirstName.error = "Name can not be blank"
        }

        if (!dob.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.basicInfoLayout.etDOB.error = "DOB can not be blank"
        }

        if (!currentAddress.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentAddress.error = "Address can not be blank"
        }

        if (!currentLandmark.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentLandmark.error = "Landmark can not be blank"
        }

        if (!currentPinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etCurrentPinCode.error = "Pin code can not be blank"
        }

        if (!currentStaying.exIsNotEmptyOrNullOrBlank() && currentStaying != "") {
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
        val permanentPinCode = binding.personalAddressLayout.etPermanentPinCode.text.toString()
        val permanentStaying = binding.personalAddressLayout.etPermanentStaying.text.toString()
        val rentAmount = binding.personalAddressLayout.etPermanentRentAmount.text.toString()

        if (!permanentAddress.exIsNotEmptyOrNullOrBlank()) {
            binding.personalAddressLayout.etPermanentAddress.error = "Address can not be blank"
            errorCount++
        }

        if (!permanentPinCode.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.personalAddressLayout.etPermanentPinCode.error = "Pin code can not be blank"
        }

        if (!permanentStaying.exIsNotEmptyOrNullOrBlank() && permanentStaying != "") {
            binding.personalAddressLayout.etPermanentStaying.error = "Required field"
            errorCount++
        }

        if (!rentAmount.exIsNotEmptyOrNullOrBlank() && rentAmount != "") {
            binding.personalAddressLayout.etPermanentRentAmount.error = "Required field"
            errorCount++
        }

        return errorCount
    }

    override fun validateLoanInformation(binding: FragmentLoanInformationBinding, loanProduct: LoanProductMaster?): Boolean {
        var errorCount = 0
        val loanAmount = binding.etAmountRequest.text.toString()
        val emi = binding.etEmi.text.toString()
        val tenure = binding.etTenure.text.toString()
        val loanPurpose = binding.spinnerLoanPurpose.selectedItem as Response.LoanPurpose?
        val loanScheme = binding.spinnerLoanScheme.selectedItem as DropdownMaster?
        val sourcingChannelPartner = binding.spinnerSourcingChannelPartner.selectedItem as DropdownMaster?

        if (loanProduct != null && tenure != "" && loanAmount != "") {
            if (tenure.toInt() > loanProduct.maxTenure || tenure.toInt() < loanProduct.minTenure) {
                errorCount++
                binding.etTenure.error = "Range:${loanProduct.minTenure} - ${loanProduct.maxTenure}"
            }

            if (loanAmount.toInt() > loanProduct.maxAmount || loanAmount.toInt() < loanProduct.minAmount) {
                errorCount++
                binding.etAmountRequest.error = "Range:${loanProduct.minAmount} - ${loanProduct.maxAmount}"
            }
        } else if (loanProduct == null) {
            errorCount++
            binding.spinnerLoanProduct.error = "Loan Product Cannot be empty"
        }

        if (!loanAmount.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAmountRequest.error = "Amount can not be blank"
        }

        if (!tenure.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etTenure.error = "Tenure can not be blank"
        }

        if (loanPurpose == null) {
            errorCount++
            binding.spinnerLoanPurpose.error = "Loan Purpose Cannot be empty"
        }

        if (loanScheme == null) {
            errorCount++
            binding.spinnerLoanScheme.error = "Loan Scheme Cannot be empty"
        }

        if (sourcingChannelPartner == null) {
            errorCount++
            binding.spinnerSourcingChannelPartner.error = "Sourcing channel partner Cannot be empty"
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

    override fun validateSalaryEmployment(salaryBinding: LayoutSalaryBinding): Boolean {
        var errorCount = 0
        val companyName = salaryBinding.etCompanyName.text.toString()
        if (!companyName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etCompanyName.error = "Company can not be blank"
        }

        val designation = salaryBinding.etDesignation.text.toString()
        if (!designation.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etDesignation.error = "Designation can not be blank"
        }

        val totalExp = salaryBinding.etTotalExperience.text.toString()
        if (!totalExp.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etTotalExperience.error = "Experience can not be blank"
        }

        val retirementAge = salaryBinding.etRetirementAge.text.toString()
        if (!retirementAge.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etRetirementAge.error = "Retirement age can not be blank"
        }

        val grossIncome = salaryBinding.etGrossIncome.text.toString()
        if (!grossIncome.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etGrossIncome.error = "Required Field"
        }

        val deduction = salaryBinding.etDeduction.text.toString()
        if (!deduction.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etDeduction.error = "Required Field"
        }

        val employeeId = salaryBinding.etEmployeeId.text.toString()
        if (!employeeId.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            salaryBinding.etEmployeeId.error = "Required Field"
        }

        val sector = salaryBinding.spinnerSector.selectedItem as DropdownMaster?
        val industry = salaryBinding.spinnerIndustry.selectedItem as DropdownMaster?
        val employmentType = salaryBinding.spinnerEmploymentType.selectedItem as DropdownMaster?

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
        if (!contact.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etContactNum.error = "Required Field"
        }

        return errorCount
    }

    override fun validateBankDetail(binding: FragmentBankDetailBinding): Boolean {
        var errorCount = 0
        val bankName = binding.spinnerBankName.selectedItem as DropdownMaster?
        val accountType = binding.spinnerAccountType.selectedItem as DropdownMaster?
        val salaryCredit = binding.spinnerSalaryCredit.selectedItem as DropdownMaster?

        if (bankName == null) {
            errorCount++
            binding.spinnerBankName.error = "Required Field"
        }
        if (accountType == null) {
            errorCount++
            binding.spinnerAccountType.error = "Required Field"
        }
        if (salaryCredit == null) {
            errorCount++
            binding.spinnerSalaryCredit.error = "Required Field"
        }

        val accountNum = binding.etAccountNum.text.toString()
        if (!accountNum.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAccountNum.error = "Account Num can not be blank"
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

    override fun validateAddLead(binding: ActivityAddLeadBinding): Boolean {
        var errorCount = 0

        val address = binding.etArea.toString()
        if (!address.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etArea.error = "Address can not be blank"
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
        if (!isValidMobile(mobile)) {
            errorCount++
            binding.etContactNum.error = "Invalid Mobile Num"
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
        return true
    }

    override fun validateProperty(binding: FragmentPropertyInfoBinding): Boolean {
        var errorCount = 0
        val unitType = binding.spinnerUnitType.selectedItem as DropdownMaster?
        val ownership = binding.spinnerOwnership.selectedItem as DropdownMaster?
        val ownedProperty = binding.spinnerOwnedProperty.selectedItem as DropdownMaster?
        val propertyTransaction = binding.spinnerPropertyTransaction.selectedItem as DropdownMaster?
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
            binding.spinnerPropertyTransaction.error = "Required Field"
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
            binding.etPropertyArea.error = "Required Field"
        }

        val agreementValue = binding.etAgreementValue.text.toString()
        if (!agreementValue.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAgreementValue.error = "Required Field"
        }

        if (ocr.toDouble() < cashOcr.toDouble()) {
            errorCount++
            binding.etOcr.error = "Cannot be grater than cash OCR"
        }

        if (propertyMv.toDouble() < agreementValue.toDouble()) {
            errorCount++
            binding.etOcr.error = "Cannot be grater than MV of property"
        }

        if (ocr.toDouble() > propertyMv.toDouble() || ocr.toDouble() > agreementValue.toDouble()) {
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
        var errorCount = 0
        val assetType = binding.spinnerAssetType.selectedItem as DropdownMaster?
        val assetSubType = binding.spinnerAssetSubType.selectedItem as DropdownMaster?
        val ownership = binding.spinnerOwnership.selectedItem as DropdownMaster?
        val documentProof = binding.spinnerDocumentProof.selectedItem as DropdownMaster?

        val value = binding.etValue.text.toString()
        if (!value.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etValue.error = "Required Field"
        }
        if (assetSubType == null) {
            errorCount++
            binding.spinnerAssetSubType.error = "Required Field"
        }

        if (assetType == null) {
            errorCount++
            binding.spinnerAssetType.error = "Required Field"
        }
        if (ownership == null) {
            errorCount++
            binding.spinnerOwnership.error = "Required Field"
        }
        if (documentProof == null) {
            errorCount++
            binding.spinnerDocumentProof.error = "Required Field"
        }
        return isValidForm(errorCount)
    }

    override fun validateCards(binding: LayoutCreditCardDetailsBinding): Boolean {
        var errorCount = 0
        val bankName = binding.spinnerBankName.selectedItem as DropdownMaster?
        val obligate = binding.spinnerObligate.selectedItem as DropdownMaster?

        val utilization = binding.etCurrentUtilization.text.toString()
        if (!utilization.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etCurrentUtilization.error = "Required Field"
        }

        val cardLimit = binding.etCreditCardLimit.text.toString()
        if (!cardLimit.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etCreditCardLimit.error = "Required Field"
        }

        if (bankName == null) {
            errorCount++
            binding.spinnerBankName.error = "Required Field"
        }

        if (obligate == null) {
            errorCount++
            binding.spinnerObligate.error = "Required Field"
        }
        return isValidForm(errorCount)
    }

    override fun validateObligations(binding: LayoutObligationBinding): Boolean {
        var errorCount = 0
        val obligate = binding.spinnerObligate.selectedItem as DropdownMaster?
        val loanType = binding.spinnerLoanType.selectedItem as DropdownMaster?
        val repaymentBank = binding.spinnerRepaymentBank.selectedItem as DropdownMaster?
        val ownership = binding.spinnerLoanOwnership.selectedItem as DropdownMaster?

        if (obligate == null) {
            errorCount++
            binding.spinnerObligate.error = "Required Field"
        }

        if (loanType == null) {
            errorCount++
            binding.spinnerLoanType.error = "Required Field"
        }
        if (repaymentBank == null) {
            errorCount++
            binding.spinnerRepaymentBank.error = "Required Field"
        }
        if (ownership == null) {
            errorCount++
            binding.spinnerLoanOwnership.error = "Required Field"
        }

        val financierName = binding.etFinancierName.text.toString()
        if (!financierName.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etFinancierName.error = "Required Field"
        }
        val accountNum = binding.etAccountNum.text.toString()
        if (!accountNum.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etAccountNum.error = "Required Field"
        }
        val tenure = binding.etTenure.text.toString()
        if (!tenure.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etTenure.error = "Required Field"
        }
        val balanceTenure = binding.etBalanceTenure.text.toString()
        if (!balanceTenure.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etBalanceTenure.error = "Required Field"
        }
        val emiAmount = binding.etEmiAmount.text.toString()
        if (!emiAmount.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etEmiAmount.error = "Required Field"
        }
        val bouncesIn6 = binding.etBouncesInLastSixMonths.text.toString()
        if (!bouncesIn6.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etBouncesInLastSixMonths.error = "Required Field"
        }
        val bouncesIn9 = binding.etBouncesInLastNineMonths.text.toString()
        if (!bouncesIn9.exIsNotEmptyOrNullOrBlank()) {
            errorCount++
            binding.etBouncesInLastNineMonths.error = "Required Field"
        }
        return isValidForm(errorCount)
    }

    override fun validateTemp(binding: TempActivityBinding): Boolean {
        val errorCount = 0
        return isValidForm(errorCount)
    }

    private fun isValidForm(errorCount: Int): Boolean {
        return errorCount <= 0
    }
}