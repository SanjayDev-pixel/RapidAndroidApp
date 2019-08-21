package com.finance.app.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.View
import com.finance.app.R
import com.finance.app.databinding.ActivityProfileBinding
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity
import motobeans.architecture.util.delegates.ActivityBindingProviderDelegate
import java.io.IOException

class ProfileActivity : BaseAppCompatActivity() {

    private val binding: ActivityProfileBinding by ActivityBindingProviderDelegate(
            this, R.layout.activity_profile)

    companion object {
        private const val CAMERA = 1
        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init() {
        hideToolbar()
        binding.progressBar.progress = 5 / 2 * 100
        binding.icEdit.setOnClickListener {
            binding.icEdit.visibility = View.GONE
            binding.icChangePhoto.visibility = View.VISIBLE
        }

        binding.icChangePhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            try {
                val profileImage = data?.extras!!.get("data") as Bitmap
                performActionsOnImage(profileImage)
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("Please, Try Again")
            }
        }
    }

    private fun performActionsOnImage(profileImage: Bitmap) {
        binding.ivProfile.setImageBitmap(profileImage)
    }
}

