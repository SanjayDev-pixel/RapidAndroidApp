package com.finance.app.utility

import android.content.Context
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter

class ClearBankForm(binding: FragmentBankDetailBinding, context: Context, masterDropdown: AllMasterDropDown) {
    init {
        binding.etAccountNum.text?.clear()
        binding.etSalaryCreditedInSixMonths.text?.clear()
        binding.etAccountHolderName.text?.clear()
        binding.spinnerSalaryCredit.adapter = MasterSpinnerAdapter(context, masterDropdown.SalaryCredit!!)
        binding.spinnerAccountType.adapter = MasterSpinnerAdapter(context, masterDropdown.AccountType!!)
        binding.spinnerBankName.adapter = MasterSpinnerAdapter(context, masterDropdown.BankName!!)
    }
}