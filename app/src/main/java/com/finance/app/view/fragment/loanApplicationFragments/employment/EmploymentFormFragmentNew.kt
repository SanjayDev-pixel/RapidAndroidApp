package com.finance.app.view.fragment.loanApplicationFragments.employment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentFormBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.persistence.model.EmploymentApplicantsModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.DateDifference
import com.finance.app.utility.LeadMetaData
import com.finance.app.utility.SelectDate
import com.finance.app.utility.SetEmploymentMandatoryField
import com.finance.app.view.adapters.recycler.spinner.MasterSpinnerAdapter
import com.finance.app.view.utils.setSelectionFromList
import kotlinx.android.synthetic.main.fragment_employment_form.*
import kotlinx.android.synthetic.main.layout_salary.view.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants
import motobeans.architecture.constants.Constants.APP.KEY_APPLICANT
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

class EmploymentFormFragmentNew : BaseFragment() {
    enum class FORM_TYPE {
        SALARY_DETAIL, BUSINESS_DETAIL, NONE
    }


    private lateinit var mContext: Context

    @Inject
    lateinit var dataBase: DataBaseUtil

    private var allMasterDropDown: AllMasterDropDown? = null
    private var selectedApplicant: PersonalApplicantsModel? = null
    //    private var selectedEmploymentDetails: EmploymentApplicantsModel? = null
    private var selectedFormType: FORM_TYPE? = null

    private lateinit var binding: FragmentEmploymentFormBinding

    companion object {

        fun newInstance(selectedApplicant: PersonalApplicantsModel): EmploymentFormFragmentNew {
            val fragment = EmploymentFormFragmentNew()
            val args = Bundle()
            args.putSerializable(KEY_APPLICANT, selectedApplicant)
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_employment_form)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        //Now fetch arguments...
        fetchSelectedApplicant()
        fetchSpinnersDataFromDB()
        selectedApplicant?.let { fetchApplicantEmploymentDetails(it) }
    }

    private fun initViews() {
        //Set Mandatory fields...
        SetEmploymentMandatoryField(binding)
        bindDatePickerToViews()
    }

    private fun bindDatePickerToViews() {
        binding.lytSalaryDetail.etJoiningDate.setOnClickListener { SelectDate(binding.lytSalaryDetail.etJoiningDate, mContext) }
        binding.lytBusinessDetail.etIncorporationDate.setOnClickListener { DateDifference(mContext, binding.lytBusinessDetail.etIncorporationDate, binding.lytBusinessDetail.etBusinessVintage) }
    }

    private fun fetchSelectedApplicant() {
        arguments?.getSerializable(KEY_APPLICANT)?.let { applicantDetails ->
            selectedApplicant = applicantDetails as PersonalApplicantsModel
        }
    }

    private fun fetchApplicantEmploymentDetails(applicant: PersonalApplicantsModel) {
        LeadMetaData.getLeadObservable().observe(this, Observer {
            it?.let { leadDetails ->
                val employmentList = leadDetails.employmentData.applicantDetails.filter { employmentDetail -> employmentDetail.leadApplicantNumber.equals(applicant.leadApplicantNumber, true) }
                if (employmentList.isNotEmpty()) fillEmploymentDetails(employmentList[0])
//                selectedEmploymentDetails?.let { employmentDetails -> fillEmploymentDetails(employmentDetails) }
            }
        })
    }

    private fun fetchSpinnersDataFromDB() {
        dataBase.provideDataBaseSource().allMasterDropDownDao().getMasterDropdownValue().observe(viewLifecycleOwner, Observer { masterDrownDownValues ->
            masterDrownDownValues?.let {
                allMasterDropDown = it
                //Assigning all spinners from here.. for sake of simplicity and symmetric....
                bindProfileSelectionSpinnerData()
                bindBusinessSpinnersData()
                bindSalarySpinnersData()
                bindCustomAddressSpinnersData()
            }
        })
    }

    private fun bindProfileSelectionSpinnerData() {
        binding.spinnerProfile.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.ProfileSegment)
        binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(mContext, ArrayList())

