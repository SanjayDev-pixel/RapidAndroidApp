package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.finance.app.R
import com.finance.app.databinding.AssetLiabilityFragmentFormBinding
import com.finance.app.databinding.FragmentAssetliabilityNewBinding
import com.finance.app.databinding.FragmentPersonalInfoNewBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.others.Injection
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AssetLiabilityModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadDetail
import com.finance.app.view.adapters.recycler.adapter.AssetLiabilityPagerAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalPagerAdapter
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import java.util.*
import javax.inject.Inject



/**
 * Created by motobeans on 2/16/2018.
 */

class AssetLiabilityFragmentNew : BaseFragment(){

    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentAssetliabilityNewBinding
    private lateinit var appDataViewModel: AppDataViewModel
    private var pagerAdapterAsset: AssetLiabilityPagerAdapter? = null
    private var pagerAdapterApplicants: PersonalPagerAdapter? = null
    private var leadData: LeadMetaData? = null
    @Inject
    lateinit var formValidation: FormValidation
    private var index = 0
    private val alCoApplicants = ArrayList<AssetLiabilityModel>()



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

        LeadMetaData.getLeadObservable().observe(this, androidx.lifecycle.Observer { leadDetail ->
            leadDetail?.let {


                val applicantsList = leadDetail.assetLiabilityData.applicantDetails

                applicantsList?.let { it1 -> setClickListeners(it1) }

                applicantsList?.let { it1 -> refreshApplicantData(it1) }

            }
        })
        setApplicantAdapter()
    }


    private fun setApplicantAdapter() {
        pagerAdapterAsset = AssetLiabilityPagerAdapter(fragmentManager!!, alCoApplicants)
        binding.viewPager.adapter = pagerAdapterAsset
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }

    private fun refreshApplicantData(applicantDetails: ArrayList<AssetLiabilityModel>) {
        alCoApplicants.clear()
        alCoApplicants.addAll(applicantDetails)
        pagerAdapterAsset?.notifyDataSetChanged()
    }

    private fun setClickListeners(applicantsList: ArrayList<AssetLiabilityModel>) {
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
    private fun checkValidationAndProceed(applicantsList: ArrayList<AssetLiabilityModel>) {
        val pApplicantList = ArrayList<AssetLiabilityModel>()

        val fragments = pagerAdapterAsset?.getAllFragments()
        fragments?.let {
            fragments.forEach { _, item ->
                if (item.isValidFragment()) {
                    val applicants = item.getApplicant()
                    pApplicantList.add(applicants)
                    ++AssetLiabilityFragmentNew.count
                }
                if (AssetLiabilityFragmentNew.count == applicantsList.size) {
                    LeadMetaData().saveAssetLiabilityData(pApplicantList)
                    AppEvents.fireEventLoanAppChangeNavFragmentNext()
                }
            }
        }
    }
}

