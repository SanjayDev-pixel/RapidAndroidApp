package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalInfoNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadDetail
import com.finance.app.view.adapters.recycler.adapter.PersonalPagerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import java.util.*
import javax.inject.Inject

class PersonalInfoFragmentNew : BaseFragment(), Observer {

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    private lateinit var binding: FragmentPersonalInfoNewBinding
    private var pagerAdapter: PersonalPagerAdapter? = null
    private var leadData: LeadMetaData? = null

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
        leadDetail?.let {
            setClickListeners()
            setCoApplicantInTabs(leadDetail!!.personalData.applicantDetails)
        }
    }

    private fun setClickListeners() {
        binding.btnPrevious.setOnClickListener {
            AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnNext.setOnClickListener {
            checkValidationAndProceed()
        }
        handleAddCoApplicant()
    }

    private fun handleAddCoApplicant() {
        binding.btnAddApplicant.visibility = View.VISIBLE
        binding.btnAddApplicant.setOnClickListener {
            LeadAndLoanDetail().addApplicants(leadDetail!!)
            binding.viewPager.adapter?.notifyDataSetChanged()
        }
    }

    private fun checkValidationAndProceed() {

        // This code need to be changed
        for (index in 0 until leadDetail!!.personalData.applicantDetails.size) {
            val page: Fragment? = childFragmentManager.findFragmentByTag("android:switcher:" + binding.viewPager.toString() + ":" + index)
            page?.let {
                val tab = page as PersonalFormFragmentNew
                if (tab.isValidFragment()) {
                    ++count
                }
            }
        }
        if (count == leadDetail!!.personalData.applicantDetails.size) {
            AppEvents.fireEventLoanAppChangeNavFragmentNext()
        }
    }

    private fun setCoApplicantInTabs(coApplicantsList: ArrayList<PersonalApplicantsModel>) {
        pagerAdapter = PersonalPagerAdapter(fragmentManager!!)
        for (index in 0 until coApplicantsList.size) {
            pagerAdapter!!.addFragment(PersonalFormFragmentNew.newInstance(coApplicantsList[index], index), "CoApplicant ${index + 1}")
        }
        binding.viewPager.adapter = pagerAdapter
        binding.tabLead.setupWithViewPager(binding.viewPager)

    }

    override fun update(o: Observable?, arg: Any?) {
        if (o == leadData) {
            leadData = o as LeadMetaData?
//            setCoApplicantInTabs(leadData?.getLead()?.personalData?.applicantDetails!!)
            //Todo task onUpdatedLead
        }
    }
}