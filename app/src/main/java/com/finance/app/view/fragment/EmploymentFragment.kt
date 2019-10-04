package com.finance.app.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finance.app.databinding.FragmentEmploymentBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.SelectDate
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.Recycler.Adapter.MasterSpinnerAdapter

class EmploymentFragment : androidx.fragment.app.Fragment(){
    private lateinit var binding: FragmentEmploymentBinding
    private lateinit var mContext: Context
    private val frag = this
    companion object {
        private const val SELECT_PDF_CODE = 1
        private const val CLICK_IMAGE_CODE = 2
        private const val SELECT_IMAGE_CODE = 3
        private var image: Bitmap? = null
        private var pdf: Uri? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEmploymentBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDatePicker()
        setClickListeners()
        setDropDownValue()
    }

    private fun setClickListeners() {
        binding.layoutSalary.ivDocumentUpload.setOnClickListener {
            UploadData(frag, mContext)
        }
    }

    private fun setDatePicker() {
        binding.layoutSalary.etJoiningDate.setOnClickListener {
            SelectDate(binding.layoutSalary.etJoiningDate, mContext)
        }
        binding.layoutSenp.etIncorporationDate.setOnClickListener{
            SelectDate(binding.layoutSenp.etIncorporationDate, mContext)
        }
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerProfileSegment.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerSubProfile.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutSalary.spinnerIndustry.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.layoutSalary.spinnerSector.adapter = MasterSpinnerAdapter(mContext, lists)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val returnUri = data!!.data
            when (requestCode) {
                SELECT_PDF_CODE -> {
                    Log.i("URI: ", returnUri?.toString())
                    pdf = returnUri
                    binding.layoutSalary.tvDocumentUpload.visibility = View.GONE
                    binding.layoutSalary.ivThumbnail.visibility = View.GONE
                    binding.layoutSalary.ivPdf.visibility = View.VISIBLE
                }
                SELECT_IMAGE_CODE -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    image = bitmap
                    binding.layoutSalary.tvDocumentUpload.visibility = View.GONE
                    binding.layoutSalary.ivThumbnail.setImageBitmap(bitmap)
                }
                CLICK_IMAGE_CODE -> {
                    val thumbnail = data.extras!!.get("data") as Bitmap
                    image = thumbnail
                    binding.layoutSalary.tvDocumentUpload.visibility = View.GONE
                    binding.layoutSalary.ivThumbnail.setImageBitmap(thumbnail)
                }
            }
        }
    }

}