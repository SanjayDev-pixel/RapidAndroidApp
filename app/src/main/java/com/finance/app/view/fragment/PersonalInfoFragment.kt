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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.model.Modals
import com.finance.app.model.Modals.AddKyc
import com.finance.app.model.Modals.ApplicantPersonal
import com.finance.app.persistence.model.AddressDetail
import com.finance.app.persistence.model.ContactDetail
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.ClearPersonalForm
import com.finance.app.utility.SelectDate
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.recycler.adapter.AddKycAdapter
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class PersonalInfoFragment : Fragment(), PersonalApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentPersonalBinding
    private val frag = this
    private lateinit var mContext: Context
    private var kycAdapter: AddKycAdapter? = null
    private var applicantAdapterPersonal: PersonalApplicantsAdapter? = null
    private var personalAddressDetail: ArrayList<AddressDetail>? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private var coApplicant = 1
        private var applicantsList: ArrayList<PersonalApplicantsModel>? = null
        private lateinit var applicant: PersonalApplicantsModel
        private lateinit var kycList: ArrayList<AddKyc>
        private lateinit var applicantTab: ArrayList<String>
        private var image: Bitmap? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPersonalBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ArchitectureApp.instance.component.inject(this)
        kycList = ArrayList()
        applicantTab = ArrayList()
        checkKycDataList()
        setCoApplicants()
        setDatePicker()
        setDropDownValue()
        setClickListeners()
        checkPropertySelection()
    }

    private fun checkPropertySelection() {
        val selected = sharedPreferences.getPropertySelection()
        if (!selected) {
            Toast.makeText(context, "Property not selected in Loan Information",
                    Toast.LENGTH_SHORT).show()
            disableAllFields()
        }
    }

    private fun disableAllFields() {
        formValidation.disablePersonalFields(binding)
    }

    private fun setCoApplicants() {
        applicantTab.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapterPersonal = PersonalApplicantsAdapter(context!!, applicantTab)
        binding.rcApplicants.adapter = applicantAdapterPersonal
        applicantAdapterPersonal!!.setOnItemClickListener(this)
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
        }

        binding.btnAddKYC.setOnClickListener {
            getKycData()
            clearKycData()
        }

        binding.basicInfoLayout.ivUploadDobProof.setOnClickListener {
            UploadData(frag, mContext)
        }

        binding.btnAddApplicant.setOnClickListener {
            onAddCoApplicantClick()
        }

        binding.btnSaveAndContinue.setOnClickListener{
            setIncomeConsidered()
            sharedPreferences.savePersonalInfoForApplicants(allApplicants)
        }

        binding.personalAddressLayout.cbSameAsCurrent.setOnClickListener {
            if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.GONE
            } else {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.VISIBLE
            }
        }
    }

    private val allApplicants: ApplicantPersonal
        get() {
            return ApplicantPersonal(applicantsList!!)
        }

    private fun setIncomeConsidered() {
        if (binding.cbIncomeConsidered.isChecked) {
            sharedPreferences.setIncomeConsideration("Yes")
        } else {
            sharedPreferences.setIncomeConsideration("No")
        }
    }

    private fun clearKycData() {
        binding.spinnerIdentificationType.isSelected = false
        binding.etIdNum.text?.clear()
        binding.etIssueDate.text?.clear()
        binding.etExpiryDate.text?.clear()
    }

    private val kyc: AddKyc
        get() {
            return AddKyc(expiryDate = binding.etExpiryDate.text.toString(),
                    idNum = binding.etIdNum.text.toString(), kycImage = image,
                    issueDate = binding.etIssueDate.text.toString(),
                    verifiedStatus = binding.spinnerVerifiedStatus.selectedItem.toString(),
                    idType = binding.spinnerIdentificationType.selectedItem.toString())
        }

    private fun getKycData() {
        kycList.add(kyc)
        showKycDetail()
    }

    private fun showKycDetail() {
        binding.rcKYC.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        kycAdapter = AddKycAdapter(activity!!, kycList)
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

    private fun onAddCoApplicantClick() {
        if (checkMandatoryField()) {
            applicantTab.add("Co- Applicant $coApplicant")
            binding.rcApplicants.adapter!!.notifyDataSetChanged()
            ClearPersonalForm(binding)
            coApplicant++
        }
    }

    override fun onApplicantClick(position: Int) {
        getApplicantData()
        saveCurrentApplicant(position)
        clearFrom()
        getParticularApplicantData(position)
    }

    private fun getApplicantData(): PersonalApplicantsModel {
        applicant.addressDetailList = personalAddressDetail!!
        applicant.dateOfBirth = binding.basicInfoLayout.etAge.text.toString()
        applicant.spouseMiddleName = binding.basicInfoLayout.etSpouseMiddleName.text.toString()
        applicant.spouseLastName = binding.basicInfoLayout.etSpouseLastName.text.toString()
        applicant.spouseFirstName = binding.basicInfoLayout.etSpouseFirstName.text.toString()
        applicant.firstName = binding.basicInfoLayout.etFirstName.text.toString()
        applicant.lastName = binding.basicInfoLayout.etLastName.text.toString()
        applicant.middleName = binding.basicInfoLayout.etMiddleName.text.toString()
        applicant.fatherFirstName = binding.basicInfoLayout.etFatherFirstName.text.toString()
        applicant.fatherMiddleName = binding.basicInfoLayout.etFatherMiddleName.text.toString()
        applicant.fatherLastName = binding.basicInfoLayout.etFatherLastName.text.toString()
        val casteType = binding.basicInfoLayout.spinnerCaste.selectedItem as Modals.DropDownMaster
        applicant.casteTypeDetailID = casteType.typeDetailID
        val detailQualification = binding.basicInfoLayout.spinnerDetailQualification.selectedItem as DropdownMaster
        applicant.detailQualificationTypeDetailID = detailQualification.typeDetailID!!
        applicant.contactDetail
        applicant.age = binding.basicInfoLayout.etAge.text.toString().toInt()
        applicant.contactDetail = ContactDetail()
        return applicant
    }

    private fun clearFrom() {

    }

    private fun checkMandatoryField(): Boolean {
        return formValidation.validatePersonalInfo(binding)
    }

    private fun getParticularApplicantData(position: Int) {

    }

    private fun saveCurrentApplicant(position: Int) {
        applicantsList!![position] = applicant
        sharedPreferences.savePersonalInfoForApplicants(allApplicants)
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
        binding.personalAddressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerCurrentDistrict.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerCurrentResidenceType.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerCurrentState.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerPermanentDistrict.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerPermanentState.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.personalAddressLayout.spinnerPermanentResidenceType.adapter = MasterSpinnerAdapter(mContext, lists)

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
