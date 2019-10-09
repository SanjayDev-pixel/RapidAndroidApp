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
        binding.addressLayout.etPermanentRentAmount.text?.clear()
        binding.addressLayout.etPermanentPinCode.text?.clear()
        binding.addressLayout.etPermanentLandmark.text?.clear()
        binding.addressLayout.etPermanentStaying.text?.clear()
        binding.addressLayout.etCurrentRentAmount.text?.clear()
        binding.addressLayout.etPermanentCity.text?.clear()
        binding.addressLayout.etPermanentAddress.text?.clear()
        binding.addressLayout.etCurrentStaying.text?.clear()
        binding.addressLayout.etCurrentPinCode.text?.clear()
        binding.addressLayout.etCurrentLandmark.text?.clear()
        binding.addressLayout.etCurrentAddress.text?.clear()
        binding.addressLayout.etCurrentCity.text?.clear()
    }
}