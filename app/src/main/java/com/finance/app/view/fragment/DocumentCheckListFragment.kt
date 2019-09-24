package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.finance.app.databinding.FragmentPropertyBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter

class DocumentCheckListFragment : Fragment() {
    private lateinit var binding: FragmentPropertyBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPropertyBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}