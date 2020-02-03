package com.finance.app.utility

import com.finance.app.databinding.LayoutCustomEmploymentViewBinding


class DisableEmploymentForm(private val binding: LayoutCustomEmploymentViewBinding) {
    init {
        disableSalaryForm()
        disableSenpForm()
    }

    private fun disableSalaryForm() {
        binding.layoutSalary.cbIsPensioner.isChecked = false
        binding.layoutSalary.etCompanyName.isEnabled = false
        binding.layoutSalary.etJoiningDate.isEnabled = false
        binding.layoutSalary.layoutAddress.etLandmark.isEnabled = false
        binding.layoutSalary.etDesignation.isEnabled = false
        binding.layoutSalary.etEmployeeId.isEnabled = false
        binding.layoutSalary.etTotalExperience.isEnabled = false
        binding.layoutSalary.etOfficialMailId.isEnabled = false
        binding.layoutSalary.etRetirementAge.isEnabled = false
        binding.layoutSalary.layoutAddress.etAddress.isEnabled = false
        binding.layoutSalary.layoutAddress.etContactNum.isEnabled = false
        binding.layoutSalary.etNetIncome.isEnabled = false
        binding.layoutSalary.etGrossIncome.isEnabled = false
        binding.layoutSalary.etDeduction.isEnabled = false
        disableSalaryDropdown()
    }

    private fun disableSalaryDropdown() {
    }

    private fun disableSenpForm() {
        binding.layoutSenp.cbAllEarningMember.isClickable = false
        binding.layoutSenp.etBusinessName.isEnabled = false
        binding.layoutSenp.etIncorporationDate.isEnabled = false
        binding.layoutSenp.layoutAddress.etLandmark.isEnabled = false
        binding.layoutSenp.etBusinessVintage.isEnabled = false
        binding.layoutSenp.etGstRegistration.isEnabled = false
        binding.layoutSenp.layoutAddress.etContactNum.isEnabled = false
        binding.layoutSenp.layoutAddress.etAddress.isEnabled = false
        binding.layoutSenp.etAverageMonthlyIncome.isEnabled = false
        binding.layoutSenp.etLastYearIncome.isEnabled = false
        binding.layoutSenp.etCurrentYearIncome.isEnabled = false
        binding.layoutSenp.etMonthlyIncome.isEnabled = false
        disableSenpDropdown()
    }

    private fun disableSenpDropdown() {
    }
}