package com.cordy.secdra.module.main.view

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.user.view.LoginActivity
import com.cordy.secdra.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var lastClickTime: Long = 0
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var rvPicture: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    override fun initView() {
        srlRefresh = srl_refresh
    }

    override fun onBackPressed() {
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
    }
}