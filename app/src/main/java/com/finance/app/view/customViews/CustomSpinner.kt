package com.finance.app.view.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.finance.app.R
import com.finance.app.presenter.connector.ValidationHandler
import com.finance.app.view.adapters.recycler.Spinner.MasterSpinnerAdapter
import motobeans.architecture.util.exGone
import motobeans.architecture.util.exVisible

/**
 * Created by Vishal Rathi on 23/12/19.
 */
class CustomSpinner : LinearLayout, AdapterView.OnItemSelectedListener, ValidationHandler {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValid(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getErrorMessage(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context,
            attrs) {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.custom_spinner, this, true)

        initializeViews(rootView)
    }

    private lateinit var activity: FragmentActivity
    private var isMandatory: Boolean = false

    fun attachActivity(activity: FragmentActivity, isMandatory: Boolean = this.isMandatory) {
        this.activity = activity
        this.isMandatory = isMandatory

//        proceedFurther()
    }

    override fun isMandatory(isMandatory: Boolean) {
        this.isMandatory = isMandatory
    }

    private lateinit var spinnerType: Spinner
    private lateinit var tvHeading: TextView
    private lateinit var tvErrorText: TextView
    private lateinit var llErrorBlock: LinearLayout

    private fun initializeViews(rootView: View) {
        spinnerType = rootView.findViewById(R.id.spinnerType)
        tvHeading = rootView.findViewById(R.id.tvHeading)
        tvErrorText = rootView.findViewById(R.id.tvErrorText)
        llErrorBlock = rootView.findViewById(R.id.llErrorBlock)

        spinnerType.onItemSelectedListener = this
    }

    private fun <T> proceedFurther() {
        val adapterExpendeType = MasterSpinnerAdapter<T>(activity,
                R.layout.spinner_textbox, ArrayList())
        spinnerType.adapter = adapterExpendeType
    }

    fun setHeading(msg: String) {
        tvHeading.text = msg
    }

    private fun <T> getSelectedType(): T? {
        return try {
            spinnerType.selectedView.tag as T
        } catch (e: Exception) {
            null
        }
    }

    override fun resetValidation() {
        showError(false)
        spinnerType.setSelection(0)
    }

//    override fun isValid(): Boolean {
//        return when (isMandatory) {
////            true -> validate()
//            false -> !isMandatory
//        }
//    }

    override fun showError(isShow: Boolean) {
        when (isShow) {
            true -> llErrorBlock.exVisible()
            false -> llErrorBlock.exGone()
        }
    }

    private fun <T> validate(): Boolean {
//        val type = getSelectedType<T>()?.id
//        if (isMandatory && !type.Ex_isNotEmptyOrNullOrBlank()) {
//            showError(true)
//            return false
//        } else {
//            showError(false)
//        }
//        return true
//    }

        fun setError(msg: String = "Required Field") {
        }

//    override fun onNothingSelected(parent: AdapterView<*>?) {
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        validate<View>()
//    }
//
//    override fun getErrorMessage(): String {
//        return ""
//    }
        return true
    }
}