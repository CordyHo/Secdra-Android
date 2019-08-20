package com.cordy.secdra.module.pictureGal.view

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.pictureGal.adapter.VpPictureAdapter
import com.cordy.secdra.widget.ImmersionBar
import kotlinx.android.synthetic.main.activity_pic_gallery.*

class PicGalleryActivity : BaseActivity(), ViewPager.OnPageChangeListener {

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

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun initView() {
        super.initView()
        vpPicture = vp_picture
        vpPicture.addOnPageChangeListener(this)
    }
}