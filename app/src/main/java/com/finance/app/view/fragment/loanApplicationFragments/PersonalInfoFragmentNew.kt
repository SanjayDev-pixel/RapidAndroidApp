package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalInfoNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadMaster
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
    private var pagerAdapter: PersonalPagerAdapter? = null

    companion object {
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
        leadMaster?.let {
            setClickListeners()
            setCoApplicantInTabs(leadMaster!!.personalData.applicantDetails!!)
            handleAddCoApplicant(leadMaster!!)
        }
    }

    private fun setClickListeners() {
        binding.btnPrevious.setOnClickListener {
            AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnNext.setOnClickListener {
            checkValidationAndProceed()
        }
    }

    private fun checkValidationAndProceed() {
        val mFragment: PersonalFormFragmentNew = pagerAdapter?.getItem(binding.viewPager.currentItem) as PersonalFormFragmentNew
        //Todo()
        if (mFragment.isValidFragment()) {
            AppEvents.fireEventLoanAppChangeNavFragmentNext()
        }
    }

    private fun handleAddCoApplicant(leadMaster: AllLeadMaster?) {
        binding.btnAddApplicant.visibility = View.VISIBLE
        binding.btnAddApplicant.setOnClickListener {
            LeadAndLoanDetail().addApplicants(leadMaster!!)
        }
    }

    private fun setCoApplicantInTabs(coApplicantsList: ArrayList<PersonalApplicantsModel>) {
        pagerAdapter = PersonalPagerAdapter(fragmentManager!!)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLead.setupWithViewPager(binding.viewPager)
        for (index in 0 until coApplicantsList.size) {
            pagerAdapter!!.addFragment(PersonalFormFragmentNew.newInstance(coApplicantsList[index], index), "CoApplicant ${index + 1}")
        }
    }
}