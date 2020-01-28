package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.finance.app.R
import com.finance.app.databinding.FragmentPersonalFormBinding
import com.finance.app.others.Injection
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.view.activity.LoanApplicationActivity.Companion.leadDetail
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

class PersonalFormFragmentNew : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentPersonalFormBinding
    private lateinit var appDataViewModel: AppDataViewModel
    private var index = 0

    companion object {
        const val KEY_CO_APPLICANT = "coApplicant"
        const val KEY_INDEX = "index"

        fun newInstance(coApplicant: PersonalApplicantsModel?, index: Int): PersonalFormFragmentNew {
            val fragment = PersonalFormFragmentNew()
            val args = Bundle()
            args.putSerializable(KEY_CO_APPLICANT, coApplicant)
            args.putInt(KEY_INDEX, index)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_personal_form)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        val viewModelFactory: ViewModelProvider.Factory = Injection.provideViewModelFactory(activity!!)
        appDataViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(AppDataViewModel::class.java)
        val indexKey = arguments?.getInt(KEY_INDEX)
        arguments?.getSerializable(KEY_CO_APPLICANT)?.let { Applicant ->
            val applicant = Applicant as PersonalApplicantsModel
            indexKey?.let {
                this.index = indexKey
                activity?.let {
                    binding.customPersonalView.attachView(activity!!, index, applicant, leadDetail!!.leadID!!)
                }
            }
        }
    }

    fun isValidFragment(): Boolean {
        val applicant = binding.customPersonalView.isValidPersonalApplicant()
        return if (applicant == null) false
        else {
            saveCurrentApplicant(applicant)
            true
        }
    }

    private fun saveCurrentApplicant(applicant: PersonalApplicantsModel) {
        //Todo Code to save Personal Applicant
        val applicantsList = leadDetail?.personalData?.applicantDetails!!
        if (applicantsList.size > index) {
            applicantsList[index] = applicant
        } else applicantsList.add(index, applicant)
        leadDetail!!.personalData.applicantDetails = applicantsList
        updateAndSaveLeadTODB(leadDetail!!)
    }

    private fun updateAndSaveLeadTODB(leadDetail: AllLeadMaster) {
        appDataViewModel.saveLead(leadDetail)
    }
}