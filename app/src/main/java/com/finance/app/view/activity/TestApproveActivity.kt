package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.finance.app.R

import kotlinx.android.synthetic.main.activity_test_approve.*
import kotlinx.android.synthetic.main.content_test_approve.*
import motobeans.architecture.customAppComponents.activity.BaseAppCompatActivity

class TestApproveActivity : BaseAppCompatActivity() {
    override fun init() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_approve)
        setSupportActionBar(toolbar)


        nextme.setOnClickListener(){
            val intent = Intent(this, TestDeviationActivity::class.java)
            startActivity(intent)

        }

    }

}
