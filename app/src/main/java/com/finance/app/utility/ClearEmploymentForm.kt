package com.finance.app.utility

import com.finance.app.databinding.FragmentEmploymentBinding

class ClearEmploymentForm(private val binding: FragmentEmploymentBinding) {

    init {
        clearSalaryForm()
        clearSenpForm()
    }

    private fun clearSalaryForm() {
        binding.layoutSalary.etCity.text?.clear()
        binding.layoutSalary.etCompanyName.text?.clear()
        binding.layoutSalary.etJoiningDate.text?.clear()
        binding.layoutSalary.etState.text?.clear()
        binding.layoutSalary.etLandmark.text?.clear()
        binding.layoutSalary.etDesignation.text?.clear()
        binding.layoutSalary.etDistrict.text?.clear()
        binding.layoutSalary.etPinCode.text?.clear()
        binding.layoutSalary.etEmployeeId.text?.clear()
        binding.layoutSalary.etLandLine.text?.clear()
        binding.layoutSalary.etTotalExperience.text?.clear()
        binding.layoutSalary.etRetirementAge.text?.clear()
        binding.layoutSalary.etOfficeAddress.text?.clear()
        binding.layoutSalary.etOfficialMailId.text?.clear()
    }

    private fun clearSenpForm() {
        binding.layoutSenp.etCity.text?.clear()
        binding.layoutSenp.etBusinessName.text?.clear()
        binding.layoutSenp.etIncorporationDate.text?.clear()
        binding.layoutSenp.etState.text?.clear()
        binding.layoutSenp.etLandmark.text?.clear()
        binding.layoutSenp.etBusinessVintage.text?.clear()
        binding.layoutSenp.etDistrict.text?.clear()
        binding.layoutSenp.etPinCode.text?.clear()
        binding.layoutSenp.etGstRegistration.text?.clear()
        binding.layoutSenp.etLandLine.text?.clear()
        binding.layoutSenp.etBusinessAddress.text?.clear()
    }
}