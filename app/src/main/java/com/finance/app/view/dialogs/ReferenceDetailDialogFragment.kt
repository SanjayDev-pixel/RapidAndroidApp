package com.finance.app.view.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.finance.app.R
import com.finance.app.databinding.DialogReferenceDetailsBinding
import com.finance.app.persistence.model.AllMasterDropDown
import com.finance.app.persistence.model.ReferenceModel
import kotlinx.android.synthetic.main.dialog_bank_detail_form.*
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.util.AppUtils
import javax.inject.Inject

class ReferenceDetailDialogFragment : DialogFragment() {
    enum class Action {
        NEW,
        EDIT,
    }

    @Inject
    lateinit var formValidation: FormValidation

    private lateinit var binding: DialogReferenceDetailsBinding

    private lateinit var action: Action
    private var referenceModel: ReferenceModel? = null
    private lateinit var allMasterDropDown: AllMasterDropDown

    private lateinit var dialogCallback: OnReferenceDetailDialogCallback

    companion object {
        fun newInstance(action: Action, dialogCallback: OnReferenceDetailDialogCallback, allMasterDropDown: AllMasterDropDown, referenceModel: ReferenceModel? = null): ReferenceDetailDialogFragment {
            val fragment = ReferenceDetailDialogFragment()
            fragment.action = action
            fragment.dialogCallback = dialogCallback
            fragment.allMasterDropDown = allMasterDropDown
            fragment.referenceModel = referenceModel
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ArchitectureApp.instance.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_reference_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setOnClickListeners()
        checkActionType()
    }

    private fun initViews() {

    }

    private fun setOnClickListeners() {
        binding.btnAdd.setOnClickListener {
            if (formValidation.validateReference(binding)) {
                if (action == Action.NEW) dialogCallback.onSaveBankDetail(getFilledReferenceDetails())
                else if (action == Action.EDIT) dialogCallback.onEditBankDetail(getFilledReferenceDetails())
                dismiss()
            } else AppUtils.showToast(context, getString(R.string.validation_error))
        }
    }

    private fun checkActionType() {
        if (action == Action.EDIT) {
            btnAdd.text = getString(R.string.update)
            referenceModel?.let { setFormDetails(it) }
        }
    }

    private fun setFormDetails(bankDetail: ReferenceModel) {

    }

    private fun getFilledReferenceDetails(): ReferenceModel {
        val referenceModel = ReferenceModel()

        return referenceModel
    }

    interface OnReferenceDetailDialogCallback {
        fun onSaveBankDetail(referenceModel: ReferenceModel)
        fun onEditBankDetail(referenceModel: ReferenceModel)
    }

}