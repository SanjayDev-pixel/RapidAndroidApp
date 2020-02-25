package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.finance.app.R

import kotlinx.android.synthetic.main.activity_test_deviation.*
import kotlinx.android.synthetic.main.content_test_deviation.*
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity

class TestDeviationActivity : BaseAppCompatActivity() {
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_deviation)
        setSupportActionBar(toolbar)
        mynext.setOnClickListener(){
            val intent = Intent(this, TestApproved::class.java)
            startActivity(intent)

        }

    }

}
