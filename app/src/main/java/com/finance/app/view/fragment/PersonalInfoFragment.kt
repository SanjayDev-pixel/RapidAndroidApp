package com.finance.app.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.model.Modals
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.SelectDate
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.Recycler.Adapter.AddKycAdapter
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter
import java.io.ByteArrayOutputStream
import java.io.IOException

class PersonalInfoFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentPersonalBinding
    private val frag = this
    private lateinit var mContext: Context
    private var kycAdapter: AddKycAdapter? = null

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private lateinit var kycList: ArrayList<Modals.AddKyc>
        private var image: Bitmap? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kycList = ArrayList()
        checkKycDataList()
        setDatePicker()
        setDropDownValue()
        setClickListeners()
    }

    private fun checkKycDataList() {
        if (kycList.isNullOrEmpty()) {
            binding.rcKYC.visibility = View.GONE
        }
    }

    private fun setClickListeners() {
        binding.ivUploadKyc.setOnClickListener {
            UploadData(frag, mContext)
        }
        binding.basicInfoLayout.btnVerifyOTP.setOnClickListener {
            Toast.makeText(mContext, binding.basicInfoLayout.otpView.text, Toast.LENGTH_SHORT).show()
        }
        binding.btnAddKYC.setOnClickListener {
            getKycData()
        }
        binding.addressLayout.cbSameAsCurrent.setOnClickListener {
            if (binding.addressLayout.cbSameAsCurrent.isChecked) {
                binding.addressLayout.llPermanentAddress.visibility = View.GONE
            } else {
                binding.addressLayout.llPermanentAddress.visibility = View.VISIBLE
            }
        }
    }

    private val kyc: Modals.AddKyc
        get() {
            return Modals.AddKyc(expiryDate = binding.etExpiryDate.text.toString(),
                    idNum = binding.etIdNum.text.toString(), kycImage = image,
                    issueDate = binding.etIssueDate.text.toString(),
                    verifiedStatus = binding.spinnerVerifiedStatus.selectedItem.toString(),
                    idType = binding.spinnerIdentificationType.selectedItem.toString())
        }

    private fun getKycData() {
        kycList.add(kyc)
        showKycGrid()
    }

    private fun showKycGrid() {
        binding.rcKYC.layoutManager = LinearLayoutManager(context)
        kycAdapter = AddKycAdapter(context!!, kycList)
        binding.rcKYC.adapter = kycAdapter
        binding.rcKYC.visibility = View.VISIBLE
    }

    private fun setDatePicker() {
        binding.basicInfoLayout.etDOB.setOnClickListener {
            SelectDate(binding.basicInfoLayout.etDOB, mContext)
        }
        binding.etIssueDate.setOnClickListener {
            SelectDate(binding.etIssueDate, mContext)
        }
        binding.etExpiryDate.setOnClickListener {
            SelectDate(binding.etExpiryDate, mContext)
        }
    }

    private fun setDropDownValue() {

        val identificationType = arrayOf("PAN", "UID", "Passport")
        val verifiedStatus = arrayOf("Positive", "Negative", "CNV")
        val dobProof = arrayOf("PAN", "Aadhar", "10th Marksheet", "Passport")
        val gender = arrayOf("Male", "Female", "Trans")
        val nationality = arrayOf("Indian", "American")
        val religion = arrayOf("Hindu", "Muslim", "Sikh", "Christian")
        val caste = arrayOf("General", "SC", "ST", "OBC")
        val qualification = arrayOf("Graduate", "Post Graduate", "Illiterate", "Middle School")
        val detailQualification = arrayOf("Science", "Arts", "Commerce", "Medical")
        val maritalStatus = arrayOf("Married", "Single", "Divorced", "Widow")
        val residenceType = arrayOf("Rent", "Owned", "Parental", "Employer")
        val addressProof = arrayOf("Aadhar UID Card", "Bank Account Statement", "Driving License")
        val districts = arrayOf("Haridwar", "Chamoli", "Roorkee")
        val state = arrayOf("Uttrakhand", "Delhi", "UP")

        val adapterIdentificationType = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, identificationType)
        adapterIdentificationType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterVerifiedStatus = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, verifiedStatus)
        adapterVerifiedStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterDobProof = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, dobProof)
        adapterDobProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterGender = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, gender)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterNationality = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, nationality)
        adapterNationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterReligion = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, religion)
        adapterReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterCaste = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, caste)
        adapterCaste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterQualification = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, qualification)
        adapterQualification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterDetailQualification = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, detailQualification)
        adapterDetailQualification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterMaritalStatus = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, maritalStatus)
        adapterMaritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val lists: ArrayList<DropdownMaster> = ArrayList()
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())
        lists.add(DropdownMaster())

        binding.spinnerIdentificationType.adapter = adapterIdentificationType
        binding.basicInfoLayout.spinnerDobProof.adapter = adapterDobProof
        binding.spinnerVerifiedStatus.adapter = adapterVerifiedStatus
        binding.basicInfoLayout.spinnerGender.adapter = adapterGender
        binding.basicInfoLayout.spinnerReligion.adapter = adapterReligion
        binding.basicInfoLayout.spinnerNationality.adapter = adapterNationality
        binding.basicInfoLayout.spinnerCaste.adapter = adapterCaste
        binding.basicInfoLayout.spinnerQualification.adapter = adapterQualification
        binding.basicInfoLayout.spinnerDetailQualification.adapter = adapterDetailQualification
        binding.basicInfoLayout.spinnerMaritalStatus.adapter = adapterMaritalStatus
        binding.addressLayout.spinnerCurrentAddressProof.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerCurrentDistrict.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerCurrentResidenceType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerCurrentState.adapter = GenericSpinnerAdapter(mContext, lists)

        binding.addressLayout.spinnerPermanentAddressProof.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentDistrict.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentState.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentResidenceType.adapter = GenericSpinnerAdapter(mContext, lists)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                        image = bitmap
                        binding.ivUploadKyc.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (requestCode == CAMERA) {
                val bitmap = data!!.extras!!.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
                val byteArray = stream.toByteArray()
                val thumbnail = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.size)
                binding.ivUploadKyc.setImageBitmap(thumbnail)
                image = thumbnail
                Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
            }
//            binding.rcKYC.adapter!!.notifyDataSetChanged()
        }
    }
}