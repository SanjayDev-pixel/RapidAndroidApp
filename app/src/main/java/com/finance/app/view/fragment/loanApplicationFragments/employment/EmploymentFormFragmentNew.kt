package com.finance.app.view.fragment.loanApplicationFragments.employment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.FragmentEmploymentFormBinding
import com.finance.app.persistence.model.EmploymentApplicantsModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.constants.Constants.APP.KEY_APPLICANT
import motobeans.architecture.customAppComponents.activity.BaseFragment

class EmploymentFormFragmentNew : BaseFragment() {

    //TODO set income not consider also.... show custom view...

    private lateinit var binding: FragmentEmploymentFormBinding

    companion object {

        fun newInstance(selectedApplicant: PersonalApplicantsModel): EmploymentFormFragmentNew {
            val fragment = EmploymentFormFragmentNew()
            val args = Bundle()
            args.putSerializable(KEY_APPLICANT, selectedApplicant)
            fragment.arguments = args
            return fragment
        }
    }

    override fun init() {
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_employment_form)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Now fetch arguments...
        fetchArguments()
    }

    private fun fetchArguments() {
        arguments?.getSerializable(KEY_APPLICANT)?.let { applicantDetails ->
            val applicant = applicantDetails as PersonalApplicantsModel
            fetchApplicantEmploymentDetails(applicant)
        }
    }

    private fun fetchApplicantEmploymentDetails(applicant: PersonalApplicantsModel) {
        LeadMetaData.getLeadObservable().observe(this, Observer {
            it?.let { leadDetails ->
                val employmentList = leadDetails.employmentData.applicantDetails.filter { employmentDetail -> employmentDetail.leadApplicantNumber.equals(applicant.leadApplicantNumber, true) }
                if (employmentList.isNotEmpty())
                    inflateFormView(applicant, employmentList[0])
            }
        })
    }

    private fun inflateFormView(applicantsDetail: PersonalApplicantsModel, employmentDetail: EmploymentApplicantsModel) {
        activity?.let { activityInstance ->
            binding.customEmploymentView.attachView(activityInstance, applicantsDetail, employmentDetail)
        }
    }

//    fun isIncomeConsidered() = binding.customEmploymentView.isIncomeConsidered()

    fun isEmploymentDetailsValid() = binding.customEmploymentView.isValidEmploymentDetails()

    fun getApplicantEmploymentDetails() = binding.customEmploymentView.getEmploymentDetails()

}
