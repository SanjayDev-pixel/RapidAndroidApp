package com.finance.app.utility

import com.finance.app.databinding.FragmentPropertyBinding

class SetPropertyMandatoryField(binding: FragmentPropertyBinding) {

    init {
        ShowAsMandatory(binding.inputLayoutLandmark)
        ShowAsMandatory(binding.inputLayoutPinCode)
        ShowAsMandatory(binding.inputLayoutPropertyAddress)
    }
}