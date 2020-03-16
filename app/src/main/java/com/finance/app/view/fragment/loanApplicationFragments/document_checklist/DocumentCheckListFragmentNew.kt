package com.finance.app.view.fragment.loanApplicationFragments.document_checklist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.finance.app.R
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import com.finance.app.eventBusModel.AppEvents
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.activity.PreviewActivity
import com.finance.app.view.adapters.recycler.adapter.DocumentCheckLIstPagerAdapter
import com.finance.app.view.customViews.CustomDocumentCheckListView
import kotlinx.android.synthetic.main.layout_document_checklist_form.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import java.util.ArrayList
import javax.inject.Inject


/**
 * Created by motobeans on 2/16/2018.
 */
class DocumentCheckListFragmentNew : BaseFragment() {
    @Inject
    lateinit var dataBase: DataBaseUtil
    @Inject
    lateinit var formValidation: FormValidation
    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    private lateinit var binding: FragmentDocumentChecklistBinding
    private var pagerAdapterDocumentCheckList: DocumentCheckLIstPagerAdapter? = null
    private var applicantList: ArrayList<PersonalApplicantsModel>? = null
    private lateinit var mContext: Context


    companion object {
        fun newInstance(): DocumentCheckListFragmentNew {
            return DocumentCheckListFragmentNew()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ArchitectureApp.instance.component.inject(this)
        mContext = context!!
    }

    override fun init() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_document_checklist)
        binding.lifecycleOwner = this

        initViews()
        setOnClickListener()
        return view
    }

    private fun initViews() {
    }
    private fun setOnClickListener() {
        binding.btnPrevious.setOnClickListener { AppEvents.fireEventLoanAppChangeNavFragmentPrevious() }
        binding.btnNext.setOnClickListener {
            pagerAdapterDocumentCheckList?.let { adapter->
                if(adapter.isDocumentDetailsValid()){
                    LeadMetaData().saveDocumentData(adapter.getAllChecklistDetail())
                    PreviewActivity.start(mContext)
                }
                else
                {
                    Toast.makeText(mContext,"Kindly fill Applicant and Co-Applicant details",Toast.LENGTH_LONG).show()
                }
            }


        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchLeadDetails()
    }
    private fun fetchLeadDetails() {
        LeadMetaData.getLeadObservable().observe(this, androidx.lifecycle.Observer { leadDetail ->
            leadDetail?.let {
                applicantList = it.personalData?.applicantDetails
                applicantList?.let {
                    //Set Tab Adapter...
                    setApplicantTabAdapter(it)
                }
            }
        })
    }
    private fun setApplicantTabAdapter(applicantList: ArrayList<PersonalApplicantsModel>) {
        pagerAdapterDocumentCheckList = DocumentCheckLIstPagerAdapter(fragmentManager!!, applicantList)
        binding.viewPager.adapter = pagerAdapterDocumentCheckList
        binding.tabLead.setupWithViewPager(binding.viewPager)
    }


}


