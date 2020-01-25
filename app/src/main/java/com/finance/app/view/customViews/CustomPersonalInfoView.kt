package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.finance.app.R
import com.finance.app.databinding.LayoutCustomViewPersonalBinding
import com.finance.app.persistence.model.AddressDetail
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.retrofit.response.Response
import javax.inject.Inject

class CustomPersonalInfoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context,
        attrs){
    private val TAG = this.javaClass.canonicalName

    private var binding: LayoutCustomViewPersonalBinding

    @Inject
    lateinit var dataBase: DataBaseUtil

    private lateinit var applicantTab: RecyclerView
    private lateinit var addApplicant: Button
    private lateinit var spinnerCurrentCity: MaterialSpinner

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.layout_custom_view_personal, this, true)
        binding = LayoutCustomViewPersonalBinding.inflate(inflater)
        initializeViews(rootView)
    }

    private fun initializeViews(rootView: View) {
        binding.basicInfoLayout.etFirstName.setText("Vish")
        spinnerCurrentCity = rootView.findViewById(R.id.spinnerCurrentCity)
        proceedFurther()
    }

    private lateinit var activity: FragmentActivity
    private var isMandatory: Boolean = false

    fun isMandatory(isMandatory: Boolean) {
        this.isMandatory = isMandatory
    }

    private fun proceedFurther() {
        ArchitectureApp.instance.component.inject(this)

        getDropDownsFromDB()
    }

    private fun getDropDownsFromDB() {
        dataBase.provideDataBaseSource().statesDao().getAllStates().observe(activity, Observer {
            it?.let {
            }
        })
    }

}