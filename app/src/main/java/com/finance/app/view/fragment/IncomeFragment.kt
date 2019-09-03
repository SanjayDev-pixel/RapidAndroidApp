package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentIncomeBinding

class IncomeFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentIncomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}