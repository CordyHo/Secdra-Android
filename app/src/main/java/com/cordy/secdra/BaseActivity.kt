package com.cordy.secdra

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cordy.secdra.module.main.view.MainActivity
import com.cordy.secdra.utils.ActivityStackManager
import com.cordy.secdra.utils.ToastUtil

abstract class BaseActivity : AppCompatActivity() {

    private var lastClickTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStackManager.addToStack(this)
    }

    open fun initView() {
        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        if (this is MainActivity) {
            val currentTime = System.currentTimeMillis()
            when {
                lastClickTime <= 0 -> {
                    lastClickTime = currentTime
                    ToastUtil.showToastShort(getString(R.string.press_once_again_exit) + getString(R.string.app_name))
                }

                currentTime - lastClickTime < 1500 -> super.onBackPressed()

                else -> {
                    ToastUtil.showToastShort(getString(R.string.press_once_again_exit) + getString(R.string.app_name))
                    lastClickTime = currentTime
                }
            }
        } else
            super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.onDestroyRemove(this)
    }
}
