package com.finance.app.view.utils

import android.app.Activity
import android.content.Intent

//Will Only Pick Image and PDF file...
fun Activity.startFilePickerActivity(requestCode: Int) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "*/*"
    val mimeTypes = arrayOf("image/*", "application/pdf")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    startActivityForResult(intent, requestCode)
}