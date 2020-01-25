package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.finance.app.R
import com.finance.app.databinding.LayoutCustomViewPersonalBinding
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.util.AppUtilExtensions
import javax.inject.Inject

class CustomPersonalInfoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val TAG = this.javaClass.canonicalName

    private lateinit var binding: LayoutCustomViewPersonalBinding

    @Inject
    lateinit var dataBase: DataBaseUtil

    fun attachView() {
        binding = AppUtilExtensions.initCustomViewBinding(context = context,
                layoutId = R.layout.layout_custom_view_personal, container = this)
        initializeViews()
    }

    private fun initializeViews() {
        binding.basicInfoLayout.etFirstName.setText("Vish")
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
