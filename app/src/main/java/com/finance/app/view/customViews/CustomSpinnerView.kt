package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import com.finance.app.R
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.presenter.connector.ValidationHandler
import com.finance.app.view.adapters.arrayadapter.CustomSpinnerAdapter
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible

/**
 * Created by Vishal Rathi on 23/12/19.
 */
@Suppress("UNCHECKED_CAST")
class CustomSpinnerView<Type> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context,
        attrs), AdapterView.OnItemSelectedListener, ValidationHandler {

    private lateinit var mContext: Context
    private var isMandatory: Boolean = true
    private var dropDowns: ArrayList<Type>? = null
    private lateinit var spinnerType: MaterialSpinner
    private lateinit var tvErrorText: TextView
    private lateinit var llErrorBlock: LinearLayout

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.custom_spinner, this, true)
        initializeViews(rootView)
    }

    fun attachActivity(mContext: Context, dropdownValue: ArrayList<Type>, label: String, isMandatory: Boolean = this.isMandatory) {
        this.mContext = mContext
        this.isMandatory = isMandatory
        this.dropDowns = dropdownValue
        proceedFurther()
        setDropdownLabel(label)
    }

    override fun isMandatory(isMandatory: Boolean) {
        this.isMandatory = isMandatory
    }

    private fun initializeViews(rootView: View) {
        spinnerType = rootView.findViewById(R.id.spinnerType)
        tvErrorText = rootView.findViewById(R.id.tvErrorText)
        llErrorBlock = rootView.findViewById(R.id.llErrorBlock)

        spinnerType.onItemSelectedListener = this
    }

    private fun proceedFurther() {
        val adapterExpendedType = CustomSpinnerAdapter(context,
                R.layout.item_custom_spinner, dropDowns!!)
        spinnerType.adapter = adapterExpendedType
    }

    private fun setDropdownLabel(msg: String) {
        spinnerType.hint = msg
        spinnerType.floatingLabelText = msg
    }

    override fun resetValidation() {
        showError(false)
        spinnerType.setSelection(0)
    }

    override fun isValid(): Boolean {
        return when (isMandatory) {
            true -> validate()
            false -> !isMandatory
        }
    }

    override fun showError(isShow: Boolean) {
        when (isShow) {
            true -> llErrorBlock.exVisible()
            false -> llErrorBlock.exGone()
        }
    }

    private fun validate(): Boolean {
        val type = getSelectedType()
        if (isMandatory && type == null) {
            showError(true)
            return false
        } else {
            showError(false)
        }
        return true
    }

    fun getSelectedType(): Type? {
        return try {
            spinnerType.selectedView.tag as Type
        } catch (e: Exception) {
            null
        }
    }

    fun selectValue(id: Int?) {
        for (index in 0 until spinnerType.count - 1) {
            val obj = spinnerType.getItemAtPosition(index) as DropdownMaster
            if (obj.typeDetailID == id) {
                spinnerType.setSelection(index + 1)
                return
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        validate()
    }

    override fun getErrorMessage(): String = "Error"

    fun setError(msg: String) {
        spinnerType.error = msg
    }

}