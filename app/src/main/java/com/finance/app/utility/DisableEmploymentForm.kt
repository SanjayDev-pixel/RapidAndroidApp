package com.finance.app.utility

import android.text.InputType
import com.finance.app.databinding.FragmentEmploymentFormBinding
import kotlinx.android.synthetic.main.layout_zip_address.view.*

class DisableEmploymentForm(private val binding: FragmentEmploymentFormBinding) {

    init {
        disableProfileSubProfile()
        disableSalaryForm()
        disableSenpForm()
    }
    private fun disableProfileSubProfile() {
        binding.spinnerProfile.isEnabled = false
        binding.spinnerSubProfile.isEnabled = false
        binding.btnUploadEmployment.isClickable =false
    }

    private fun disableSalaryForm() {
        binding.lytSalaryDetail.cbIsPensioner.isChecked = false
        binding.lytSalaryDetail.cbIsPensioner.isClickable = false
        binding.lytSalaryDetail.etCompanyName.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etJoiningDate.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etJoiningDate.isClickable = false
        binding.lytSalaryDetail.etJoiningDate.isEnabled = false
        binding.lytSalaryDetail.layoutAddress.etLandmark.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.layoutAddress.customZipAddressView.etCurrentPinCode.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.layoutAddress.etAddress.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.layoutAddress.etContactNum.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etDesignation.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etEmployeeId.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etTotalExperience.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etOfficialMailId.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etRetirementAge.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etNetIncome.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etGrossIncome.inputType = InputType.TYPE_NULL
        binding.lytSalaryDetail.etDeduction.inputType = InputType.TYPE_NULL
        disableSalaryDropdown()
    }

    private fun disableSalaryDropdown() {
        binding.lytSalaryDetail.spinnerSector.isEnabled = false
        binding.lytSalaryDetail.spinnerSector.isEnabled = false
        binding.lytSalaryDetail.spinnerIndustry.isEnabled = false
        binding.lytSalaryDetail.spinnerEmploymentType.isEnabled = false

    }

    private fun disableSenpForm() {
        binding.lytBusinessDetail.cbAllEarningMember.isEnabled = false
        binding.lytBusinessDetail.etBusinessName.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etIncorporationDate.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etBusinessVintage.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etGstRegistration.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etAverageMonthlyIncome.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etLastYearIncome.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etCurrentYearIncome.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etMonthlyIncome.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.layoutAddress.etLandmark.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.layoutAddress.etContactNum.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.layoutAddress.etAddress.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.layoutAddress.customZipAddressView.inputLayoutCurrentPinCode.etCurrentPinCode.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.etIncorporationDate.inputType = InputType.TYPE_NULL
        binding.lytBusinessDetail.inputLayoutIncorporationDate.isClickable = false
        disableSenpDropdown()
    }

    private fun disableSenpDropdown() {
        binding.lytBusinessDetail.spinnerBusinessSetupType.isEnabled = false
        binding.lytBusinessDetail.spinnerConstitution.isEnabled = false
        binding.lytBusinessDetail.spinnerIndustry.isEnabled = false
        }

}