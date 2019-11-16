package com.finance.app.utility

import com.finance.app.databinding.FragmentBankDetailBinding

class SetBankDetailMandatoryFiled(binding: FragmentBankDetailBinding) {

    init {
        ShowAsMandatory(binding.inputLayoutAccountHolderName)
        ShowAsMandatory(binding.inputLayoutAccountNum)
    }
}