package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import com.finance.app.model.Modals
import com.finance.app.persistence.model.AddressDetail
import com.finance.app.persistence.model.PersonalApplicants
import com.finance.app.view.adapters.Recycler.Adapter.ApplicantsAdapter
import com.finance.app.view.adapters.Recycler.Adapter.DocumentCheckListAdapter
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class DocumentCheckListFragment : Fragment(), ApplicantsAdapter.ItemClickListener {

    private lateinit var binding: FragmentDocumentChecklistBinding
    private lateinit var mContext: Context
    private var applicantsList: ArrayList<PersonalApplicants>? = null
    private var personalAddressDetail: ArrayList<AddressDetail>? = null
    private var applicantAdapter: ApplicantsAdapter? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil

    companion object {
        private var coApplicant = 1
        private lateinit var kycList: ArrayList<Modals.AddKyc>
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
        binding.btnAddApplicant.setOnClickListener {
            onAddApplicantClick()
        }
    }

    private fun onAddApplicantClick() {
        applicantMenu.add("Co- Applicant $coApplicant")
        binding.rcApplicants.adapter!!.notifyDataSetChanged()
        coApplicant++
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
        applicantAdapter = ApplicantsAdapter(context!!, applicantMenu)
        applicantAdapter!!.setOnItemClickListener(this)
        binding.rcApplicants.adapter = applicantAdapter
    }

    override fun onApplicantClick(position: Int) {
        saveCurrentApplicant(position)
        changeCurrentApplicant()
    }

    private val applicant: PersonalApplicants
        get() {
            personalAddressDetail!!.add(AddressDetail())
            return PersonalApplicants(personalAddressDetail!!)
        }

    private fun saveCurrentApplicant(position: Int) {
        applicantsList!!.add(applicant)
        sharedPreferences.savePersonalInfoForApplicants(applicantsList!!)
    }

    private fun changeCurrentApplicant() {
    }
}