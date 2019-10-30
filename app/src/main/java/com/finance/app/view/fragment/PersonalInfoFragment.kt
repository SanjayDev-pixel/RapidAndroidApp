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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalBinding
import com.finance.app.model.Modals.AddKyc
import com.finance.app.model.Modals.ApplicantPersonal
import com.finance.app.persistence.model.*
import com.finance.app.presenter.connector.LoanApplicationConnector
import com.finance.app.utility.AgeFromDOB
import com.finance.app.utility.ClearPersonalForm
import com.finance.app.utility.SelectDate
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.recycler.adapter.AddKycAdapter
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.YesNoSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class PersonalInfoFragment : BaseFragment(), LoanApplicationConnector.PersonalInfo, PersonalApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentPersonalBinding
    private val frag = this
    private lateinit var mContext: Context
    private var kycAdapter: AddKycAdapter? = null
    private var personalApplicantAdapter: PersonalApplicantsAdapter? = null
    private var personalAddressDetail: ArrayList<AddressDetail>? = null
    private lateinit var allMasterDropDown: AllMasterDropDown
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var dataBase: DataBaseUtil

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
        binding = initBinding(inflater, container, R.layout.fragment_personal)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        kycList = ArrayList()
        mContext = context!!
        applicantTab = ArrayList()
        getDropDownsFromDB()
        checkKycDataList()
        setCoApplicants()
        setDatePicker()
        setClickListeners()
        checkPropertySelection()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues.let {
                allMasterDropDown = it
                setMasterDropDownValue(allMasterDropDown)
            }
        })
    }

    private fun setMasterDropDownValue(dropDown: AllMasterDropDown) {
        binding.spinnerIdentificationType.adapter = MasterSpinnerAdapter(mContext, dropDown.IdentificationType!!)
        binding.spinnerVerifiedStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.VerifiedStatus!!)
        binding.basicInfoLayout.spinnerGender.adapter = MasterSpinnerAdapter(mContext, dropDown.Gender!!)
        binding.basicInfoLayout.spinnerNationality.adapter = MasterSpinnerAdapter(mContext, dropDown.Nationality!!)
        binding.basicInfoLayout.spinnerReligion.adapter = MasterSpinnerAdapter(mContext, dropDown.Religion!!)
        binding.basicInfoLayout.spinnerCaste.adapter = MasterSpinnerAdapter(mContext, dropDown.Caste!!)
        binding.basicInfoLayout.spinnerQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.Qualification!!)
        binding.basicInfoLayout.spinnerDetailQualification.adapter = MasterSpinnerAdapter(mContext, dropDown.DetailQualification!!)
        binding.basicInfoLayout.spinnerMaritalStatus.adapter = MasterSpinnerAdapter(mContext, dropDown.MaritalStatus!!)
        binding.basicInfoLayout.spinnerRelationship.adapter = MasterSpinnerAdapter(mContext, dropDown.Relationship!!)
        binding.personalAddressLayout.spinnerCurrentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof!!)
        binding.personalAddressLayout.spinnerPermanentAddressProof.adapter = MasterSpinnerAdapter(mContext, dropDown.AddressProof!!)
        binding.personalAddressLayout.spinnerCurrentResidenceType.adapter = MasterSpinnerAdapter(mContext, dropDown.ResidenceType!!)
        binding.personalAddressLayout.spinnerPermanentResidenceType.adapter = MasterSpinnerAdapter(mContext, dropDown.ResidenceType!!)
        binding.basicInfoLayout.spinnerLivingStandard.adapter = YesNoSpinnerAdapter(mContext)
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
        ClearPersonalForm(binding)
//        getApplicantData(position)
        getParticularApplicantData(position)
    }

    private fun saveCurrentApplicant() {
        val position = 1
        applicantsList!![position] = applicant
        sharedPreferences.savePersonalInfoForApplicants(allApplicants)
    }

    private fun onAddCoApplicantClick() {
        if (formValidation.validatePersonalInfo(binding)) {
            applicantTab.add("Co- Applicant $coApplicant")
            binding.rcApplicants.adapter!!.notifyDataSetChanged()
            ClearPersonalForm(binding)
            coApplicant++
        }
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
        personalApplicantAdapter = PersonalApplicantsAdapter(context!!, applicantTab)
        personalApplicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = personalApplicantAdapter
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
            if (formValidation.validatePersonalInfo(binding)) {
                setIncomeConsidered()
                sharedPreferences.savePersonalInfoForApplicants(allApplicants)
                gotoNextFragment()
            }
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
            AgeFromDOB(mContext, binding.basicInfoLayout.etDOB, binding.basicInfoLayout.etAge)
        }
        binding.etIssueDate.setOnClickListener {
            SelectDate(binding.etIssueDate, mContext)
        }
        binding.etExpiryDate.setOnClickListener {
            SelectDate(binding.etExpiryDate, mContext)
        }
    }

    private fun getParticularApplicantData(position: Int) {
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
        val casteType = binding.basicInfoLayout.spinnerCaste.selectedItem as DropdownMaster
        applicant.casteTypeDetailID = casteType.typeDetailID!!
        val detailQualification = binding.basicInfoLayout.spinnerDetailQualification.selectedItem as DropdownMaster
        applicant.detailQualificationTypeDetailID = detailQualification.typeDetailID!!
        applicant.contactDetail
        applicant.age = binding.basicInfoLayout.etAge.text.toString().toInt()
        applicant.contactDetail = ContactDetail()
    }

    override val personalInfoRequest: Requests.RequestPersonalInfo
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getPersonalInfoSuccess(value: Response.ResponseLoanApplication) {
        gotoNextFragment()
    }

    override fun getPersonalInfoFailure(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun gotoNextFragment() {
        val ft = fragmentManager?.beginTransaction()
        ft?.replace(R.id.secondaryFragmentContainer, AssetLiabilityFragment())
        ft?.addToBackStack(null)
        ft?.commit()
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
            binding.rcKYC.adapter!!.notifyDataSetChanged()
        }
    }
}
