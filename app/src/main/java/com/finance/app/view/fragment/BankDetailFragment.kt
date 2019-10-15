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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.finance.app.databinding.FragmentBankDetailBinding
import com.finance.app.persistence.model.DropdownMaster
import com.finance.app.utility.UploadData
import com.finance.app.view.adapters.recycler.adapter.MasterSpinnerAdapter
import motobeans.architecture.application.ArchitectureApp
import motobeans.architecture.development.interfaces.FormValidation
import motobeans.architecture.development.interfaces.SharedPreferencesUtil
import javax.inject.Inject

class BankDetailFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentBankDetailBinding
    private lateinit var mContext: Context
    private val frag: Fragment = this
    @Inject
    lateinit var sharedPreferences: SharedPreferencesUtil
    @Inject
    lateinit var formValidation: FormValidation

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
        ArchitectureApp.instance.component.inject(this)
        setDropDownValue()
        setClickListeners()
        checkIncomeConsideration()
    }

    private fun checkIncomeConsideration() {
        val selected = sharedPreferences.getIncomeConsideration()
        if (!selected) {
            Toast.makeText(context, "Income not considered in Loan Information",
                    Toast.LENGTH_SHORT).show()
            disableAllFields()
        }
    }

    private fun disableAllFields() {
        formValidation.disableBankDetailFields(binding)
    }

    private fun setClickListeners() {
        binding.ivUploadStatement.setOnClickListener {
            UploadData(frag, context!!)
        }
    }

    private fun setDropDownValue() {
        val lists: ArrayList<DropdownMaster> = ArrayList()

        binding.spinnerAccountType.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerBankName.adapter = MasterSpinnerAdapter(mContext, lists)
        binding.spinnerSalaryCredit.adapter = MasterSpinnerAdapter(mContext, lists)
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
