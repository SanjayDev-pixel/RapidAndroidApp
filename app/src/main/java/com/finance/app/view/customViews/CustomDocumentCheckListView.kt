package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.finance.app.R
import com.finance.app.databinding.CustomviewDocumentchecklistBinding
import com.finance.app.databinding.LayoutCustomviewAssetliabilityBinding
import com.finance.app.persistence.model.PersonalApplicantsModel
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.util.AppUtilExtensions

class CustomDocumentCheckListView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs){

    private lateinit var binding: CustomviewDocumentchecklistBinding
    private lateinit var activity: FragmentActivity
    private lateinit var selectedApplicant: PersonalApplicantsModel



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

    }


}