package com.cordy.secdra.module.user.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cordy.secdra.R
import com.cordy.secdra.SlideActivity
import com.cordy.secdra.module.user.bean.JsonBeanUser
import com.cordy.secdra.module.user.fragment.WorksFragment
import com.cordy.secdra.utils.AccountManager
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.ScreenUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_user_details.*
import java.util.*
import kotlin.math.abs

class UserDetailsActivity : SlideActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var vpInfo: ViewPager
    private lateinit var tabInfo: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenUtils.setStatusBariImmerse(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        initView()
        initVp()
        setViewData()
    }

    private fun setViewData() {
        val jsonBeanUser = Gson().fromJson(AccountManager.userDetails, JsonBeanUser::class.java)
        ImageLoader.setBackGroundImageFromUrl(jsonBeanUser.data?.background, iv_background)
        ImageLoader.setPortrait200FromUrl(jsonBeanUser.data?.head, iv_portraitSmall)
        ImageLoader.setPortrait200FromUrl(jsonBeanUser.data?.head, iv_portraitBig)
        tv_name.text = jsonBeanUser.data?.name
        tv_introduce.text = jsonBeanUser.data?.let { it.gender + it.introduction + it.address + it.birthday + it.focus }
    }

    private fun initVp() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(WorksFragment())
        fragmentList.add(WorksFragment())
        vpInfo.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> getString(R.string.works)
                    else -> getString(R.string.collect)
                }
            }

            override fun getCount(): Int {
                return fragmentList.size
            }
        }
        tabInfo.setupWithViewPager(vpInfo)
    }

    override fun onOffsetChanged(view: AppBarLayout?, verticalOffset: Int) {
        if (abs(verticalOffset) >= appbarLayout.totalScrollRange)  //CollapsingToolbarLayout 完全收起
            lv_title.visibility = View.VISIBLE
        else
            lv_title.visibility = View.INVISIBLE
    }

    override fun initView() {
        super.initView()
        vpInfo = vp_info
        tabInfo = tab_info
        appbarLayout.addOnOffsetChangedListener(this)
        setToolBarMargin()
        setInfoLayoutHeight()
    }

    private fun setInfoLayoutHeight() {
        ctl_toolbar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val statusBarHeight = ScreenUtils.getStatusHeight(this@UserDetailsActivity)
                val paddingHorizontal = ScreenUtils.dp2px(this@UserDetailsActivity, 15f)
                val paddingTop = toolbar.height + statusBarHeight + ScreenUtils.dp2px(this@UserDetailsActivity, 5f)
                val paddingBottom = ScreenUtils.dp2px(this@UserDetailsActivity, 10f)
                val ivBackgroundHeight = ScreenUtils.dp2px(this@UserDetailsActivity, 250f)
                lv_info.setPadding(paddingHorizontal, paddingTop, paddingHorizontal, 0)
                iv_background.layoutParams = CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ivBackgroundHeight + statusBarHeight + paddingBottom)
                if (toolbar.height > 0)
                    ctl_toolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setToolBarMargin() {
        val statusBarHeight = ScreenUtils.getStatusHeight(this)
        (toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).setMargins(0, statusBarHeight, 0, 0)
    }
}