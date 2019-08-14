package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.R
import com.finance.app.databinding.FragmentTempBinding
import motobeans.architecture.customAppComponents.activity.BaseFragment

/**
 * Created by motobeans on 2/16/2018.
 */
class TestFragment : BaseFragment() {

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var binding: FragmentTempBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = initBinding(inflater, container, R.layout.fragment_temp)
        init()
        return view
    }

    override fun init() {

    }
}