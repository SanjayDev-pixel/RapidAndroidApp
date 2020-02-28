package com.finance.app.utility

import com.finance.app.databinding.FragmentLoanInformationBinding

class DisableLoanInfoForm(binding: FragmentLoanInformationBinding) {

    init {
//        binding.spinnerLoanScheme.isEnabled = false
//        binding.spinnerInterestType.isEnabled = false
//        binding.spinnerSourcingChannelPartner.isEnabled = false
//        binding.spinnerPartnerName.isEnabled = false
        binding.etAmountRequest.isEnabled = false
        binding.etTenure.isEnabled = false
        binding.etEmi.isEnabled = false
        binding.cbPropertySelected.isClickable = false
        //binding.btnNext.isEnabled = false

    }
}