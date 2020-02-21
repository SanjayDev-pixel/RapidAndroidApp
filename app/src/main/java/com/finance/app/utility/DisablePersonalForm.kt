package com.finance.app.utility

import android.text.InputType
import com.finance.app.databinding.LayoutCustomViewPersonalBinding

class DisablePersonalForm(binding: LayoutCustomViewPersonalBinding) {

    init {
        binding.basicInfoLayout.etFirstName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etMiddleName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etLastName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etDOB.setOnTouchListener { _, _ -> true }
        binding.basicInfoLayout.etAge.setOnTouchListener { _, _ -> true }
        binding.basicInfoLayout.etMobile.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.btnGetOTP.isClickable = false
//        binding.basicInfoLayout.layoutDobProof.isClickable = true
        //will Disable children all click view...
//        for (i in 0 until binding.basicInfoLayout.layoutDobProof.childCount)
//            binding.basicInfoLayout.layoutDobProof.getChildAt(i).isEnabled = false//Only has spinner in this...

        binding.basicInfoLayout.etFatherFirstName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etFatherMiddleName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etFatherLastName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etSpouseFirstName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etSpouseMiddleName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etSpouseLastName.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etEmail.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.etAge.inputType = InputType.TYPE_NULL
        binding.basicInfoLayout.btnGetOTP.inputType = InputType.TYPE_NULL

        binding.basicInfoLayout.ivUploadDobProof.isClickable = false
//        binding.spinnerIdentificationType.isEnabled = false
//        binding.etIdNum.inputType = InputType.TYPE_NULL
//        binding.etExpiryDate.inputType = InputType.TYPE_NULL
//        binding.etIssueDate.isEnabled = false
//        binding.ivUploadKyc.isClickable = false
        binding.btnAddKYC.isClickable = false
//        binding.spinnerVerifiedStatus.isEnabled = false

        binding.personalAddressLayout.cbSameAsCurrent.isClickable = false
        binding.basicInfoLayout.cbIncomeConsidered.isClickable = false
        binding.personalAddressLayout.etCurrentRentAmount.inputType = InputType.TYPE_NULL
        binding.personalAddressLayout.etPermanentLandmark.inputType = InputType.TYPE_NULL
        binding.personalAddressLayout.etPermanentRentAmount.inputType = InputType.TYPE_NULL

        binding.personalAddressLayout.customCurrentZipAddressView.disableSelf()
        binding.personalAddressLayout.customPermanentZipAddressView.disableSelf()
    }
}