package com.finance.app.view.fragment.loanApplicationFragments.personal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalInfoNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.AppEnums
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.pager.PersonalPagerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.FormValidation
import javax.inject.Inject

class PersonalInfoFragmentNew : BaseFragment() {

    @Inject
    lateinit var formValidation: FormValidation

    private var pagerAdapterApplicants: PersonalPagerAdapter? = null

    private lateinit var binding: FragmentPersonalInfoNewBinding


    companion object {
        fun newInstance(): PersonalInfoFragmentNew {
            return PersonalInfoFragmentNew()
        }
    }

    override fun init() {
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_personal_info_new)
        binding.lifecycleOwner = this

        setOnClickListeners()
        LeadMetaData.getLeadData()?.let {
            if (it.status.equals(AppEnums.LEAD_TYPE.SUBMITTED.type, true)) {
                binding.btnAddApplicantTab.visibility = View.GONE
            } else {
                binding.btnAddApplicantTab.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch applicants from database.....
        fetchLeadApplicantDetails()
    }

    private fun fetchLeadApplicantDetails() {
        LeadMetaData.getLeadObservable().observe(this, Observer { leadDetail ->
            leadDetail?.let {
                //Now Set Applicant Tabs....
                setApplicantTabLayout(ArrayList(it.personalData.applicantDetails))
            }
        })
    }

    private fun setApplicantTabLayout(applicantList: ArrayList<PersonalApplicantsModel>) {
        pagerAdapterApplicants = PersonalPagerAdapter(fragmentManager!!, applicantList)
        binding.viewPager.offscreenPageLimit = 5 //Must be called before setting adapter
        binding.viewPager.adapter = pagerAdapterApplicants
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }


    private fun setOnClickListeners() {

        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnNext.setOnClickListener { addApplicant() }
        binding.btnAddApplicantTab.setOnClickListener {
            pagerAdapterApplicants?.addItem()
            if (binding.tabLead.tabCount > 2)//Scroll tab to last item....
                binding.tabLead.getTabAt(binding.tabLead.tabCount - 1)?.select()
        }
    }

    private fun addApplicant() {
        pagerAdapterApplicants?.let { adapter ->
            if (adapter.isApplicantDetailsValid()) {
                LeadMetaData().savePersonalData(adapter.getApplicantDetails())
                AppEvents.fireEventLoanAppChangeNavFragmentNext()
            }
        }
    }

}