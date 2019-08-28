package com.cordy.secdra.module.main.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.user.view.LoginActivity
import com.cordy.secdra.utils.AccountManager

class CoverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)
        //隐藏导航栏
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
        decorView.systemUiVisibility = uiOptions
        Handler().postDelayed({
            if (AccountManager.isSignIn)
                startActivity(Intent(this, MainActivity::class.java))
            else
                startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 800)
    }

    override fun onBackPressed() {}
}