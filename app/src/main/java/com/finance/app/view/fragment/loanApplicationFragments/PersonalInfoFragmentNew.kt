package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalInfoNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.PersonalPagerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class PersonalInfoFragmentNew : BaseFragment() {

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    private lateinit var binding: FragmentPersonalInfoNewBinding
    private var pagerAdapterApplicants: PersonalPagerAdapter? = null

    private val alCoApplicants = ArrayList<PersonalApplicantsModel>()

    companion object {
        private var count = 0
        fun newInstance(): PersonalInfoFragmentNew {
            return PersonalInfoFragmentNew()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_personal_info_new)
        binding.lifecycleOwner = this
        init()
        return view
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        setApplicantAdapter()

        LeadMetaData.getLeadObservable().observe(this, androidx.lifecycle.Observer { leadDetail ->
            leadDetail?.let {
                val applicantsList = leadDetail.personalData.applicantDetails
                setClickListeners(applicantsList)
                refreshApplicantData(applicantsList)
            }
        })
    }

    private fun setApplicantAdapter() {
        pagerAdapterApplicants = PersonalPagerAdapter(fragmentManager!!, alCoApplicants)
        binding.viewPager.adapter = pagerAdapterApplicants
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }

    private fun refreshApplicantData(applicantDetails: ArrayList<PersonalApplicantsModel>) {
        alCoApplicants.clear()
        alCoApplicants.addAll(applicantDetails)
        pagerAdapterApplicants?.notifyDataSetChanged()
    }

    private fun setClickListeners(applicantsList: ArrayList<PersonalApplicantsModel>) {
        binding.btnPrevious.setOnClickListener {
            AppEvents.fireEventLoanAppChangeNavFragmentPrevious()
        }
        binding.btnNext.setOnClickListener {
            checkValidationAndProceed(applicantsList)
        }
        handleAddCoApplicant()
    }

    private fun handleAddCoApplicant() {
        binding.btnAddApplicant.setOnClickListener {
            LeadMetaData.addCoApplicant()
            binding.viewPager.adapter?.notifyDataSetChanged()
        }
    }

    private fun checkValidationAndProceed(applicantsList: ArrayList<PersonalApplicantsModel>) {
        val pApplicantList = ArrayList<PersonalApplicantsModel>()

        val fragments = pagerAdapterApplicants?.getAllFragments()
        fragments?.let {
            fragments.forEach { _, item ->
                if (item.isValidFragment()) {
                    val applicant = item.getApplicant()
                    pApplicantList.add(applicant)
                    ++count
                }
                if (count == applicantsList.size) {
                    LeadMetaData().savePersonalData(pApplicantList)
                    AppEvents.fireEventLoanAppChangeNavFragmentNext()
                }
            }
        }
    }
}