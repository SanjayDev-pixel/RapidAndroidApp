package com.finance.app.utility

import com.finance.app.databinding.FragmentPersonalBinding

class ClearPersonalForm(binding: FragmentPersonalBinding) {

    init {
        binding.etIdNum.text?.clear()
        binding.etIssueDate.text?.clear()
        binding.etExpiryDate.text?.clear()
        binding.basicInfoLayout.otpView.text?.clear()
        binding.basicInfoLayout.etNumOfDependent.text?.clear()
        binding.basicInfoLayout.etAlternateNum.text?.clear()
        binding.basicInfoLayout.etMobile.text?.clear()
        binding.basicInfoLayout.etAge.text?.clear()
        binding.basicInfoLayout.etLastName.text?.clear()
        binding.basicInfoLayout.etMiddleName.text?.clear()
        binding.basicInfoLayout.etFirstName.text?.clear()
        binding.basicInfoLayout.etSpouseLastName.text?.clear()
        binding.basicInfoLayout.etSpouseFirstName.text?.clear()
        binding.basicInfoLayout.etSpouseMiddleName.text?.clear()
        binding.basicInfoLayout.etFatherFirstName.text?.clear()
        binding.basicInfoLayout.etFatherMiddleName.text?.clear()
        binding.basicInfoLayout.etFatherLastName.text?.clear()
        binding.basicInfoLayout.etEmail.text?.clear()
        binding.basicInfoLayout.etDOB.text?.clear()
        binding.personalAddressLayout.etPermanentRentAmount.text?.clear()
        binding.personalAddressLayout.etPermanentPinCode.text?.clear()
        binding.personalAddressLayout.etPermanentLandmark.text?.clear()
        binding.personalAddressLayout.etPermanentStaying.text?.clear()
        binding.personalAddressLayout.etCurrentRentAmount.text?.clear()
        binding.personalAddressLayout.etPermanentCity.text?.clear()
        binding.personalAddressLayout.etPermanentAddress.text?.clear()
        binding.personalAddressLayout.etCurrentStaying.text?.clear()
        binding.personalAddressLayout.etCurrentPinCode.text?.clear()
        binding.personalAddressLayout.etCurrentLandmark.text?.clear()
        binding.personalAddressLayout.etCurrentAddress.text?.clear()
        binding.personalAddressLayout.etCurrentCity.text?.clear()
    }
}