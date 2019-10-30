package com.finance.app.utility

import com.finance.app.databinding.FragmentBankDetailBinding

class ClearBankForm(binding: FragmentBankDetailBinding) {
    init {
        binding.etAccountNum.text?.clear()
        binding.etSalaryCreditedInSixMonths.text?.clear()
        binding.etAccountHolderName.text?.clear()
    }
}