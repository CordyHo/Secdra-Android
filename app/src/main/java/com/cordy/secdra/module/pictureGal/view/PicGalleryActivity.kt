package com.cordy.secdra.module.pictureGal.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.pictureGal.adapter.VpPictureAdapter
import com.cordy.secdra.widget.ImmersionBar
import com.serhatsurguvec.swipablelayout.SwipeableLayout
import kotlinx.android.synthetic.main.activity_pic_gallery.*

class PicGalleryActivity : BaseActivity(), SwipeableLayout.OnLayoutCloseListener {

    private lateinit var vpPicture: ViewPager
    private lateinit var adapter: VpPictureAdapter
    private var beanList = ArrayList<JsonBeanPicture.DataBean.ContentBean>()

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
        beanList = intent?.getSerializableExtra("bean") as ArrayList<JsonBeanPicture.DataBean.ContentBean>
        adapter = VpPictureAdapter(beanList, supportFragmentManager)
        vpPicture.adapter = adapter
        intent?.run { vpPicture.currentItem = intent?.getIntExtra("pos", 0)!! }
    }

    private fun enterShareElementCallback() {
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String?, View?>?) {
                super.onMapSharedElements(names, sharedElements)
                //  val url = mipmaps[mPager!!.currentItem].tag
                val fragment = adapter.instantiateItem(vpPicture, vpPicture.currentItem) as PicFragment
                sharedElements?.clear()
                sharedElements?.set(beanList[vpPicture.currentItem].url, fragment.sharedElement)  // todo 唯一值用位置可能有错？？
            }
        })
    }

    override fun OnLayoutClosed() {
        onBackPressed()
    }

    override fun onBackPressed() {
        sbl_layout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent("scrollPos").putExtra("scrollPos", vpPicture.currentItem))
        setResult(RESULT_OK, Intent().putExtra("pos", vpPicture.currentItem))
        supportFinishAfterTransition()
    }

    override fun initView() {
        vpPicture = vp_picture
        sbl_layout.setOnLayoutCloseListener(this)
    }
}