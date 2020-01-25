package com.finance.app.view.fragment.LeadInfoFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentAssetLiablityBinding
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.customAppComponents.activity.BaseFragment
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject


/**
 * Created by motobeans on 2/16/2018.
 */
class LoanInfoFragmentNew : BaseFragment(){

    companion object {
        fun newInstance(): LoanInfoFragmentNew {
            val fragment = LoanInfoFragmentNew()
            return fragment
        }
    }

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    private lateinit var binding: FragmentAssetLiablityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_asset_liablity)
        binding.setLifecycleOwner(this)
        init()
        return view
    }

    override fun init() {
        ArchitectureApp.instance.component.inject(this)

        // ToDo()
    }
}
