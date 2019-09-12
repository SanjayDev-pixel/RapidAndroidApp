package com.finance.app.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.Fragment
import com.example.motobeans.educationapp.app.view.adapter.arrayadapter.GenericSpinnerAdapter
import com.finance.app.databinding.FragmentLoanInformationBinding
import com.finance.app.model.Modals
import com.google.android.material.textfield.TextInputLayout

class LoanInformationFragment : Fragment() {
    private lateinit var binding: FragmentLoanInformationBinding

    companion object {
        private const val PICK_FILE_RESULT_CODE = 1
        private const val CLICK_IMAGE_RESULT_CODE = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoanInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownValue()
        setMandatoryField()
        binding.ivUploadForm.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "file/*"
            startActivityForResult(intent, PICK_FILE_RESULT_CODE)
        }
    }

    private fun setDropDownValue() {

        val loanProducts = arrayOf("Home Loan", "LAP")
        val loanPurpose = arrayOf("House Purchase", "Flat Purchase", "Plot Purchase",
                "Improvement", "Plot Purchase + Construction", "HL- BT", "Top up", "HL-BT + Top Up",
                "LAP", "LAP BT+ Top Up", "BT+ Extension", "Seller BT")
        val loanScheme = arrayOf("Regular ITR", "Bank/Cheque")
        val interestType = arrayOf("Floating", "Fixed")
        val sourceChannelPartner = arrayOf("DSA", "DIRECT", "CORPORATE DSA")
        val partnerName = arrayOf("Channel Name", "Direct Bank")

        val loanProductAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, loanProducts)
        loanProductAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterLoanPurpose = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, loanPurpose)
        adapterLoanPurpose.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterLoanScheme = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, loanScheme)
        adapterLoanScheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterInterestType = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, interestType)
        adapterInterestType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterSourcingChannelPartner = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, sourceChannelPartner)
        adapterSourcingChannelPartner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterPartnerName = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, partnerName)
        adapterPartnerName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val lists :ArrayList<Modals.SpinnerValue> = ArrayList()
        lists.add(mCompany)
        lists.add(mCompany)
        lists.add(mCompany)

        binding.spinnerLoanProduct.adapter = GenericSpinnerAdapter(context!!, lists)
        binding.spinnerLoanScheme.adapter = adapterLoanScheme
        binding.spinnerLoanPurpose.adapter = adapterLoanPurpose
        binding.spinnerInterestType.adapter = adapterInterestType
        binding.spinnerPartnerName.adapter = adapterPartnerName
        binding.spinnerSourcingChannelPartner.adapter = adapterSourcingChannelPartner
    }

    private val mCompany: Modals.SpinnerValue
        get() {
            return Modals.SpinnerValue("lajv", 1)
        }


    private fun setMandatoryField() {
        binding.inputLayoutAmount.isMandatory()
        binding.inputLayoutTenure.isMandatory()
        binding.inputLayoutEmi.isMandatory()
    }

    private fun TextInputLayout.isMandatory() {
        hint = buildSpannedString {
            append(hint)
            color(Color.RED) { append(" *") } // Mind the space prefix.
        }
    }
}
