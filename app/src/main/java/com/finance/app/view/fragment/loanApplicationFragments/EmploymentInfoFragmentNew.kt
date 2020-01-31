package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.EmploymentApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.EmploymentPagerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class EmploymentInfoFragmentNew : BaseFragment(){

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation
    private lateinit var binding: FragmentEmploymentNewBinding
    private var pagerAdapterApplicants: EmploymentPagerAdapter? = null

    private val alCoApplicants = ArrayList<EmploymentApplicantsModel>()

    companion object {
        fun newInstance(): EmploymentInfoFragmentNew {
            return EmploymentInfoFragmentNew()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_employment_new)
        binding.lifecycleOwner = this
        init()
        return view
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        setApplicantAdapter()

        LeadMetaData.getLeadObservable().observe(this, Observer { leadDetail ->
            leadDetail?.let {
                val applicantsList = leadDetail.employmentData.applicantDetails
                setClickListeners()
                refreshApplicantData(applicantsList)
            }
        })
    }

    private fun setApplicantAdapter() {
        pagerAdapterApplicants = EmploymentPagerAdapter(fragmentManager!!, alCoApplicants)
        binding.viewPager.adapter = pagerAdapterApplicants
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }

    private fun refreshApplicantData(applicantDetails: ArrayList<EmploymentApplicantsModel>) {
        alCoApplicants.clear()
        alCoApplicants.addAll(applicantDetails)
        pagerAdapterApplicants?.notifyDataSetChanged()
    }

    private fun setClickListeners() {
        binding.btnPrevious.setOnClickListener {
            AppEvents.fireEventLoanAppChangeNavFragmentPrevious()
        }
        binding.btnNext.setOnClickListener {
            checkValidationAndProceed()
        }
    }

    private fun checkValidationAndProceed() {
        val eApplicantList = ArrayList<EmploymentApplicantsModel>()
        var errorCount = 0
        val fragments = pagerAdapterApplicants?.getAllFragments()
        fragments?.let {
            fragments.forEach { _, item ->
                if (item.isValidFragment()) {
                    val applicant = item.getApplicant()
                    eApplicantList.add(applicant)
                } else ++errorCount

                if (errorCount <= 0) {
                    LeadMetaData().saveEmploymentData(eApplicantList)
                    AppEvents.fireEventLoanAppChangeNavFragmentNext()
                }
            }
        }
    }
}
