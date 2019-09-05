package com.finance.app.view.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.finance.app.databinding.FragmentPersonalBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

@Suppress("NAME_SHADOWING")
class PersonalInfoFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentPersonalBinding

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDatePicker()
        setDropDownValue()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.ivUploadKyc.setOnClickListener {
            getPicture()
        }
        binding.btnVerifyOTP.setOnClickListener {
            Toast.makeText(requireContext(), binding.otpView.text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPicture() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
        val pictureDialog = AlertDialog.Builder(
                requireContext())
        pictureDialog.setTitle("Select Action")
        pictureDialog.setItems(items) { dialog, item ->
            when (item) {
                0 -> takePicture()
                1 -> choosePicture()
                2 -> dialog.dismiss()
            }
        }
        pictureDialog.show()
    }

    private fun takePicture() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
            this.startActivityForResult(cameraIntent, CAMERA)
        }
    }

    private fun choosePicture() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        if (galleryIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(galleryIntent, GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
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
                Toast.makeText(requireContext(), "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDatePicker() {
        binding.basicInfoLayout.etDOB.setOnClickListener {
            clickDatePicker(binding.basicInfoLayout.etDOB)
        }
        binding.etIssueDate.setOnClickListener {
            clickDatePicker(binding.etIssueDate)
        }
        binding.etExpiryDate.setOnClickListener {
            clickDatePicker(binding.etExpiryDate)
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

    }

    private fun clickDatePicker(et: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
            et.setText("$day/ ${month + 1}/ $year")
        }, year, month, day)
        dialog.show()
    }
}