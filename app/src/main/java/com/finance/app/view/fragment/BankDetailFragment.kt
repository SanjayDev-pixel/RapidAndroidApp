package com.finance.app.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentLoanInformationBinding

class BankDetailFragment : Fragment() {
    private lateinit var binding: FragmentLoanInformationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoanInformationBinding.inflate(inflater, container, false)
        return binding.root
    }
}