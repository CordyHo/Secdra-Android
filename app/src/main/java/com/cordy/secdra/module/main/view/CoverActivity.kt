@file:Suppress("DEPRECATION")

package com.cordy.secdra.module.main.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R

class CoverActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //刘海适配全屏
        if (Build.VERSION.SDK_INT >= 28) {
            val params = window.attributes
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)
        if (Build.VERSION.SDK_INT >= 30) {
            //隐藏SystemBar，适配Android 11
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.hide(WindowInsets.Type.systemBars())
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            //隐藏SystemBar
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = uiOptions
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 800)
    }

    override fun onBackPressed() {}
}
