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
import com.finance.app.presenter.connector.ValidationHandler
import com.finance.app.view.adapters.arrayadapter.CustomSpinnerAdapter
import com.finance.app.view.customViews.Interfaces.IspinnerCustomView
import com.finance.app.view.customViews.Interfaces.IspinnerMainView
import com.finance.app.view.customViews.Interfaces.IspinnerModel
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible

/**
 * Created by Vishal Rathi on 23/12/19.
 */
class CustomSpinnerViewTest<Type : IspinnerModel>(context: Context, private val dropDowns: ArrayList<Type>?, label: String, attrs: AttributeSet? = null,
                                                  val ispinnerMainView: IspinnerMainView<Type>? = null) : LinearLayout(context,
        attrs), AdapterView.OnItemSelectedListener, ValidationHandler, IspinnerCustomView<Type> {

    private lateinit var spinnerType: MaterialSpinner
    private lateinit var tvErrorText: TextView
    private var isMandatory: Boolean = true
    private lateinit var llErrorBlock: LinearLayout
    private lateinit var adapterExpendedType: CustomSpinnerAdapter<Type>

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.custom_spinner, this, true)
        initializeViews(rootView)
        proceedFurther()
        setDropdownLabel(label)
    }

    override fun isMandatory(isMandatory: Boolean) {
        this.isMandatory = isMandatory
    }

    override fun initializeViews(rootView: View) {
        spinnerType = rootView.findViewById(R.id.spinnerType)
        tvErrorText = rootView.findViewById(R.id.tvErrorText)
        llErrorBlock = rootView.findViewById(R.id.llErrorBlock)

        spinnerType.onItemSelectedListener = this
    }

    private fun proceedFurther() {
        dropDowns?.let {
            adapterExpendedType = CustomSpinnerAdapter(context, R.layout.item_custom_spinner, dropDowns)
            spinnerType.adapter = adapterExpendedType
        }
        setListenerForSpinner()
    }

    private fun setListenerForSpinner() {
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    val value = parent.getItemAtPosition(position) as Type
                    ispinnerMainView?.getSelectedValue(value)
                }
            }
        }
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
        val type = getSelectedValue()
        if (isMandatory && type == null) {
            showError(true)
            return false
        } else {
            showError(false)
        }
        return true
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        validate()
    }

    override fun getErrorMessage(): String = "Error"

    fun disableSelf() {
        spinnerType.isEnabled = false
    }

    fun clearSpinner() {
        spinnerType.setSelection(0)
    }

    override fun setSelection(iSpinnerValue: String?) {
        iSpinnerValue?.let {
            try {
                for (dd in dropDowns!!) {
                    if (dd.getCompareValue() == iSpinnerValue) {
                        spinnerType.setSelection((spinnerType.adapter as CustomSpinnerAdapter<Type?>).getPosition(dd) + 1)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun getSelectedValue(): Type? {
        return try {
            spinnerType.selectedView.tag as Type
        } catch (e: Exception) {
            null
        }
    }
}