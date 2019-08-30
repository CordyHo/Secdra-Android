package com.cordy.secdra.module.user.view

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.user.bean.JsonBeanUser
import com.cordy.secdra.utils.AccountManager
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.ScreenUtils
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsActivity : BaseActivity() {

    private lateinit var srlRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenUtils.setStatusBariImmerse(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        initView()
        setViewData()
    }

    private fun setViewData() {
        val jsonBeanUser = Gson().fromJson(AccountManager.userDetails, JsonBeanUser::class.java)
        ImageLoader.setBackGroundImageFromUrl(jsonBeanUser.data?.background, iv_background)
    }

    override fun initView() {
        super.initView()
        srlRefresh = srl_refresh
        setToolBarMargin()
    }

    private fun setToolBarMargin() {
        val statusBarHeight = ScreenUtils.getStatusHeight(this)
        (toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).setMargins(0, statusBarHeight, 0, 0)
    }
}