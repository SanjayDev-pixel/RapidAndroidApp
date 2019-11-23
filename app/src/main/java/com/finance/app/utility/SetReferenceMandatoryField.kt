package com.finance.app.utility

import com.finance.app.databinding.FragmentReferenceBinding

class SetReferenceMandatoryField(binding: FragmentReferenceBinding) {

    init {
        ShowAsMandatory(binding.inputLayoutName)
        ShowAsMandatory(binding.referenceAddressLayout.inputLayoutAddress1)
        ShowAsMandatory(binding.referenceAddressLayout.inputLayoutLandmark)
        ShowAsMandatory(binding.referenceAddressLayout.inputLayoutPinCode)
    }
}