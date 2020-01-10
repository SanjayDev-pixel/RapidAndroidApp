package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import com.finance.app.persistence.model.AllLeadMaster
import com.finance.app.persistence.model.CoApplicantsList
import com.finance.app.utility.LeadAndLoanDetail
import com.finance.app.view.adapters.recycler.adapter.ApplicantsAdapter
import com.finance.app.view.adapters.recycler.adapter.DocumentCheckListAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class DocumentCheckListFragment : BaseFragment(), ApplicantsAdapter.ItemClickListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var dataBase: DataBaseUtil
    private lateinit var binding: FragmentDocumentChecklistBinding
    private lateinit var mContext: Context
    private var applicantAdapter: ApplicantsAdapter? = null
    private var applicantTab: ArrayList<CoApplicantsList>? = ArrayList()
    private var currentPosition = 0
    private var mLead: AllLeadMaster? = null

    companion object {
        private val leadAndLoanDetail = LeadAndLoanDetail()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_document_checklist)
        init()
        return binding.root
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        mContext=requireContext()
        showDocumentList()
        setCoApplicants()
        mLead = sharedPreferences.getLeadDetail()
        setClickListeners()
    }

    override fun onApplicantClick(position: Int, coApplicant: CoApplicantsList) {
        saveCurrentApplicant()
//        ClearAssetLiabilityForm(binding, mContext, allMasterDropDown)
        currentPosition = position
        getParticularApplicantData(position)
    }

    private fun saveCurrentApplicant() {
    }

    private fun getParticularApplicantData(position: Int) {
    }

    private fun setClickListeners() {

    }

    private fun showDocumentList() {
        binding.rcDocuments.layoutManager = LinearLayoutManager(context)
        binding.rcDocuments.adapter = DocumentCheckListAdapter(context!!)
    }

    private fun setCoApplicants() {
        dataBase.provideDataBaseSource().coApplicantsDao().getCoApplicants(mLead!!.leadID!!).observe(viewLifecycleOwner, Observer { coApplicantsMaster ->
            coApplicantsMaster.let {
                if (coApplicantsMaster.coApplicantsList!!.isEmpty()) {
                    applicantTab?.add(leadAndLoanDetail.getDefaultApplicant(currentPosition, mLead!!.leadNumber!!))
                } else {
                    applicantTab = coApplicantsMaster.coApplicantsList
                }
                binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false)
                applicantAdapter = ApplicantsAdapter(context!!, applicantTab!!)
                applicantAdapter!!.setOnItemClickListener(this)
                binding.rcApplicants.adapter = applicantAdapter
            }
        })
    }
}