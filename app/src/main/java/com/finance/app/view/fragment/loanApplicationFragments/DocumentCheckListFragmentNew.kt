package com.finance.app.view.fragment.loanApplicationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject


/**
 * Created by motobeans on 2/16/2018.
 */
class DocumentCheckListFragmentNew : BaseFragment(){

    companion object {
        fun newInstance(): DocumentCheckListFragmentNew {
            return DocumentCheckListFragmentNew()
        }
    }

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    private lateinit var binding: FragmentDocumentChecklistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_document_checklist)
        binding.lifecycleOwner = this
        init()
        return view
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)
        // ToDo()
    }
}
