package motobeans.architecture.development.interfaces

import com.finance.app.databinding.*

/**
 * Created by munishkumarthakur on 04/11/17.
 */

interface FormValidation {
    fun validateTemp(binding: TempActivityBinding): Boolean
    fun validatePersonalInfo(binding: FragmentPersonalBinding):Boolean
    fun validateLoanInformation(binding: FragmentLoanInformationBinding):Boolean
    fun validateEmployment(binding:FragmentEmploymentBinding):Boolean
    fun validateBankDetail(binding: FragmentBankDetailBinding):Boolean
    fun validateAssetLiability(binding: FragmentAssetLiablityBinding):Boolean
    fun validateReference(binding: FragmentReferenceBinding):Boolean
    fun validateProperty(binding: FragmentPropertyBinding):Boolean


}