        binding.spinnerProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val profileSelected = parent?.selectedItem as DropdownMaster?
                profileSelected?.let { profile ->
                    val subProfileSegment: ArrayList<DropdownMaster> = ArrayList()
                    allMasterDropDown?.SubProfileSegment?.forEach { subProfile -> if (subProfile.refTypeDetailID == profile.typeDetailID) subProfileSegment.add(subProfile) }
                    binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(mContext, subProfileSegment)
                } ?: run { binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(mContext, ArrayList()) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spinnerSubProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val subProfileSelected = parent?.selectedItem as DropdownMaster?
                subProfileSelected?.let { subProfile ->

                    if (subProfile.typeDetailID == Constants.CASH_SALARY || subProfile.typeDetailID == Constants.BANK_SALARY) {
                        selectedFormType = FORM_TYPE.SALARY_DETAIL
                        switchEmploymentTypeView(FORM_TYPE.SALARY_DETAIL)
                        shouldShowPensionerCheckView(subProfile.typeDetailID == Constants.BANK_SALARY)

                    } else if (subProfile.typeDetailID == Constants.ITR || subProfile.typeDetailID == Constants.ASSESED_INCOME) {
                        selectedFormType = FORM_TYPE.BUSINESS_DETAIL
                        switchEmploymentTypeView(FORM_TYPE.BUSINESS_DETAIL)
                        shouldShowPensionerCheckView(false)

                    } else {
                        selectedFormType = FORM_TYPE.NONE
                        switchEmploymentTypeView(FORM_TYPE.NONE)
                    }

                } ?: run {
                    selectedFormType = FORM_TYPE.NONE
                    switchEmploymentTypeView(FORM_TYPE.NONE)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun bindBusinessSpinnersData() {
        binding.lytBusinessDetail.spinnerConstitution.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.Constitution)
        binding.lytBusinessDetail.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.Industry)
        binding.lytBusinessDetail.spinnerBusinessSetupType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.BusinessSetupType)
    }

    private fun bindSalarySpinnersData() {
        binding.lytSalaryDetail.spinnerSector.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.Sector)
        binding.lytSalaryDetail.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.Industry)
        binding.lytSalaryDetail.spinnerEmploymentType.adapter = MasterSpinnerAdapter(mContext, allMasterDropDown?.EmploymentType)
    }

    private fun bindCustomAddressSpinnersData() {
        activity?.let {
            binding.lytSalaryDetail.layoutAddress.customZipAddressView.attachActivity(it)
            binding.lytBusinessDetail.layoutAddress.customZipAddressView.attachActivity(it)
        }
    }

    private fun switchEmploymentTypeView(formType: FORM_TYPE) {
        when (formType) {
            FORM_TYPE.SALARY_DETAIL -> {
                lytSalaryDetail.visibility = View.VISIBLE
                lytBusinessDetail.visibility = View.GONE
            }
            FORM_TYPE.BUSINESS_DETAIL -> {
                lytSalaryDetail.visibility = View.GONE
                lytBusinessDetail.visibility = View.VISIBLE
            }
            FORM_TYPE.NONE -> {
                lytSalaryDetail.visibility = View.GONE
                lytBusinessDetail.visibility = View.GONE
            }
        }
    }

    private fun shouldShowPensionerCheckView(visible: Boolean) {
        lytSalaryDetail.cbIsPensioner.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun fillEmploymentDetails(employmentDetails: EmploymentApplicantsModel) {
        employmentDetails.profileSegmentTypeDetailID?.let { allMasterDropDown?.ProfileSegment?.let { list -> binding.spinnerProfile.setSelectionFromList(list, it) } }

        when (selectedFormType) {
            FORM_TYPE.SALARY_DETAIL -> fillSalaryDetails(employmentDetails)
            FORM_TYPE.BUSINESS_DETAIL -> fillBusinessDetails(employmentDetails)
        }
    }

    private fun fillSalaryDetails(employmentDetails: EmploymentApplicantsModel) {
        binding.lytSalaryDetail.cbIsPensioner.isChecked = employmentDetails.isPensioner
        binding.lytSalaryDetail.cbIsPensioner.isChecked = employmentDetails.isPensioner

        /*applicant.dateOfJoining?.let {
            binding.etJoiningDate.setText(applicant.dateOfJoining)
        }
        applicant.totalExperience?.let {
            binding.etTotalExperience.setText(applicant.totalExperience)
        }
        applicant.retirementAge?.let {
            binding.etRetirementAge.setText(applicant.retirementAge.toString())
        }
        applicant.officialMailID?.let {
            binding.etOfficialMailId.setText(applicant.officialMailID)
        }
        applicant.designation?.let {
            binding.etDesignation.setText(applicant.designation)
        }
        applicant.employeeID?.let {
            binding.etEmployeeId.setText(applicant.employeeID)
        }
        applicant.companyName?.let {
            binding.etCompanyName.setText(applicant.companyName)
        }
        applicant.sectorTypeDetailID?.let {
            sector.setSelection(applicant.sectorTypeDetailID.toString())
        }
        applicant.industryTypeDetailID?.let {
            salaryIndustry.setSelection(applicant.industryTypeDetailID.toString())
        }
        applicant.employmentTypeDetailID?.let {
            employmentType.setSelection(applicant.employmentTypeDetailID.toString())
        }*/

    }

    private fun fillBusinessDetails(employmentDetails: EmploymentApplicantsModel) {

    }

    fun isEmploymentDetailsValid(): Boolean {
        return false
    }

    fun getApplicantEmploymentDetails(): EmploymentApplicantsModel {
        //Assign New Model if null...
        val employmentDetails = EmploymentApplicantsModel()
        selectedApplicant?.let { applicant ->

            //Basic Details...
            employmentDetails.leadApplicantNumber = applicant.leadApplicantNumber
            employmentDetails.isMainApplicant = applicant.isMainApplicant
            employmentDetails.incomeConsidered = applicant.incomeConsidered

            //Employment Details....
            employmentDetails.profileSegmentTypeDetailID = (binding.spinnerProfile.selectedItem as DropdownMaster?)?.typeDetailID
            employmentDetails.subProfileTypeDetailID = (binding.spinnerProfile.selectedItem as DropdownMaster?)?.typeDetailID

//            when (selectedFormType) {
//                FORM_TYPE.SALARY_DETAIL -> getSalaryDetails(employmentDetails)
//                FORM_TYPE.BUSINESS_DETAIL -> getBusinessDetails(employmentDetails)
//                FORM_TYPE.NONE ->{}
//            }


        }





        return employmentDetails
    }

    private fun getBusinessDetails(employmentDetails: EmploymentApplicantsModel):EmploymentApplicantsModel {
            return EmploymentApplicantsModel()
    }

    private fun getSalaryDetails(employmentDetails: EmploymentApplicantsModel):EmploymentApplicantsModel {
        return EmploymentApplicantsModel()
    }

}
