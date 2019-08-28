package com.cordy.secdra.module.main.view

import android.content.Intent
import android.os.Build
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
        //刘海适配全屏
        if (Build.VERSION.SDK_INT >= 28) {
            val params = window.attributes
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)
        //隐藏SystemBar
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = uiOptions
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