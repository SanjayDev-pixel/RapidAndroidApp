package com.finance.app.utility

import com.finance.app.databinding.FragmentBankDetailBinding

class DisableBankForm(private val binding: FragmentBankDetailBinding) {

    init {
        disableBankFields(binding)
    }

    private fun disableBankFields(binding: FragmentBankDetailBinding) {
        binding.spinnerBankName.isEnabled = false
        binding.spinnerAccountType.isEnabled = false
        binding.spinnerSalaryCredit.isEnabled = false
        binding.etAccountNum.isEnabled = false
        binding.etAccountHolderName.isEnabled = false
        binding.ivUploadStatement.isClickable = false
    }
}