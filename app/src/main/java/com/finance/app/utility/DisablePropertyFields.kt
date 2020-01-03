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
        binding.etNumOfTenants.isEnabled = false
        binding.etCashOcr.isEnabled = false
        binding.etOcr.isEnabled = false
        binding.etPropertyArea.isEnabled = false
        binding.etMvProperty.isEnabled = false
        binding.etAgreementValue.isEnabled = false
        binding.etDistanceFromBranch.isEnabled = false
        binding.etDistanceFromResidence.isEnabled = false
        binding.etPropertyAddress.isEnabled = false
        binding.etLandmark.isEnabled = false
        binding.etPinCode.isEnabled = false
        binding.btnPrevious.isEnabled = false
        binding.btnNext.isEnabled = false

    }
}