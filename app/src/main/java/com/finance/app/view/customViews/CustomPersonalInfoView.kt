package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.assent.Assent
import com.finance.app.R
import com.finance.app.databinding.LayoutCustomViewPersonalBinding
import com.finance.app.persistence.model.AddressDetail
import com.finance.app.viewModel.SyncDataViewModel
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.appDelegates.ViewModelType
import motobeans.architecture.appDelegates.viewModelProvider
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.DataBaseUtil
import motobeans.architecture.retrofit.response.Response
import motobeans.architecture.util.AppUtilExtensions
import javax.inject.Inject

class CustomPersonalInfoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val TAG = this.javaClass.canonicalName

    private lateinit var binding: LayoutCustomViewPersonalBinding

    @Inject
    lateinit var dataBase: DataBaseUtil

    fun attachView() {
        binding = AppUtilExtensions.initCustomViewBinding(context = context, layoutId = R.layout.layout_custom_view_personal,  container = this)
        initializeViews()
    }

    private fun initializeViews() {
        binding.basicInfoLayout.etFirstName.setText("Vishal")
        //proceedFurther()
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
