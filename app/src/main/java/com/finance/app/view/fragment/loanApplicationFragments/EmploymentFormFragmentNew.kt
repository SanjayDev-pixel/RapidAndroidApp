package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentFormBinding
import com.finance.app.persistence.model.EmploymentApplicantsModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants.APP.KEY_CO_APPLICANT
import motobeans.architecture.constants.Constants.APP.KEY_INDEX
import motobeans.architecture.constants.Constants.APP.KEY_PERSONAL_APPLICANT
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import javax.inject.Inject

class EmploymentFormFragmentNew : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentEmploymentFormBinding
    private var index = 0
    private lateinit var applicant: EmploymentApplicantsModel

    companion object {

        fun newInstance(coApplicant: EmploymentApplicantsModel?,
                        index: Int, personalApplicant: PersonalApplicantsModel)
                : EmploymentFormFragmentNew {

            val fragment = EmploymentFormFragmentNew()
            val args = Bundle()
            args.putSerializable(KEY_CO_APPLICANT, coApplicant)
            args.putSerializable(KEY_PERSONAL_APPLICANT, personalApplicant)
            args.putInt(KEY_INDEX, index)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_employment_form)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        val indexKey = arguments?.getInt(KEY_INDEX)
        val pApplicant = arguments?.getSerializable(KEY_PERSONAL_APPLICANT) as PersonalApplicantsModel?
        val eApplicant = arguments?.getSerializable(KEY_CO_APPLICANT) as EmploymentApplicantsModel?
        val leadId = LeadMetaData.getLeadId()

        leadId?.let {
            pApplicant?.let {
                eApplicant?.let {
                    indexKey?.let {
                        activity?.let {
                            binding.customEmploymentView.attachView(activity!!, index, eApplicant, pApplicant)
                        }
                    }
                }
            }
        }
    }

    fun isValidFragment(): Boolean {
        val applicant = binding.customEmploymentView.isValidEmploymentApplicant()
        return if (applicant == null) false
        else {
            this.applicant = applicant
            true
        }
    }

    fun getApplicant(): EmploymentApplicantsModel {
        return applicant
    }
}
