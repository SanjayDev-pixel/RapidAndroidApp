package com.finance.app.utility

import com.finance.app.databinding.FragmentReferenceBinding

class ClearReferenceForm(private val binding: FragmentReferenceBinding) {

    init {
        binding.etContactNum.text?.clear()
        binding.etName.text?.clear()
        binding.etKnownSince.text?.clear()
        binding.referenceAddressLayout.etDistrict.text?.clear()
        binding.referenceAddressLayout.etCity.text?.clear()
        binding.referenceAddressLayout.etPinCode.text?.clear()
        binding.referenceAddressLayout.etAddress1.text?.clear()
        binding.referenceAddressLayout.etAddress2.text?.clear()
        binding.referenceAddressLayout.etLandmark.text?.clear()
        binding.referenceAddressLayout.etState.text?.clear()
    }
}