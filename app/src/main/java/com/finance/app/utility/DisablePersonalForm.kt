package com.finance.app.utility

import com.finance.app.databinding.FragmentPersonalBinding

class DisablePersonalForm(binding: FragmentPersonalBinding) {

    init {
        binding.spinnerIdentificationType.isEnabled = false
        binding.etIdNum.isEnabled = false
        binding.etExpiryDate.isEnabled = false
        binding.etIssueDate.isEnabled = false
        binding.btnNext.isEnabled = false
        binding.btnPrevious.isEnabled = false
        binding.ivUploadKyc.isClickable = false
        binding.btnAddKYC.isEnabled = false
        binding.spinnerVerifiedStatus.isEnabled = false
        binding.basicInfoLayout.ivUploadDobProof.isClickable = false
        binding.basicInfoLayout.btnGetOTP.isEnabled = false
        binding.basicInfoLayout.etAge.isEnabled = false
        binding.basicInfoLayout.etEmail.isEnabled = false
        binding.basicInfoLayout.etFatherFirstName.isEnabled = false
        binding.basicInfoLayout.etFatherMiddleName.isEnabled = false
        binding.basicInfoLayout.etFatherLastName.isEnabled = false
        binding.basicInfoLayout.etFirstName.isEnabled = false
        binding.basicInfoLayout.etLastName.isEnabled = false
        binding.basicInfoLayout.etMiddleName.isEnabled = false
        binding.basicInfoLayout.etSpouseFirstName.isEnabled = false
        binding.basicInfoLayout.etSpouseMiddleName.isEnabled = false
        binding.basicInfoLayout.etSpouseLastName.isEnabled = false
        binding.personalAddressLayout.cbSameAsCurrent.isClickable = false
        binding.basicInfoLayout.cbIncomeConsidered.isClickable = false
        binding.personalAddressLayout.etCurrentRentAmount.isEnabled = false
        binding.personalAddressLayout.etPermanentLandmark.isEnabled = false
        binding.personalAddressLayout.etPermanentRentAmount.isEnabled = false

        binding.personalAddressLayout.customCurrentZipAddressView.disableSelf()
        binding.personalAddressLayout.customPermanentZipAddressView.disableSelf()
    }
}