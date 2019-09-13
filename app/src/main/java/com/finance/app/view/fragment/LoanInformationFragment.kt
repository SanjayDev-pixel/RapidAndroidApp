package com.finance.app.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.Fragment
import com.finance.app.databinding.FragmentLoanInformationBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter
import com.google.android.material.textfield.TextInputLayout

class LoanInformationFragment : Fragment() {
    private lateinit var binding: FragmentLoanInformationBinding
    private val frag: Fragment = this

    companion object {
        private const val SELECT_PDF_CODE = 1
        private const val CLICK_IMAGE_CODE = 2
        private const val SELECT_IMAGE_CODE = 3
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
            UploadData(frag, context!!)
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

        val lists: ArrayList<DropdownMaster> = ArrayList()
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())

        binding.spinnerLoanProduct.adapter = GenericSpinnerAdapter(context!!, lists)
        binding.spinnerLoanScheme.adapter = adapterLoanScheme
        binding.spinnerLoanPurpose.adapter = adapterLoanPurpose
        binding.spinnerInterestType.adapter = adapterInterestType
        binding.spinnerPartnerName.adapter = adapterPartnerName
        binding.spinnerSourcingChannelPartner.adapter = adapterSourcingChannelPartner
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val returnUri = data!!.data
            when (requestCode) {
                SELECT_PDF_CODE -> {
                    Log.i("URI: ", returnUri?.toString())
                    binding.ivThumbnail.visibility = View.GONE
                    binding.ivPdf.visibility = View.VISIBLE
                }
                SELECT_IMAGE_CODE -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    binding.ivThumbnail.setImageBitmap(bitmap)
                }
                CLICK_IMAGE_CODE -> {
                    val thumbnail = data.extras!!.get("data") as Bitmap
                    binding.ivThumbnail.setImageBitmap(thumbnail)
                }
            }
        }
    }
}