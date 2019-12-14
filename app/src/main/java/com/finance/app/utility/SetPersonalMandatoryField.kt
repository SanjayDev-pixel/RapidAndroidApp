package com.finance.app.utility

import com.finance.app.databinding.FragmentPersonalBinding

class SetPersonalMandatoryField(binding: FragmentPersonalBinding) {

    init {
        ShowAsMandatory(binding.basicInfoLayout.inputLayoutFirstName)
        ShowAsMandatory(binding.basicInfoLayout.inputLayoutDob)
        ShowAsMandatory(binding.basicInfoLayout.inputLayoutFatherFirstName)
        ShowAsMandatory(binding.basicInfoLayout.inputLayoutSpouseFirstName)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutCurrentAddress)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutPermanentAddress)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutCurrentLandmark)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutPermanentLandmark)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutCurrentStaying)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutPermanentStaying)
        ShowAsMandatory(binding.personalAddressLayout.inputLayoutCurrentRentAmount)
    }
}