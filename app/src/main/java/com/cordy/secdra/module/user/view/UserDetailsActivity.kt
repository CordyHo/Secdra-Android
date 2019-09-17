package com.cordy.secdra.module.user.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cordy.secdra.R
import com.cordy.secdra.SlideActivity
import com.cordy.secdra.module.user.bean.JsonBeanUser
import com.cordy.secdra.module.user.fragment.WorksFragment
import com.cordy.secdra.utils.ScreenUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_user_details.*
import java.util.*
import kotlin.math.abs

class UserDetailsActivity : SlideActivity(), AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private lateinit var vpInfo: ViewPager
    private lateinit var tabInfo: TabLayout
    private var jsonBeanUser:JsonBeanUser? = JsonBeanUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenUtils.setStatusBariImmerse(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        initView()
        initVp()
        setViewData()
    }

    private fun setViewData() {
        /*todo 提前获取用户信息*/
  /*      jsonBeanUser = Gson().fromJson(AccountManager.userDetails, JsonBeanUser::class.java)
        ImageLoader.setBackGroundImageFromUrl(jsonBeanUser?.data?.background, iv_background)
        ImageLoader.setPortraitFromUrl(jsonBeanUser?.data?.head, iv_portraitSmall)
        ImageLoader.setPortraitFromUrl(jsonBeanUser?.data?.head, iv_portraitBig)
        tv_name.text = jsonBeanUser?.data?.name
        tv_nameBig.text = jsonBeanUser?.data?.name
        tv_introduce.text = jsonBeanUser?.data?.let { it.gender + it.introduction + it.address + it.birthday + it.focus }*/
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
        if (abs(verticalOffset) >= appbarLayout.totalScrollRange) { //CollapsingToolbarLayout 完全收起
            lv_title.visibility = View.VISIBLE
            lv_info.visibility = View.INVISIBLE
        } else {
            lv_title.visibility = View.INVISIBLE
            lv_info.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_portraitBig ->
                startActivity(Intent(this, PictureViewerActivity::class.java)
                        .setAction("head")
                        .putExtra("url", jsonBeanUser?.data?.head),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "picture").toBundle())

            R.id.iv_portraitSmall -> startActivity(Intent(this, PictureViewerActivity::class.java)
                    .setAction("head")
                    .putExtra("url", jsonBeanUser?.data?.head),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "picture").toBundle())

            R.id.iv_background -> startActivity(Intent(this, PictureViewerActivity::class.java)
                    .setAction("bg")
                    .putExtra("url", jsonBeanUser?.data?.background),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, "picture").toBundle())
        }
    }

    override fun initView() {
        super.initView()
        vpInfo = vp_info
        tabInfo = tab_info
        iv_portraitBig.setOnClickListener(this)
        iv_portraitSmall.setOnClickListener(this)
        iv_background.setOnClickListener(this)
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