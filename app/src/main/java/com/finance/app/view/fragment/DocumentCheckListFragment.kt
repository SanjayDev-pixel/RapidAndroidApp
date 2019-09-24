package com.finance.app.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.app.databinding.FragmentDocumentChecklistBinding
import com.finance.app.view.adapters.Recycler.Adapter.DocumentCheckListAdapter

class DocumentCheckListFragment : Fragment() {
    private lateinit var binding: FragmentDocumentChecklistBinding
    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDocumentChecklistBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDocumentList()
    }

    private fun showDocumentList() {
        binding.rcDocuments.layoutManager = LinearLayoutManager(context)
        binding.rcDocuments.adapter = DocumentCheckListAdapter(context!!)
    }
}