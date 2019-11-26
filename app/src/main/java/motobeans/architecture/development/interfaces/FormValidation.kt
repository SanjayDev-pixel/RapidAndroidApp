package motobeans.architecture.development.interfaces

import com.finance.app.databinding.*
import com.finance.app.persistence.model.LoanProductMaster

interface FormValidation {
    fun validateTemp(binding: TempActivityBinding): Boolean
    fun validatePersonalInfo(binding: FragmentPersonalBinding):Boolean
    fun validateAddLead(binding: ActivityAddLeadBinding):Boolean
    fun validateLoanInformation(binding: FragmentLoanInformationBinding, loanProduct: LoanProductMaster?): Boolean
    fun validateSalaryEmployment(salaryBinding: LayoutSalaryBinding): Boolean
    fun validateSenpEmployment(senpBinding: LayoutSenpBinding ): Boolean
    fun validateBankDetail(binding: FragmentBankDetailBinding):Boolean
    fun validateAssetLiability(binding: FragmentAssetLiablityBinding):Boolean
    fun validateReference(binding: FragmentReferenceBinding):Boolean
    fun validateProperty(binding: FragmentPropertyBinding):Boolean
    fun disablePersonalFields(binding: FragmentPersonalBinding)
    fun disableEmploymentFields(binding: FragmentEmploymentBinding)
    fun disableAssetLiabilityFields(binding: FragmentAssetLiablityBinding)
    fun disableBankDetailFields(binding: FragmentBankDetailBinding)
}
