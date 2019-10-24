package motobeans.architecture.development.interfaces

import com.finance.app.databinding.*

interface FormValidation {
    fun validateTemp(binding: TempActivityBinding): Boolean
    fun validatePersonalInfo(binding: FragmentPersonalBinding):Boolean
    fun validateLoanInformation(binding: FragmentLoanInformationBinding):Boolean
    fun validateSalaryEmployment(binding: LayoutSalaryBinding): Boolean
    fun validateSenpEmployment(binding: LayoutSenpBinding): Boolean
    fun validateBankDetail(binding: FragmentBankDetailBinding):Boolean
    fun validateAssetLiability(binding: FragmentAssetLiablityBinding):Boolean
    fun validateReference(binding: FragmentReferenceBinding):Boolean
    fun validateProperty(binding: FragmentPropertyBinding):Boolean
    fun disablePersonalFields(binding: FragmentPersonalBinding)
    fun disableEmploymentFields(binding: FragmentEmploymentBinding)
    fun disableAssetLiabilityFields(binding: FragmentAssetLiablityBinding)
    fun disableBankDetailFields(binding: FragmentBankDetailBinding)
}
