package com.finance.app.utility

import android.text.InputType
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
        binding.spinnerState.isClickable = false
        binding.spinnerDistrict.isEnabled = false
        binding.spinnerCity.isEnabled = false
        binding.spinnerTenantNocAvailable.isEnabled =  false
        binding.cbIsFirstProperty.isEnabled = false
        binding.etNumOfTenants.inputType = InputType.TYPE_NULL
        binding.etCashOcr.inputType = InputType.TYPE_NULL
        binding.etOcr.inputType = InputType.TYPE_NULL
        binding.etPropertyArea.inputType = InputType.TYPE_NULL
        binding.etMvProperty.inputType = InputType.TYPE_NULL
        binding.etAgreementValue.inputType = InputType.TYPE_NULL
        binding.etDistanceFromBranch.inputType = InputType.TYPE_NULL
        binding.etDistanceFromResidence.inputType = InputType.TYPE_NULL
        binding.etPropertyAddress.inputType = InputType.TYPE_NULL
        binding.etLandmark.inputType = InputType.TYPE_NULL
        binding.etPinCode.inputType = InputType.TYPE_NULL
        binding.btnPrevious.isEnabled = false
        binding.btnNext.isEnabled = false

    }
}