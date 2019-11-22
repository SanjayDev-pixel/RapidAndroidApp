package com.finance.app.utility

import android.content.Context
import android.view.View
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.StatesMaster
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.Spinner.StatesSpinnerAdapter

class ClearPersonalForm(private val binding: FragmentPersonalBinding, private val context: Context,
                        private val masterDropdown: AllMasterDropDown, private val state: List<StatesMaster>) {

    init {
        binding.etIdNum.text?.clear()
        binding.etIssueDate.text?.clear()
        binding.etExpiryDate.text?.clear()
        binding.basicInfoLayout.cbIncomeConsidered.isChecked = false
        clearBasicInfoForm()
        clearAddressInfoForm()
    }

    private fun clearAddressInfoForm() {
        binding.personalAddressLayout.etPermanentRentAmount.text?.clear()
        binding.personalAddressLayout.etPermanentPinCode.text?.clear()
        binding.personalAddressLayout.etPermanentLandmark.text?.clear()
        binding.personalAddressLayout.etPermanentStaying.text?.clear()
        binding.personalAddressLayout.etCurrentRentAmount.text?.clear()
        binding.personalAddressLayout.etPermanentAddress.text?.clear()
        binding.personalAddressLayout.etCurrentStaying.text?.clear()
        binding.personalAddressLayout.etCurrentPinCode.text?.clear()
        binding.personalAddressLayout.etCurrentLandmark.text?.clear()
        binding.personalAddressLayout.etCurrentAddress.text?.clear()
        binding.personalAddressLayout.cbSameAsCurrent.isChecked = false
        clearAddressInfoDropdown()
    }

    private fun clearAddressInfoDropdown() {
        binding.personalAddressLayout.llPermanentAddress.visibility = View.VISIBLE
        binding.personalAddressLayout.spinnerCurrentState.adapter = StatesSpinnerAdapter(context, state)
        binding.personalAddressLayout.spinnerPermanentState.adapter = StatesSpinnerAdapter(context, state)
        binding.personalAddressLayout.spinnerPermanentResidenceType.adapter = MasterSpinnerAdapter(context, masterDropdown.ResidenceType!!)
        binding.personalAddressLayout.spinnerCurrentResidenceType.adapter = MasterSpinnerAdapter(context, masterDropdown.ResidenceType!!)
        binding.personalAddressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(context, masterDropdown.AddressProof!!)
        binding.personalAddressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(context, masterDropdown.AddressProof!!)

    }

    private fun clearBasicInfoForm() {
        binding.basicInfoLayout.otpView.text?.clear()
        binding.basicInfoLayout.etNumOfDependent.text?.clear()
        binding.basicInfoLayout.etAlternateNum.text?.clear()
        binding.basicInfoLayout.etMobile.text?.clear()
        binding.basicInfoLayout.etAge.text?.clear()
        binding.basicInfoLayout.etLastName.text?.clear()
        binding.basicInfoLayout.etMiddleName.text?.clear()
        binding.basicInfoLayout.etFirstName.text?.clear()
        binding.basicInfoLayout.etSpouseLastName.text?.clear()
        binding.basicInfoLayout.etSpouseFirstName.text?.clear()
        binding.basicInfoLayout.etSpouseMiddleName.text?.clear()
        binding.basicInfoLayout.etFatherFirstName.text?.clear()
        binding.basicInfoLayout.etFatherMiddleName.text?.clear()
        binding.basicInfoLayout.etFatherLastName.text?.clear()
        binding.basicInfoLayout.etEmail.text?.clear()
        binding.basicInfoLayout.etDOB.text?.clear()
        clearBasicInfoDropdown()
    }

    private fun clearBasicInfoDropdown() {
        binding.basicInfoLayout.spinnerDobProof.adapter = MasterSpinnerAdapter(context, masterDropdown.DOBProof!!)
        binding.basicInfoLayout.spinnerLivingStandard.adapter = MasterSpinnerAdapter(context, masterDropdown.LivingStandardIndicators!!)
        binding.basicInfoLayout.spinnerRelationship.adapter = MasterSpinnerAdapter(context, masterDropdown.Relationship!!)
        binding.basicInfoLayout.spinnerMaritalStatus.adapter = MasterSpinnerAdapter(context, masterDropdown.MaritalStatus!!)
        binding.basicInfoLayout.spinnerDetailQualification.adapter = MasterSpinnerAdapter(context, masterDropdown.DetailQualification!!)
        binding.basicInfoLayout.spinnerQualification.adapter = MasterSpinnerAdapter(context, masterDropdown.Qualification!!)
        binding.basicInfoLayout.spinnerCaste.adapter = MasterSpinnerAdapter(context, masterDropdown.Caste!!)
        binding.basicInfoLayout.spinnerReligion.adapter = MasterSpinnerAdapter(context, masterDropdown.Religion!!)
        binding.basicInfoLayout.spinnerNationality.adapter = MasterSpinnerAdapter(context, masterDropdown.Nationality!!)
        binding.basicInfoLayout.spinnerGender.adapter = MasterSpinnerAdapter(context, masterDropdown.Gender!!)
    }
}