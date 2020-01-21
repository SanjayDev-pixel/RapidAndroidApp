package com.finance.app.utility

import android.content.Context
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.StatesMaster
import com.finance.app.view.adapters.recycler.spinner.CitySpinnerAdapter
import com.finance.app.view.adapters.recycler.spinner.DistrictSpinnerAdapter
import com.finance.app.view.adapters.recycler.spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.spinner.StatesSpinnerAdapter

class ClearEmploymentForm(private val binding: FragmentEmploymentBinding, private val context: Context,
                          private val masterDropdown: AllMasterDropDown, private val state: List<StatesMaster>) {

    fun clearAll() {
        binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(context,masterDropdown.SubProfileSegment!!)
        binding.spinnerProfileSegment.adapter = MasterSpinnerAdapter(context,masterDropdown.ProfileSegment!!)
        clearSalaryForm()
        clearSenpForm()
    }

    fun clearSalaryForm() {
        binding.layoutSalary.cbIsPensioner.isChecked = false
        binding.layoutSalary.etCompanyName.text?.clear()
        binding.layoutSalary.etJoiningDate.text?.clear()
        binding.layoutSalary.layoutAddress.etLandmark.text?.clear()
        binding.layoutSalary.etDesignation.text?.clear()
        binding.layoutSalary.etEmployeeId.text?.clear()
        binding.layoutSalary.etTotalExperience.text?.clear()
        binding.layoutSalary.etOfficialMailId.text?.clear()
        binding.layoutSalary.etRetirementAge.text?.clear()
        binding.layoutSalary.layoutAddress.etAddress.text?.clear()
        binding.layoutSalary.layoutAddress.etContactNum.text?.clear()
        binding.layoutSalary.layoutAddress.etPinCode.text?.clear()
        binding.layoutSalary.etNetIncome.text?.clear()
        binding.layoutSalary.etGrossIncome.text?.clear()
        binding.layoutSalary.etDeduction.text?.clear()
        clearSalaryDropdown()
    }

    private fun clearSalaryDropdown() {
        binding.layoutSalary.spinnerIndustry.adapter = MasterSpinnerAdapter(context, masterDropdown.Industry!!)
        binding.layoutSalary.spinnerSector.adapter = MasterSpinnerAdapter(context, masterDropdown.Sector!!)
        binding.layoutSalary.spinnerEmploymentType.adapter = MasterSpinnerAdapter(context, masterDropdown.EmploymentType!!)
        binding.layoutSalary.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(context, state)
        binding.layoutSalary.layoutAddress.spinnerDistrict.adapter = DistrictSpinnerAdapter(context, ArrayList())
        binding.layoutSalary.layoutAddress.spinnerCity.adapter = CitySpinnerAdapter(context,ArrayList())
        binding.layoutSalary.layoutAddress.spinnerState.isEnabled = true
        binding.layoutSalary.layoutAddress.spinnerDistrict.isEnabled = true
        binding.layoutSalary.layoutAddress.spinnerCity.isEnabled = true
    }

    fun clearSenpForm() {
        binding.layoutSenp.cbAllEarningMember.isChecked = false
        binding.layoutSenp.etBusinessName.text?.clear()
        binding.layoutSenp.etIncorporationDate.text?.clear()
        binding.layoutSenp.layoutAddress.etLandmark.text?.clear()
        binding.layoutSenp.etBusinessVintage.text?.clear()
        binding.layoutSenp.etGstRegistration.text?.clear()
        binding.layoutSenp.layoutAddress.etContactNum.text?.clear()
        binding.layoutSenp.layoutAddress.etAddress.text?.clear()
        binding.layoutSenp.etAverageMonthlyIncome.text?.clear()
        binding.layoutSenp.layoutAddress.etPinCode.text?.clear()
        binding.layoutSenp.etLastYearIncome.text?.clear()
        binding.layoutSenp.etCurrentYearIncome.text?.clear()
        binding.layoutSenp.etMonthlyIncome.text?.clear()
        clearSenpDropdown()
    }

    private fun clearSenpDropdown() {
        binding.layoutSenp.spinnerIndustry.adapter = MasterSpinnerAdapter(context, masterDropdown.Industry!!)
        binding.layoutSenp.spinnerConstitution.adapter = MasterSpinnerAdapter(context, masterDropdown.Constitution!!)
        binding.layoutSenp.spinnerBusinessSetUpType.adapter = MasterSpinnerAdapter(context, masterDropdown.BusinessSetupType!!)
        binding.layoutSenp.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(context,state)
        binding.layoutSenp.layoutAddress.spinnerDistrict.adapter = DistrictSpinnerAdapter(context, ArrayList())
        binding.layoutSenp.layoutAddress.spinnerCity.adapter = CitySpinnerAdapter(context,ArrayList())
        binding.layoutSenp.layoutAddress.spinnerState.isEnabled = true
        binding.layoutSenp.layoutAddress.spinnerDistrict.isEnabled = true
        binding.layoutSenp.layoutAddress.spinnerCity.isEnabled = true
    }
}