package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalInfoNewBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadDetail
import com.finance.app.view.adapters.recycler.adapter.PersonalPagerAdapter
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class PersonalMainFragment : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    private lateinit var binding: FragmentPersonalInfoNewBinding
    private var pagerAdapter: PersonalPagerAdapter? = null
    private var coApplicantsList: ArrayList<PersonalApplicantsModel> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_personal_info_new)
        init()
        return binding.root
    }

    override fun init() {
        //ArchitectureApp.instance.component.inject(this)
        leadDetail?.let {
            getCoApplicantsList(leadDetail!!)
        }
    }

    private fun getCoApplicantsList(lead: AllLeadMaster) {
        val personalApplicants = lead.personalData
        if ((personalApplicants?.applicantDetails ?: ArrayList()).isNullOrEmpty())
            coApplicantsList.add(LeadAndLoanDetail().getApplicant(lead.leadNumber))
        else coApplicantsList.addAll(personalApplicants!!.applicantDetails!!)
        setCoApplicantInTabs()
    }

    private fun setCoApplicantInTabs() {
        if (!coApplicantsList.isNullOrEmpty()) {
           /* pagerAdapter = PersonalPagerAdapter(fragmentManager!!)
            for (index in 0 until coApplicantsList.size) {
                pagerAdapter!!.addFragment(PersonalFormFragment.newInstance(coApplicantsList[index]), "CoApplicant ${index + 1}")
            }
            binding.viewPager.adapter = pagerAdapter
            binding.tabLead.setupWithViewPager(binding.viewPager)*/
        }
    }
}