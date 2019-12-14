package com.finance.app.utility

import com.finance.app.databinding.FragmentPersonalBinding

class DisablePersonalForm(private val binding: FragmentPersonalBinding) {

    init {
        disablePersonalFields(binding)
    }

    private fun disablePersonalFields(binding: FragmentPersonalBinding) {
        binding.spinnerIdentificationType.isEnabled = false
        binding.etIdNum.isEnabled = false
        binding.etExpiryDate.isEnabled = false
        binding.etIssueDate.isEnabled = false
        binding.btnNext.isEnabled = false
        binding.btnPrevious.isEnabled = false
        binding.ivUploadKyc.isClickable = false
        binding.btnAddKYC.isEnabled = false
        binding.spinnerVerifiedStatus.isEnabled = false
        binding.basicInfoLayout.ivUploadDobProof.isClickable = false
        binding.basicInfoLayout.btnGetOTP.isEnabled = false
        binding.basicInfoLayout.etAge.isEnabled = false
        binding.basicInfoLayout.etEmail.isEnabled = false
        binding.basicInfoLayout.etFatherFirstName.isEnabled = false
        binding.basicInfoLayout.etFatherMiddleName.isEnabled = false
        binding.basicInfoLayout.etFatherLastName.isEnabled = false
        binding.basicInfoLayout.etFirstName.isEnabled = false
        binding.basicInfoLayout.etLastName.isEnabled = false
        binding.basicInfoLayout.etMiddleName.isEnabled = false
        binding.basicInfoLayout.etSpouseFirstName.isEnabled = false
        binding.basicInfoLayout.etSpouseMiddleName.isEnabled = false
        binding.basicInfoLayout.etSpouseLastName.isEnabled = false
        binding.basicInfoLayout.spinnerReligion.isEnabled = false
        binding.basicInfoLayout.spinnerCaste.isEnabled = false
        binding.basicInfoLayout.etNumOfDependent.isEnabled = false
        binding.basicInfoLayout.spinnerDobProof.isEnabled = false
        binding.basicInfoLayout.spinnerDetailQualification.isEnabled = false
        binding.basicInfoLayout.spinnerLivingStandard.isEnabled = false
        binding.basicInfoLayout.spinnerRelationship.isEnabled = false
        binding.basicInfoLayout.spinnerMaritalStatus.isEnabled = false
        binding.basicInfoLayout.spinnerNationality.isEnabled = false
        binding.basicInfoLayout.spinnerQualification.isEnabled = false
        binding.basicInfoLayout.etMobile.isEnabled = false
        binding.basicInfoLayout.spinnerGender.isEnabled = false
        binding.basicInfoLayout.etDOB.isEnabled = false
        binding.basicInfoLayout.etAlternateNum.isEnabled = false
        binding.basicInfoLayout.etNumOfEarningMember.isEnabled = false
        binding.personalAddressLayout.etCurrentAddress.isEnabled = false
        binding.personalAddressLayout.etPermanentAddress.isEnabled = false
        binding.personalAddressLayout.etCurrentLandmark.isEnabled = false
        binding.personalAddressLayout.etCurrentPinCode.isEnabled = false
        binding.personalAddressLayout.etPermanentPinCode.isEnabled = false
        binding.personalAddressLayout.etPermanentStaying.isEnabled = false
        binding.personalAddressLayout.etCurrentStaying.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentResidenceType.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentState.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentDistrict.isEnabled = false
        binding.personalAddressLayout.spinnerPermanentAddressProof.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentAddressProof.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentDistrict.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentState.isEnabled = false
        binding.personalAddressLayout.cbSameAsCurrent.isClickable = false
        binding.basicInfoLayout.cbIncomeConsidered.isClickable = false
        binding.personalAddressLayout.etCurrentRentAmount.isEnabled = false
        binding.personalAddressLayout.spinnerCurrentResidenceType.isEnabled = false
        binding.personalAddressLayout.etPermanentLandmark.isEnabled = false
        binding.personalAddressLayout.etPermanentRentAmount.isEnabled = false
    }
}