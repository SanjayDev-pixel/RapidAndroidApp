package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.CustomviewDocumentchecklistBinding
import com.finance.app.databinding.LayoutCustomviewAssetliabilityBinding
import com.finance.app.persistence.model.DocumentCheckListModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.LeadMetaData
import com.finance.app.view.adapters.recycler.adapter.AssetDetailAdapter
import com.finance.app.view.adapters.recycler.adapter.CheckListAdapter
import com.finance.app.view.adapters.recycler.adapter.DocumentCheckListAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.util.AppUtilExtensions

class CustomDocumentCheckListView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs){

    private lateinit var binding: CustomviewDocumentchecklistBinding
    private lateinit var activity: FragmentActivity
    private lateinit var selectedApplicant: PersonalApplicantsModel
    private var checklistAdapter: CheckListAdapter? = null




    init {
        ArchitectureApp.instance.component.inject(this)
        binding = AppUtilExtensions.initCustomViewBinding(context = context, layoutId = R.layout.customview_documentchecklist, container = this)

        initViews()
        setOnClickListener()
    }


    private fun initViews() {
    }

    private fun setOnClickListener() {

    }

    fun initApplicantDetails(it: FragmentActivity, applicant: PersonalApplicantsModel){
        activity = it
        selectedApplicant = applicant
        selectedApplicant = applicant

        //fetch details
        fetchApplicantCheckListDetailsById(applicant.leadApplicantNumber.toString())

    }

    private fun fetchApplicantCheckListDetailsById(applicantNumber: String) {

        LeadMetaData.getLeadObservable().observe(activity, Observer { allLeadDetails ->
            allLeadDetails?.let {
              //  val selectedApplicantList = it.assetLiabilityData.applicantDetails.filter { assetsLiability -> applicantNumber.equals(assetsLiability.leadApplicantNumber, true) }

                /*if (!selectedApplicantList.isNullOrEmpty()) {

                    if (selectedApplicantList[0].applicantAssetLiabilityList.isNullOrEmpty()) {
                        selectedApplicantList[0].applicantAssetLiabilityList = ArrayList()
                        setDocumentCheckListAdapter(selectedApplicantList[0].applicantAssetLiabilityList!!)
                    } else setDocumentCheckListAdapter(selectedApplicantList[0].applicantAssetLiabilityList!!)


                }*/
            }
        })



    }


    //get current applicant
    fun getCurrentApplicant(): DocumentCheckListModel{
        var currentApplicant:DocumentCheckListModel= DocumentCheckListModel()


        return currentApplicant

    }


}