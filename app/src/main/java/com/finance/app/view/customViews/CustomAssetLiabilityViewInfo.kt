package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.finance.app.R
import com.finance.app.databinding.LayoutCustomViewPersonalBinding
import com.finance.app.databinding.LayoutCustomviewAssetliabilityBinding
import com.finance.app.persistence.model.AssetLiabilityModel
import com.finance.app.persistence.model.PersonalApplicantsModel
import com.finance.app.utility.SetPersonalMandatoryField
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.util.AppUtilExtensions
import javax.inject.Inject

class CustomAssetLiabilityViewInfo @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val TAG = this.javaClass.canonicalName
@Inject
lateinit var dataBase: DataBaseUtil
@Inject
lateinit var formValidation: FormValidation
private lateinit var binding: LayoutCustomviewAssetliabilityBinding
private lateinit var activity: FragmentActivity

    fun attachView(activity: FragmentActivity, index: Int, applicant: AssetLiabilityModel, leadId: Int) {
        this.activity = activity
        //this.index = index
        binding = AppUtilExtensions.initCustomViewBinding(context = context,
                layoutId = R.layout.layout_customview_assetliability, container = this)
        initializeViews(applicant, leadId)
    }

    private fun initializeViews(applicant: AssetLiabilityModel, leadId: Int) {

       /* setDatePicker()
        setClickListeners(leadId, applicant)
        setUpCustomViews()
        proceedFurther(applicant)*/
    }

}