package com.finance.app.utility

import com.finance.app.databinding.FragmentPersonalBinding

class ClearPersonalForm(private val binding: FragmentPersonalBinding) {

    init {
        binding.etIdNum.text?.clear()
        binding.etIssueDate.text?.clear()
        binding.etExpiryDate.text?.clear()
    }
}