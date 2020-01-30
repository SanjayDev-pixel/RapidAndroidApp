package com.finance.app.view.fragment.loanApplicationFragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.finance.app.R
import com.finance.app.databinding.AssetLiabilityFragmentFormBinding

import com.finance.app.databinding.FragmentAssetLiablityBinding
import com.finance.app.databinding.FragmentPersonalFormBinding
import com.finance.app.others.Injection
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.AssetLiabilityModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.LoanApplicationActivity
import com.finance.app.view.fragment.PersonalFormFragment
import com.finance.app.viewModel.AppDataViewModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject



/**
 * A simple [Fragment] subclass.
 */


class AssetLiabilityFragmentForm : BaseFragment() {


    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: AssetLiabilityFragmentFormBinding
    private lateinit var appDataViewModel: AppDataViewModel
    private var index = 0
    private lateinit var applicant: AssetLiabilityModel



    companion object {
        const val KEY_CO_APPLICANT = "coApplicant"
        const val KEY_INDEX = "index"

        fun newInstance(coApplicant: AssetLiabilityModel?, index: Int): AssetLiabilityFragmentForm {
            val fragment = AssetLiabilityFragmentForm()
            val args = Bundle()
            args.putSerializable(KEY_CO_APPLICANT, coApplicant)
            args.putInt(KEY_INDEX, index)
            fragment.arguments = args
            return fragment
        }


    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.asset_liability_fragment_form)
        init()
        return binding.root
    }



    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        val indexKey = arguments?.getInt(AssetLiabilityFragmentForm.KEY_INDEX)
        arguments?.getSerializable(AssetLiabilityFragmentForm.KEY_CO_APPLICANT)?.let { Applicant ->
            val applicant = Applicant as AssetLiabilityModel
            indexKey?.let {
                this.index = indexKey
                activity?.let {
                    val leadId = LeadMetaData.getLeadId()
                    leadId?.let {
                        binding.customAssetView.attachView(activity!!, index, applicant, leadId)
                    }
                }
            }
        }
    }



    fun isValidFragment(): Boolean {
        val applicant = binding.customAssetView.isValidAssetApplicant()
        return if (applicant == null) false
        else {
            this.applicant = applicant
            true
        }
    }

    fun getApplicant(): AssetLiabilityModel {
        return applicant
    }
}
