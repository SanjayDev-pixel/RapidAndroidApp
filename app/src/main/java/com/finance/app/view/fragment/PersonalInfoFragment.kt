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
import com.finance.app.persistence.model.AddressDetail
import com.finance.app.persistence.model.ContactDetail
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.PersonalApplicants
import com.finance.app.utility.ClearPersonalForm
import com.finance.app.utility.SelectDate
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.Recycler.Adapter.AddKycAdapter
import com.finance.app.view.adapters.Recycler.Adapter.ApplicantsAdapter
import com.finance.app.view.adapters.Recycler.Adapter.MasterSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class PersonalInfoFragment : Fragment(), ApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentPersonalBinding
    private val frag = this
    private lateinit var mContext: Context
    private var kycAdapter: AddKycAdapter? = null
    private var applicantAdapter: ApplicantsAdapter? = null
    private var applicantsList: ArrayList<PersonalApplicants>? = null
    private var personalAddressDetail: ArrayList<AddressDetail>? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private var coApplicant = 1
        private lateinit var kycList: ArrayList<Modals.AddKyc>
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
        applicantAdapter = ApplicantsAdapter(context!!, applicantTab)
        binding.rcApplicants.adapter = applicantAdapter
        applicantAdapter!!.setOnItemClickListener(this)
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
            sharedPreferences.savePersonalInfoForApplicants(applicantsList!!)
        }

        binding.addressLayout.cbSameAsCurrent.setOnClickListener {
            if (binding.addressLayout.cbSameAsCurrent.isChecked) {
                binding.addressLayout.llPermanentAddress.visibility = View.GONE
            } else {
                binding.addressLayout.llPermanentAddress.visibility = View.VISIBLE
            }
        }
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
            saveCurrentApplicant()
            applicantTab.add("Co- Applicant $coApplicant")
            binding.rcApplicants.adapter!!.notifyDataSetChanged()
            ClearPersonalForm(binding)
            coApplicant++
        }
    }

    private fun checkMandatoryField(): Boolean {
        return formValidation.validatePersonalInfo(binding)
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
        getParticularApplicantData(position)
    }

    private fun getParticularApplicantData(position: Int) {

    }

    private fun saveCurrentApplicant() {
        applicantsList!!.add(applicant)
        sharedPreferences.savePersonalInfoForApplicants(applicantsList!!)
    }

    private val applicant: PersonalApplicants
        get() {
            personalAddressDetail!!.add(AddressDetail())
            return PersonalApplicants(personalAddressDetail!!)
        }

    private val addressDetail: AddressDetail
        get() {
            return AddressDetail()
        }

    private val contactDetail: ContactDetail
        get() {
            applicantTab.add(applicantTab.size - 1, "Co-Applicant:${coApplicant}")
                applicantAdapter!!.notifyDataSetChanged()
            saveCurrentApplicant()
            applicantTab.add("Co- Applicant $coApplicant")
                coApplicant++
            return ContactDetail()
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
        binding.addressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerCurrentDistrict.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerCurrentResidenceType.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerCurrentState.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentDistrict.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentState.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.addressLayout.spinnerPermanentResidenceType.adapter = MasterSpinnerAdapter(mContext, lists)

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
