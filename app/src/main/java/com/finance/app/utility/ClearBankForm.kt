package com.finance.app.utility

import android.content.Context
import android.view.View
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.customViews.CustomSpinnerViewTest

class ClearBankForm(binding: FragmentBankDetailBinding, context: Context, masterDropdown: AllMasterDropDown, bankName: CustomSpinnerViewTest<DropdownMaster>, accountType: CustomSpinnerViewTest<DropdownMaster>) {
    init {
        binding.etAccountNum.text?.clear()
        binding.etSalaryCreditedInSixMonths.text?.clear()
        binding.etAccountHolderName.text?.clear()
        binding.spinnerSalaryCredit.adapter = MasterSpinnerAdapter(context, masterDropdown.SalaryCredit!!)
        bankName.clearSpinner()
        accountType.clearSpinner()
//        binding.spinnerAccountType.adapter = MasterSpinnerAdapter(context, masterDropdown.AccountType!!)
//        binding.spinnerBankName.adapter = MasterSpinnerAdapter(context, masterDropdown.BankName!!)
        binding.inputLayoutSalaryCreditInSixMonth.visibility = View.GONE
    }
}