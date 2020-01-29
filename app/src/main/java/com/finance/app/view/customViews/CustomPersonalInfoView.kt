package com.finance.app.view.customViews

import android.app.Dialog
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.LayoutCustomViewPersonalBinding
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.*
import com.finance.app.presenter.presenter.Presenter
import com.finance.app.presenter.presenter.ViewGeneric
import com.finance.app.utility.*
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadDetail
import com.finance.app.view.customViews.interfaces.IspinnerMainView
import kotlinx.android.synthetic.main.pop_up_verify_otp.view.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.Constants.APP.CURRENT_ADDRESS
import motobeans.architecture.constants.Constants.APP.PERMANENT_ADDRESS
import motobeans.architecture.constants.Constants.APP.RENTED
import motobeans.architecture.constants.Constants.APP.SELF
import motobeans.architecture.constants.Constants.APP.SINGLE
import motobeans.architecture.constants.ConstantsApi
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.retrofit.request.Requests
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.AppUtilExtensions
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible
import javax.inject.Inject

class CustomPersonalInfoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val TAG = this.javaClass.canonicalName

    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation
    private lateinit var binding: LayoutCustomViewPersonalBinding
    private lateinit var activity: FragmentActivity
    private var index: Int = 0
    private var otp: Int? = 0
    private val presenter = Presenter()
    private lateinit var verifyOTPDialog: Dialog
    private lateinit var verifyOTPDialogView: View
    private lateinit var gender: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var nationality: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var dobProof: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var livingStandard: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var detailQualification: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var qualification: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var caste: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var religion: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var relationship: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var currentAddressProof: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var permanentAddressProof: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var maritalStatus: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var permanentResidenceType: CustomSpinnerViewTest<DropdownMaster>
    private lateinit var currentResidenceType: CustomSpinnerViewTest<DropdownMaster>
    private var spinnerDMList: ArrayList<CustomSpinnerViewTest<DropdownMaster>> = ArrayList()

    fun attachView(activity: FragmentActivity, index: Int, applicant: PersonalApplicantsModel, leadId: Int?) {
        this.activity = activity
        this.index = index
        binding = AppUtilExtensions.initCustomViewBinding(context = context,
                layoutId = R.layout.layout_custom_view_personal, container = this)
        initializeViews(applicant, leadId)
    }

    private fun initializeViews(applicant: PersonalApplicantsModel, leadId: Int?) {
        SetPersonalMandatoryField(binding)
        setDatePicker()
        setClickListeners(leadId, applicant)
        setUpCustomViews()
        proceedFurther(applicant)
    }

    private fun setDatePicker() {
        binding.basicInfoLayout.etDOB.setOnClickListener {
            SelectDOB(context, binding.basicInfoLayout.etDOB, binding.basicInfoLayout.etAge)
        }
        binding.etIssueDate.setOnClickListener {
            SelectDate(binding.etIssueDate, context)
        }
        binding.etExpiryDate.setOnClickListener {
            SelectDate(binding.etExpiryDate, context)
        }
    }

    private fun setClickListeners(leadId: Int?, applicant: PersonalApplicantsModel) {
        binding.basicInfoLayout.btnGetOTP.setOnClickListener { showVerifyOTPDialog(leadId, applicant) }
        binding.personalAddressLayout.cbSameAsCurrent.setOnClickListener {
            if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.GONE
            } else {
                binding.personalAddressLayout.llPermanentAddress.visibility = View.VISIBLE
            }
        }
        CurrencyConversion().convertToCurrencyType(binding.personalAddressLayout.etPermanentRentAmount)
        CurrencyConversion().convertToCurrencyType(binding.personalAddressLayout.etCurrentRentAmount)
    }

    private fun setUpCustomViews() {
        binding.personalAddressLayout.customCurrentZipAddressView.attachActivity(activity = activity)
        binding.personalAddressLayout.customPermanentZipAddressView.attachActivity(activity = activity)
    }

    private fun proceedFurther(applicant: PersonalApplicantsModel) {
        ArchitectureApp.instance.component.inject(this)
        getDropDownsFromDB(applicant)
    }

    private fun getDropDownsFromDB(applicant: PersonalApplicantsModel) {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(activity,
                Observer { allMasterDropdown ->
                    allMasterDropdown?.let {
                        setMasterDropDownValue(allMasterDropdown, applicant)
                    }
                })
    }

    private fun getRelationship(relationship: ArrayList<DropdownMaster>?): ArrayList<DropdownMaster> {
        val relationshipList: ArrayList<DropdownMaster> = ArrayList()
        relationship?.let {

            for (dropdown in relationship) {
                if (dropdown.typeDetailID != SELF) {
                    relationshipList.add(dropdown)
                }
            }
        }
        return relationshipList
    }

    private fun setMasterDropDownValue(dropDown: AllMasterDropDown, applicant: PersonalApplicantsModel) {
        setCustomSpinner(dropDown, applicant)
        fillValueInMasterDropDown(applicant)
    }

    private fun setUpRelationshipValue(allMasterDropDown: AllMasterDropDown, applicant: PersonalApplicantsModel) {
        if (applicant.isMainApplicant) {
            relationship = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.Relationship!!, label = "Relationship *")
            binding.basicInfoLayout.layoutRelationShip.addView(relationship)

            relationship.setSelection(SELF.toString())
            relationship.disableSelf()
        } else {
            relationship = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = getRelationship(allMasterDropDown.Relationship), label = "Relationship *")
            binding.basicInfoLayout.layoutRelationShip.addView(relationship)

            relationship.setSelection(applicant.relationshipTypeDetailId?.toString())
        }
    }

    private fun setCustomSpinner(allMasterDropDown: AllMasterDropDown, applicant: PersonalApplicantsModel) {
        dobProof = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.DOBProof!!, label = "DOB Proof *")
        binding.basicInfoLayout.layoutDobProof.addView(dobProof)
        livingStandard = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.LivingStandardIndicators!!, label = "Living Standard *")
        binding.basicInfoLayout.layoutLivingStandard.addView(livingStandard)
        detailQualification = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.DetailQualification!!, label = "Detail Qualification *")
        binding.basicInfoLayout.layoutDetailQualification.addView(detailQualification)
        qualification = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.Qualification!!, label = "Qualification *")
        binding.basicInfoLayout.layoutQualification.addView(qualification)
        caste = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.Caste!!, label = "Caste *")
        binding.basicInfoLayout.layoutCaste.addView(caste)
        religion = CustomSpinnerViewTest(mContext = context, dropDowns = allMasterDropDown.Religion!!, label = "Religion *")
        binding.basicInfoLayout.layoutReligion.addView(religion)
        nationality = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.Nationality!!, label = "Nationality *")
        binding.basicInfoLayout.layoutNationality.addView(nationality)
        gender = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.Gender!!, label = "Gender *")
        binding.basicInfoLayout.layoutGender.addView(gender)
        permanentAddressProof = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.AddressProof!!, label = "Address Proof *")
        binding.personalAddressLayout.layoutPermanentAddressProof.addView(permanentAddressProof)
        currentAddressProof = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.AddressProof!!, label = "Address Proof *")
        binding.personalAddressLayout.layoutCurrentAddressProof.addView(currentAddressProof)
        setUpRelationshipValue(allMasterDropDown, applicant)
        setCustomSpinnerWithCondition(allMasterDropDown)
    }

    private fun setCustomSpinnerWithCondition(allMasterDropDown: AllMasterDropDown) {
        maritalStatus = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.MaritalStatus!!, label = "Marital Status *", iSpinnerMainView = object : IspinnerMainView<DropdownMaster> {
            override fun getSelectedValue(value: DropdownMaster) {
                binding.basicInfoLayout.layoutMaritalStatus.removeAllViews()
                if (value.typeDetailID == SINGLE) {
                    binding.basicInfoLayout.llSpouse.visibility = View.GONE
                } else {
                    binding.basicInfoLayout.llSpouse.visibility = View.VISIBLE
                }
            }
        })

        binding.basicInfoLayout.layoutMaritalStatus.addView(maritalStatus)

        permanentResidenceType = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.ResidenceType!!, label = "Residence Type *", iSpinnerMainView = object : IspinnerMainView<DropdownMaster> {
            override fun getSelectedValue(value: DropdownMaster) {
                if (value.typeDetailID == RENTED) {
                    binding.personalAddressLayout.inputLayoutPermanentRentAmount.visibility = View.VISIBLE
                } else {
                    binding.personalAddressLayout.inputLayoutPermanentRentAmount.visibility = View.GONE
                }
            }
        })

        binding.personalAddressLayout.layoutPermanentResidenceType.addView(permanentResidenceType)
        currentResidenceType = CustomSpinnerViewTest(mContext = context, isMandatory = true, dropDowns = allMasterDropDown.ResidenceType!!, label = "Residence Type *", iSpinnerMainView = object : IspinnerMainView<DropdownMaster> {
            override fun getSelectedValue(value: DropdownMaster) {
                if (value.typeDetailID == RENTED) {
                    binding.personalAddressLayout.inputLayoutCurrentRentAmount.visibility = View.VISIBLE
                } else {
                    binding.personalAddressLayout.inputLayoutCurrentRentAmount.visibility = View.GONE
                }
            }
        })
        binding.personalAddressLayout.layoutCurrentResidenceType.addView(currentResidenceType)

        setDropDownList()
    }

    private fun setDropDownList() {
        spinnerDMList = arrayListOf(dobProof, livingStandard, maritalStatus, gender,
                nationality, religion, caste, qualification, detailQualification, livingStandard,
                relationship, currentResidenceType, currentAddressProof, permanentResidenceType,
                permanentAddressProof)
    }

    private fun fillValueInMasterDropDown(currentApplicant: PersonalApplicantsModel) {
        gender.setSelection(currentApplicant.genderTypeDetailID?.toString())
        nationality.setSelection(currentApplicant.nationalityTypeDetailID?.toString())
        religion.setSelection(currentApplicant.religionTypeDetailID?.toString())
        caste.setSelection(currentApplicant.casteTypeDetailID?.toString())
        dobProof.setSelection(currentApplicant.dobProofTypeDetailID?.toString())
        qualification.setSelection(currentApplicant.qualificationTypeDetailID?.toString())
        detailQualification.setSelection(currentApplicant.detailQualificationTypeDetailID?.toString())
        livingStandard.setSelection(currentApplicant.livingStandardTypeDetailId?.toString())
        relationship.setSelection(currentApplicant.relationshipTypeDetailId?.toString())
        maritalStatus.setSelection(currentApplicant.maritialStatusTypeDetailID?.toString())

        fillFormWithCurrentApplicant(currentApplicant)
        if (!currentApplicant.addressDetailList.isNullOrEmpty()) {
            fillAddressInfo(currentApplicant.addressDetailList!!)
        }
    }

    private fun fillFormWithCurrentApplicant(currentApplicant: PersonalApplicantsModel) {
        currentApplicant.contactDetail?.let {
            binding.basicInfoLayout.etEmail.setText(currentApplicant.contactDetail?.email)
            binding.basicInfoLayout.etMobile.setText(currentApplicant.contactDetail?.mobile)
            if (currentApplicant.contactDetail!!.isMobileVerified!!) {
                binding.basicInfoLayout.etMobile.isEnabled = false
                binding.basicInfoLayout.btnGetOTP.visibility = View.GONE
                binding.basicInfoLayout.ivVerifiedStatus.visibility = View.VISIBLE
            }
        }

        binding.basicInfoLayout.etDOB.setText(ConvertDate().convertToAppFormat(currentApplicant.dateOfBirth!!))
        binding.basicInfoLayout.cbIncomeConsidered.isChecked = currentApplicant.incomeConsidered!!
        binding.basicInfoLayout.etFatherLastName.setText(currentApplicant.fatherLastName)
        binding.basicInfoLayout.etFatherMiddleName.setText(currentApplicant.fatherMiddleName)
        binding.basicInfoLayout.etFatherFirstName.setText(currentApplicant.fatherFirstName)
        binding.basicInfoLayout.etFirstName.setText(currentApplicant.firstName)
        binding.basicInfoLayout.etMiddleName.setText(currentApplicant.middleName)
        binding.basicInfoLayout.etNumOfDependent.setText(currentApplicant.numberOfDependents.toString())
        binding.basicInfoLayout.etNumOfEarningMember.setText(currentApplicant.numberOfEarningMembers.toString())
        binding.basicInfoLayout.etLastName.setText(currentApplicant.lastName)
        binding.basicInfoLayout.etAge.setText(currentApplicant.age.toString())
        binding.basicInfoLayout.etAlternateNum.setText(currentApplicant.alternateContact.toString())
        if (currentApplicant.maritialStatusTypeDetailID != SINGLE) {
            binding.basicInfoLayout.etSpouseMiddleName.setText(currentApplicant.spouseMiddleName)
            binding.basicInfoLayout.etSpouseFirstName.setText(currentApplicant.spouseFirstName)
            binding.basicInfoLayout.etSpouseLastName.setText(currentApplicant.spouseLastName)
        }
        checkSubmission()
    }

    private fun checkSubmission() {
        if (leadDetail?.status == AppEnums.LEAD_TYPE.SUBMITTED.type) {
            DisablePersonalForm(binding)
        }
    }

    private fun fillAddressInfo(addressDetailList: ArrayList<AddressDetail>) {
        fillPermanentAddressInfo(addressDetailList[1])
        fillCurrentAddressInfo(addressDetailList[0])
    }

    private fun fillCurrentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etCurrentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etCurrentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etCurrentRentAmount.setText(addressDetail.rentAmount.toString())
        binding.personalAddressLayout.etCurrentStaying.setText(addressDetail.stayingInYears.toString())
        currentAddressProof.setSelection(addressDetail.addressTypeDetailID?.toString())
        currentResidenceType.setSelection(addressDetail.residenceTypeTypeDetailID.toString())
        updateCustomZipCode(customZipView = binding.personalAddressLayout.customCurrentZipAddressView, addressDetail = addressDetail)
    }

    private fun fillPermanentAddressInfo(addressDetail: AddressDetail) {
        binding.personalAddressLayout.etPermanentAddress.setText(addressDetail.address1)
        binding.personalAddressLayout.etPermanentLandmark.setText(addressDetail.landmark)
        binding.personalAddressLayout.etPermanentRentAmount.setText(addressDetail.rentAmount.toString())
        binding.personalAddressLayout.etPermanentStaying.setText(addressDetail.stayingInYears.toString())
        permanentAddressProof.setSelection(addressDetail.addressTypeDetailID?.toString())
        permanentResidenceType.setSelection(addressDetail.residenceTypeTypeDetailID?.toString())
        updateCustomZipCode(customZipView = binding.personalAddressLayout.customPermanentZipAddressView, addressDetail = addressDetail)
    }

    private fun updateCustomZipCode(customZipView: CustomZipAddressView, addressDetail: AddressDetail) {
        customZipView.updateAddressData(addressDetail = addressDetail)
    }

    private fun getCurrentApplicant(): PersonalApplicantsModel {
        val currentApplicant = PersonalApplicantsModel()
        val casteDD = caste.getSelectedValue()
        val dQualificationDD = detailQualification.getSelectedValue()
        val qDD = qualification.getSelectedValue()
        val dobProofDD = dobProof.getSelectedValue()
        val genderDD = gender.getSelectedValue()
        val nationalityDD = nationality.getSelectedValue()
        val religionDD = religion.getSelectedValue()
        val mStatusDD = maritalStatus.getSelectedValue()
        val livingStandardDD = livingStandard.getSelectedValue()
        val relationshipDD = relationship.getSelectedValue()
        val dependents = binding.basicInfoLayout.etNumOfDependent.text.toString()
        val earningMembers = binding.basicInfoLayout.etNumOfEarningMember.text.toString()

        currentApplicant.casteTypeDetailID = casteDD?.typeDetailID
        currentApplicant.detailQualificationTypeDetailID = dQualificationDD?.typeDetailID
        currentApplicant.qualificationTypeDetailID = qDD?.typeDetailID
        currentApplicant.dobProofTypeDetailID = dobProofDD?.typeDetailID
        currentApplicant.genderTypeDetailID = genderDD?.typeDetailID
        currentApplicant.nationalityTypeDetailID = nationalityDD?.typeDetailID
        currentApplicant.religionTypeDetailID = religionDD?.typeDetailID
        currentApplicant.relationshipTypeDetailId = relationshipDD?.typeDetailID
        currentApplicant.maritialStatusTypeDetailID = mStatusDD?.typeDetailID
        currentApplicant.isMainApplicant = index == 0
        currentApplicant.livingStandardTypeDetailId = livingStandardDD?.typeDetailID
        currentApplicant.numberOfEarningMembers = if (earningMembers == "") 0 else earningMembers.toInt()
        currentApplicant.numberOfDependents = if (dependents == "") 0 else dependents.toInt()
        currentApplicant.firstName = binding.basicInfoLayout.etFirstName.text.toString()
        currentApplicant.middleName = binding.basicInfoLayout.etMiddleName.text.toString()
        currentApplicant.lastName = binding.basicInfoLayout.etLastName.text.toString()
        currentApplicant.spouseFirstName = binding.basicInfoLayout.etSpouseFirstName.text.toString()
        currentApplicant.spouseMiddleName = binding.basicInfoLayout.etSpouseMiddleName.text.toString()
        currentApplicant.spouseLastName = binding.basicInfoLayout.etSpouseLastName.text.toString()
        currentApplicant.fatherFirstName = binding.basicInfoLayout.etFatherFirstName.text.toString()
        currentApplicant.fatherMiddleName = binding.basicInfoLayout.etFatherMiddleName.text.toString()
        currentApplicant.fatherLastName = binding.basicInfoLayout.etFatherLastName.text.toString()
        currentApplicant.dateOfBirth = ConvertDate().convertToApiFormat(binding.basicInfoLayout.etDOB.text.toString())
        currentApplicant.age = binding.basicInfoLayout.etAge.text.toString().toInt()
        currentApplicant.incomeConsidered = binding.basicInfoLayout.cbIncomeConsidered.isChecked
        currentApplicant.alternateContact = binding.basicInfoLayout.etAlternateNum.text.toString()
        currentApplicant.contactDetail = getContactDetail()
        currentApplicant.addressDetailList = getAddressDetailList(currentApplicant.addressDetailList)
        return currentApplicant
    }

    private fun getContactDetail(): ContactDetail? {
        val contactDetail = ContactDetail()
        contactDetail.email = binding.basicInfoLayout.etEmail.text.toString()
        contactDetail.mobile = binding.basicInfoLayout.etMobile.text.toString()
        return contactDetail
    }

    private fun getAddressDetailList(addressDetailList: ArrayList<AddressDetail>?): ArrayList<AddressDetail>? {
        val cAddressDetail = AddressDetail()
        val cResidenceType = currentAddressProof.getSelectedValue()
        val cAddressProof = currentAddressProof.getSelectedValue()

        cAddressDetail.rentAmount = CurrencyConversion().convertToNormalValue(binding.personalAddressLayout.etCurrentRentAmount.text.toString())
        cAddressDetail.stayingInYears = binding.personalAddressLayout.etCurrentStaying.text.toString().toFloat()
        cAddressDetail.address1 = binding.personalAddressLayout.etCurrentAddress.text.toString()
        cAddressDetail.landmark = binding.personalAddressLayout.etCurrentLandmark.text.toString()
        cAddressDetail.zip = binding.personalAddressLayout.customCurrentZipAddressView.pinCode
        cAddressDetail.addressTypeDetail = CURRENT_ADDRESS
        cAddressDetail.stateID = binding.personalAddressLayout.customCurrentZipAddressView.getStateId()
        cAddressDetail.districtID = binding.personalAddressLayout.customCurrentZipAddressView.getDistrictId()
        cAddressDetail.cityID = binding.personalAddressLayout.customCurrentZipAddressView.getCityId()
        cAddressDetail.residenceTypeTypeDetailID = cResidenceType?.typeDetailID
        cAddressDetail.addressTypeDetailID = cAddressProof?.typeDetailID

        var pAddressDetail = AddressDetail()
        if (binding.personalAddressLayout.cbSameAsCurrent.isChecked) {
            pAddressDetail = cAddressDetail
        } else {
            val pResidenceType = permanentResidenceType.getSelectedValue()
            val pAddressProof = permanentAddressProof.getSelectedValue()

            pAddressDetail.rentAmount = CurrencyConversion().convertToNormalValue(binding.personalAddressLayout.etPermanentRentAmount.text.toString())
            pAddressDetail.stayingInYears = binding.personalAddressLayout.etPermanentStaying.text.toString().toFloat()
            pAddressDetail.address1 = binding.personalAddressLayout.etPermanentAddress.text.toString()
            pAddressDetail.landmark = binding.personalAddressLayout.etPermanentLandmark.text.toString()
            pAddressDetail.zip = binding.personalAddressLayout.customPermanentZipAddressView.pinCode
            pAddressDetail.residenceTypeTypeDetailID = pResidenceType?.typeDetailID
            pAddressDetail.addressTypeDetailID = pAddressProof?.typeDetailID
            pAddressDetail.stateID = binding.personalAddressLayout.customPermanentZipAddressView.getStateId()
            pAddressDetail.districtID = binding.personalAddressLayout.customPermanentZipAddressView.getDistrictId()
            pAddressDetail.cityID = binding.personalAddressLayout.customPermanentZipAddressView.getCityId()
        }
        pAddressDetail.addressTypeDetail = PERMANENT_ADDRESS
        if (addressDetailList.isNullOrEmpty()) {
            addressDetailList?.add(0, cAddressDetail)
            addressDetailList?.add(1, pAddressDetail)
        } else {
            addressDetailList[0] = cAddressDetail
            addressDetailList[1] = pAddressDetail
        }
        return addressDetailList
    }

    private fun showVerifyOTPDialog(leadId: Int?, applicant: PersonalApplicantsModel) {
        verifyOTPDialogView = LayoutInflater.from(context).inflate(R.layout.pop_up_verify_otp, null)
        val mBuilder = AlertDialog.Builder(context)
                .setView(verifyOTPDialogView)
                .setCancelable(false)

        verifyOTPDialog = mBuilder.show()
        val pinEntry = verifyOTPDialogView.etOTP
        pinEntry!!.setOnPinEnteredListener { pin ->
            if (pin.toString().length == 4) {
                otp = pin.toString().toInt()
                presenter.callNetwork(ConstantsApi.CALL_VERIFY_OTP, CallVerifyOTP(leadId, applicant))
            } else {
                pinEntry.text = null
            }
        }

        verifyOTPDialogView.tvResendOTP?.setOnClickListener {
            handleResendOtpEvent(verifyOTPDialogView, leadDetail, applicant)
        }
        verifyOTPDialogView.ivCross?.setOnClickListener { dismissOtpVerificationDialog() }
        verifyOTPDialogView.tvResendOTP?.callOnClick()
        timerOtpResend.start()
    }

    private fun handleResendOtpEvent(verifyOTPDialogView: View, leadMaster: AllLeadMaster?, applicant: PersonalApplicantsModel) {
        verifyOTPDialogView.tvResendOTP?.exGone()
        verifyOTPDialogView.tvResendOTPTimeLeftInfo?.exVisible()
        timerOtpResend.start()
        leadMaster?.let {
            presenter.callNetwork(ConstantsApi.CALL_SEND_OTP, CallSendOTP(leadMaster, applicant))
        }
    }

    private fun handleOtbResendTimerEndEvent() {
        verifyOTPDialogView.tvResendOTP?.exVisible()
        verifyOTPDialogView.tvResendOTPTimeLeftInfo?.exGone()
        timerOtpResend.cancel()
    }

    private fun dismissOtpVerificationDialog() {
        timerOtpResend.cancel()
        verifyOTPDialog.dismiss()
    }

    private val minutes = 1L
    private val seconds = 60L
    private val millisecond = 1000L

    private val timerOtpResend = object : CountDownTimer(minutes * seconds * millisecond, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsUntilFinish = (millisUntilFinished / millisecond).toInt()
            verifyOTPDialogView.tvResendOTPTimeLeftInfo?.text = "$secondsUntilFinish ${context.getString(R.string.seconds)}"
        }

        override fun onFinish() {
            handleOtbResendTimerEndEvent()
        }
    }

    inner class CallSendOTP(private val leadMaster: AllLeadMaster, val applicant: PersonalApplicantsModel) : ViewGeneric<Requests.RequestSendOTP, Response.ResponseOTP>(context = activity) {
        override val apiRequest: Requests.RequestSendOTP
            get() = otpSendRequest

        private val otpSendRequest: Requests.RequestSendOTP
            get() {
                val leadId = leadMaster.leadID!!.toInt()
                val mobile = binding.basicInfoLayout.etMobile.text.toString()
                return Requests.RequestSendOTP(leadID = leadId, mobile = mobile)
            }

        override fun getApiSuccess(value: Response.ResponseOTP) {
            value.responseMsg?.let {
                showToast(value.responseMsg)
            }
        }
    }

    inner class CallVerifyOTP(private val leadId: Int?, val applicant: PersonalApplicantsModel) : ViewGeneric<Requests.RequestVerifyOTP, Response.ResponseOTP>(context = activity) {
        override val apiRequest: Requests.RequestVerifyOTP
            get() = otpVerifyRequest

        private val otpVerifyRequest: Requests.RequestVerifyOTP
            get() {
                val leadId = leadId
                val mobile = binding.basicInfoLayout.etMobile.text.toString()
                return Requests.RequestVerifyOTP(leadID = leadId, mobile = mobile, otpValue = otp!!)
            }

        override fun getApiSuccess(value: Response.ResponseOTP) {
            if (value.responseCode == Constants.SUCCESS) {
                dismissOtpVerificationDialog()
                applicant.contactDetail!!.isMobileVerified = true
                binding.basicInfoLayout.etMobile.isEnabled = false
                binding.basicInfoLayout.btnGetOTP.visibility = View.GONE
                binding.basicInfoLayout.ivVerifiedStatus.visibility = View.VISIBLE
            }
        }
    }

    fun isValidPersonalApplicant(): PersonalApplicantsModel? {
        return if (formValidation.validatePersonalInfo(binding, spinnerDMList)) getCurrentApplicant()
        else null
    }
}
