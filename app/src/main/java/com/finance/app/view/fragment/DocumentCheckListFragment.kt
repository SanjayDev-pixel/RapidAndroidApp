package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.R
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import com.finance.app.utility.ClearBankForm
import com.finance.app.view.adapters.recycler.adapter.DocumentCheckListAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class DocumentCheckListFragment : BaseFragment(), PersonalApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentDocumentChecklistBinding
    private lateinit var mContext: Context
    private var applicantAdapterPersonal: PersonalApplicantsAdapter? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    companion object {
        private lateinit var applicantMenu: ArrayList<String>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, R.layout.fragment_document_checklist)
        init()
        return binding.root
    }

    override fun init() {
        mContext=requireContext()
        showDocumentList()
        setCoApplicants()
        setClickListeners()
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant()
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
        applicantMenu = ArrayList()
        applicantMenu.add("Applicant")
        binding.rcApplicants.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        applicantAdapterPersonal = PersonalApplicantsAdapter(context!!, applicantMenu)
        binding.rcApplicants.adapter = applicantAdapterPersonal
    }
}