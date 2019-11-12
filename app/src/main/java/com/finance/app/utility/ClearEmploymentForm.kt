package com.finance.app.utility

import android.content.Context
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.StatesMaster
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter

class ClearEmploymentForm(private val binding: FragmentEmploymentBinding, private val context: Context,
                          private val masterDropdown: AllMasterDropDown, private val state: List<StatesMaster>) {

    init {
        binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(context,masterDropdown.SubProfileSegment!!)
        binding.spinnerProfileSegment.adapter = MasterSpinnerAdapter(context,masterDropdown.ProfileSegment!!)
        binding.spinnerAllEarningMember.adapter = MasterSpinnerAdapter(context,masterDropdown.LivingStandardIndicators!!)
        clearSalaryForm()
        clearSenpForm()
    }

    private fun clearSalaryForm() {
        binding.layoutSalary.layoutAddress.etCity.text?.clear()
        binding.layoutSalary.etCompanyName.text?.clear()
        binding.layoutSalary.etJoiningDate.text?.clear()
        binding.layoutSalary.layoutAddress.etLandmark.text?.clear()
        binding.layoutSalary.etDesignation.text?.clear()
        binding.layoutSalary.layoutAddress.etPinCode.text?.clear()
        binding.layoutSalary.etEmployeeId.text?.clear()
        binding.layoutSalary.layoutAddress.etContactNum.text?.clear()
        binding.layoutSalary.etTotalExperience.text?.clear()
        binding.layoutSalary.etRetirementAge.text?.clear()
        binding.layoutSalary.layoutAddress.etAddress.text?.clear()
        binding.layoutSalary.etOfficialMailId.text?.clear()
        clearSalaryDropdown()
    }

    private fun clearSalaryDropdown() {
        binding.layoutSalary.spinnerIndustry.adapter = MasterSpinnerAdapter(context, masterDropdown.Industry!!)
        binding.layoutSalary.spinnerSector.adapter = MasterSpinnerAdapter(context, masterDropdown.Sector!!)
        binding.layoutSalary.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(context,state)
        binding.layoutSalary.spinnerEmploymentType.adapter = MasterSpinnerAdapter(context, masterDropdown.EmploymentType!!)
    }

    private fun clearSenpForm() {
        binding.layoutSenp.layoutAddress.etCity.text?.clear()
        binding.layoutSenp.etBusinessName.text?.clear()
        binding.layoutSenp.etIncorporationDate.text?.clear()
        binding.layoutSenp.layoutAddress.etLandmark.text?.clear()
        binding.layoutSenp.etBusinessVintage.text?.clear()
        binding.layoutSenp.layoutAddress.etPinCode.text?.clear()
        binding.layoutSenp.etGstRegistration.text?.clear()
        binding.layoutSenp.layoutAddress.etContactNum.text?.clear()
        binding.layoutSenp.layoutAddress.etAddress.text?.clear()
        clearSenpDropdown()
    }

    private fun clearSenpDropdown() {
        binding.layoutSenp.spinnerIndustry.adapter = MasterSpinnerAdapter(context, masterDropdown.Industry!!)
        binding.layoutSenp.spinnerConstitution.adapter = MasterSpinnerAdapter(context, masterDropdown.Constitution!!)
        binding.layoutSenp.spinnerBusinessSetUpType.adapter = MasterSpinnerAdapter(context, masterDropdown.BusinessSetupType!!)
        binding.layoutSenp.layoutAddress.spinnerState.adapter = StatesSpinnerAdapter(context,state)
    }
}