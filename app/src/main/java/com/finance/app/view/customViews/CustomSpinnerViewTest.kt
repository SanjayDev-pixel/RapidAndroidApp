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
import com.finance.app.presenter.connector.Ispinner
import com.finance.app.presenter.connector.ValidationHandler
import com.finance.app.view.adapters.arrayadapter.CustomSpinnerAdapter
import fr.ganfra.materialspinner.MaterialSpinner
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible

/**
 * Created by Vishal Rathi on 23/12/19.
 */
class CustomSpinnerViewTest<Type : Ispinner>(context: Context, private val dropDowns: ArrayList<Type>?, isMandatory: Boolean = true, label: String, attrs: AttributeSet? = null) : LinearLayout(context,
        attrs), AdapterView.OnItemSelectedListener, ValidationHandler {

    private lateinit var spinnerType: MaterialSpinner
    private lateinit var tvErrorText: TextView
    private var isMandatory: Boolean = true
    private lateinit var llErrorBlock: LinearLayout
    private lateinit var adapterExpendedType: CustomSpinnerAdapter<Type>
    private var mClickListener: ItemClickListener? = null

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

    interface ItemClickListener {
        fun getSelectedValue(value: String)
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        mClickListener = listener
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
        dropDowns?.let {
            adapterExpendedType = CustomSpinnerAdapter(context, R.layout.item_custom_spinner, dropDowns)
            spinnerType.adapter = adapterExpendedType
        }

        setListenerForSpinner()
    }

    private var value: Type?= null

    private fun setListenerForSpinner() {
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    value = parent.getItemAtPosition(position) as Type
//                    mClickListener?.getSelectedValue(value.getCompareValue())
                }
            }
        }
    }

    fun getSelectedId(): Int {
        return returnId(value!!.getCompareValue())
    }

    private fun returnId(value: String): Int {
        return value.toInt()
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
        val type = getSelectedObj()
        if (isMandatory && type == null) {
            showError(true)
            return false
        } else {
            showError(false)
        }
        return true
    }

    fun getSelectedObj(): Type? {
        return when (val type = spinnerType.selectedView.tag) {
            is Int -> null
            else -> when {
                type != null -> spinnerType.selectedView.tag as Type
                else -> null
            }
        }
    }

    fun setSelection(id: Int?) {
        id.let {
            for (dd in dropDowns!!) {
                if (dd.getCompareValue() == id.toString()) {
                    spinnerType.setSelection((spinnerType.adapter as CustomSpinnerAdapter<Type?>).getPosition(dd) + 1)
                }
            }
        }
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
}