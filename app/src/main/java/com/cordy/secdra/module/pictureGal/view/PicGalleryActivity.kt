package com.cordy.secdra.module.pictureGal.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.pictureGal.adapter.VpPictureAdapter
import com.cordy.secdra.widget.ImmersionBar
import com.serhatsurguvec.swipablelayout.SwipeableLayout
import kotlinx.android.synthetic.main.activity_pic_gallery.*

class PicGalleryActivity : BaseActivity(), ViewPager.OnPageChangeListener, SwipeableLayout.OnLayoutCloseListener {


    private lateinit var vpPicture: ViewPager
    private lateinit var adapter: VpPictureAdapter
    private var beanList = ArrayList<JsonBeanPicture.DataBean.ContentBean>()
    private lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar(this).setImmersionBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_gallery)
        initView()
        initVp()
        supportStartPostponedEnterTransition()  //延迟元素共享动画，更连贯
        enterShareElementCallback()
    }

    private fun initVp() {
        beanList = intent?.getSerializableExtra("beanList") as ArrayList<JsonBeanPicture.DataBean.ContentBean>
        adapter = VpPictureAdapter(beanList, supportFragmentManager)
        vpPicture.adapter = adapter
        intent?.run { vpPicture.currentItem = intent?.getIntExtra("pos", 0)!! }
        vpPicture.addOnPageChangeListener(this)
    }

    private fun enterShareElementCallback() {
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String?, View?>?) {
                val fragment = adapter.instantiateItem(vpPicture, vpPicture.currentItem) as PicFragment
                val pos = vpPicture.currentItem
                sharedElements?.clear()
                sharedElements?.set(pos.toString(), fragment.sharedElement)  // pos 作为元素共享的唯一tag
            }
        })
    }

    override fun onPageSelected(position: Int) {  //滑动VP发送广播滚动RV到相应位置
        localBroadcastManager.sendBroadcast(Intent("scrollPos").putExtra("scrollPos", vpPicture.currentItem))
    }

    override fun OnLayoutClosed() {  //下滑布局回调
        onBackPressed()
    }

    override fun onBackPressed() {
        setResult(RESULT_OK, Intent().putExtra("pos", vpPicture.currentItem))
        supportFinishAfterTransition()
    }

    override fun initView() {
        vpPicture = vp_picture
        sbl_layout.setOnLayoutCloseListener(this)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
}