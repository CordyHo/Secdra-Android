package com.cordy.secdra.module.pictureGal.view

import android.os.Bundle
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar(this).setImmersionBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_gallery)
        initView()
        initVp()
    }

    private fun initVp() {
        val jsonBeanPicture = intent?.getSerializableExtra("bean") as ArrayList<JsonBeanPicture.DataBean.ContentBean>
        adapter = VpPictureAdapter(this, jsonBeanPicture)
        vpPicture.adapter = adapter
        intent?.run { vpPicture.currentItem = intent?.getIntExtra("pos", 0)!! }
    }

    override fun OnLayoutClosed() {
        onBackPressed()
    }

    override fun onPageSelected(position: Int) {
        //todo 发广播更新RV rvPicture.smoothScrollToPosition(index)  滚动位置在头顶
    }

    override fun onBackPressed() {
        sbl_layout.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent))
        finish() // 直接finish 没有元素共享动画
    }

    override fun initView() {
        super.initView()
        vpPicture = vp_picture
        vpPicture.addOnPageChangeListener(this)
        sbl_layout.setOnLayoutCloseListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

}