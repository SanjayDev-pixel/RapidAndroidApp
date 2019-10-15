package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import com.finance.app.model.Modals.AddKyc
import com.finance.app.view.adapters.recycler.adapter.DocumentCheckListAdapter
import com.finance.app.view.adapters.recycler.adapter.PersonalApplicantsAdapter
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class DocumentCheckListFragment : Fragment() {

    private lateinit var binding: FragmentDocumentChecklistBinding
    private lateinit var mContext: Context
    private var applicantAdapterPersonal: PersonalApplicantsAdapter? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    companion object {
        private var coApplicant = 1
        private lateinit var kycList: ArrayList<AddKyc>
        private lateinit var applicantMenu: ArrayList<String>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDocumentChecklistBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDocumentList()
        setCoApplicants()
        setClickListeners()
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