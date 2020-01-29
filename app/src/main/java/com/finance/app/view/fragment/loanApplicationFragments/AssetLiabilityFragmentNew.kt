package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.finance.app.R
import com.finance.app.databinding.AssetLiabilityFragmentFormBinding
import com.finance.app.databinding.FragmentAssetliabilityNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.Injection
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AssetLiabilityModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadDetail
import com.finance.app.view.adapters.recycler.adapter.PersonalPagerAdapter
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import java.util.*
import javax.inject.Inject


class AssetLiabilityFragmentNew {}
/**
 * Created by motobeans on 2/16/2018.
 */
/*
class AssetLiabilityFragmentNew : BaseFragment(){

    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentAssetliabilityNewBinding
    private lateinit var appDataViewModel: AppDataViewModel
    private var pagerAdapter: PersonalPagerAdapter? = null
    private var leadData: LeadMetaData? = null

    private var index = 0

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil



    companion object {
        private var count = 0
        fun newInstance(): AssetLiabilityFragmentNew {
            return AssetLiabilityFragmentNew()
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_assetliability_new)
        binding.lifecycleOwner = this
        init()
        return view
    }




    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        leadDetail?.let {
            setClickListeners()
            setCoApplicantInTabs(leadDetail!!.assetLiabilityData.applicantDetails)
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
        for (index in 0 until leadDetail!!.assetLiabilityData.applicantDetails.size) {
            val page: Fragment? = childFragmentManager.findFragmentByTag("android:switcher:" + binding.viewPager.toString() + ":" + index)
            page?.let {
                val tab = page as AssetLiabilityFragmentForm
                if (tab.isValidFragment()) {
                    ++AssetLiabilityFragmentNew.count
                }
            }
        }
        if (AssetLiabilityFragmentNew.count == leadDetail!!.assetLiabilityData.applicantDetails.size) {
            AppEvents.fireEventLoanAppChangeNavFragmentNext()
        }
    }
    private fun setCoApplicantInTabs(coApplicantsList: ArrayList<AssetLiabilityModel>) {
        pagerAdapter = PersonalPagerAdapter(fragmentManager!!)
        for (index in 0 until coApplicantsList.size) {
            pagerAdapter!!.addFragment(AssetLiabilityFragmentNew.newInstance(coApplicantsList[index], index), "CoApplicant ${index + 1}")
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
*/
