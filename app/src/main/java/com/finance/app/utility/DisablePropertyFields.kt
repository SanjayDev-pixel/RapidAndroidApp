package com.finance.app.utility

import com.finance.app.databinding.FragmentPropertyInfoBinding

class DisablePropertyFields(binding: FragmentPropertyInfoBinding) {

    init {
        binding.spinnerUnitType.isEnabled = false
        binding.spinnerOwnership.isEnabled = false
        binding.spinnerPropertyNature.isEnabled = false
        binding.spinnerTransactionCategory.isEnabled = false
        binding.spinnerOwnedProperty.isEnabled = false
        binding.spinnerOccupiedBy.isEnabled = false
        binding.spinnerState.isEnabled = false
        binding.spinnerDistrict.isEnabled = false
        binding.spinnerCity.isEnabled = false
        binding.etNumOfTenants.isClickable = false
        binding.etCashOcr.isClickable = false
        binding.etOcr.isClickable = false
        binding.etPropertyArea.isClickable = false
        binding.etMvProperty.isClickable = false
        binding.etAgreementValue.isClickable = false
        binding.etDistanceFromBranch.isClickable = false
        binding.etDistanceFromResidence.isClickable = false
        binding.etPropertyAddress.isClickable = false
        binding.etLandmark.isClickable = false
        binding.etPinCode.isClickable = false

    }
}