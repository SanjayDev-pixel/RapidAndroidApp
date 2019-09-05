package com.finance.app.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.databinding.FragmentLoanInformationBinding

class BankDetailFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentBankDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBankDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}