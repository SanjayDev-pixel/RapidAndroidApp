package com.finance.app.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentEmploymentBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class EmploymentFragment : androidx.fragment.app.Fragment(){
    private lateinit var binding: FragmentEmploymentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmploymentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}