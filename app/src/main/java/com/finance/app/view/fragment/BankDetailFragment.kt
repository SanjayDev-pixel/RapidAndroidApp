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
import androidx.fragment.app.Fragment
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.model.Modals
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.Recycler.Adapter.GenericSpinnerAdapter

class BankDetailFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentBankDetailBinding
    private lateinit var mContext: Context
    private val frag: Fragment = this

    companion object {
        private const val SELECT_PDF_CODE = 1
        private const val CLICK_IMAGE_CODE = 2
        private const val SELECT_IMAGE_CODE = 3
        private var image: Bitmap? = null
        private var pdf: Uri? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBankDetailBinding.inflate(inflater, container, false)
        mContext = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDropDownValue()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.ivUploadStatement.setOnClickListener {
            UploadData(frag, context!!)
        }
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerAccountType.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerBankName.adapter = GenericSpinnerAdapter(mContext, lists)
        binding.spinnerSalaryCredit.adapter = GenericSpinnerAdapter(mContext, lists)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val returnUri = data!!.data
            when (requestCode) {
                SELECT_PDF_CODE -> {
                    Log.i("URI: ", returnUri?.toString())
                    pdf = returnUri
                    binding.tvUploadBankStatement.visibility = View.GONE
                    binding.ivThumbnail.visibility = View.GONE
                    binding.ivPdf.visibility = View.VISIBLE
                }
                SELECT_IMAGE_CODE -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
                    image = bitmap
                    binding.tvUploadBankStatement.visibility = View.GONE
                    binding.ivThumbnail.setImageBitmap(bitmap)
                }
                CLICK_IMAGE_CODE -> {
                    val thumbnail = data.extras!!.get("data") as Bitmap
                    image = thumbnail
                    binding.tvUploadBankStatement.visibility = View.GONE
                    binding.ivThumbnail.setImageBitmap(thumbnail)
                }
            }
        }
    }
}
