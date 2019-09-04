package com.finance.app.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.finance.app.R

class SplashScreen : AppCompatActivity() {

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val nextActivity = Intent(this,
                    LoginActivity::class.java)
            startActivity(nextActivity)
            finish()
        }, SPLASH_SCREEN_TIME_OUT)
    }
}