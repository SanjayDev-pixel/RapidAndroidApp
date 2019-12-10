package com.finance.app.utility

import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.databinding.LayoutSalaryBinding
import com.finance.app.databinding.LayoutSenpBinding

class SetEmploymentMandatoryField(binding: FragmentEmploymentBinding) {

    init {
        setSalaryMandatoryField(binding.layoutSalary)
        setSenpMandatoryField(binding.layoutSenp)
        }

    private fun setSenpMandatoryField(binding: LayoutSenpBinding) {
        ShowAsMandatory(binding.inputBusinessName)
        ShowAsMandatory(binding.inputLayoutIncorporationDate)
        ShowAsMandatory(binding.inputGstRegistration)
        ShowAsMandatory(binding.inputLayoutBusinessVintage)
        ShowAsMandatory(binding.layoutAddress.inputLayoutAddress)
        ShowAsMandatory(binding.layoutAddress.inputLayoutLandmark)
        ShowAsMandatory(binding.layoutAddress.inputLayoutContact)
        ShowAsMandatory(binding.layoutAddress.inputLayoutZip)
    }

    private fun setSalaryMandatoryField(binding: LayoutSalaryBinding) {
        ShowAsMandatory(binding.inputCompanyName)
        ShowAsMandatory(binding.inputDesignation)
        ShowAsMandatory(binding.inputLayoutEmployeeId)
        ShowAsMandatory(binding.inputLayoutJoiningDate)
        ShowAsMandatory(binding.layoutAddress.inputLayoutAddress)
        ShowAsMandatory(binding.layoutAddress.inputLayoutLandmark)
        ShowAsMandatory(binding.layoutAddress.inputLayoutContact)
        ShowAsMandatory(binding.inputLayoutGrossIncome)
        ShowAsMandatory(binding.inputLayoutDeduction)
        ShowAsMandatory(binding.layoutAddress.inputLayoutZip)
    }
}