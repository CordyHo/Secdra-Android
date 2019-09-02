package com.cordy.secdra.module.user.view

import android.Manifest
import android.os.Bundle
import android.view.View
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.permission.interfaces.IPermissionCallback
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.utils.AppParamUtils
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.SavePictureUtils
import com.cordy.secdra.widget.ImmersionBar
import kotlinx.android.synthetic.main.activity_picture_viewer.*

class PictureViewerActivity : BaseActivity(), IPermissionCallback {

    private var isHideUIBar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        supportPostponeEnterTransition()  //延迟元素共享动画，更连贯，记得重新开启
        ImmersionBar(this).setImmersionBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_viewer)
        when (intent?.action) {
            "head" -> ImageLoader.setPortraitFromUrl(intent?.getStringExtra("url"), iv_picture)

            "bg" -> ImageLoader.setBackGroundImageFromUrl(intent?.getStringExtra("url"), iv_picture)

            else -> ImageLoader.setOriginBaseImageWithCallbackFromUrl(intent?.getStringExtra("url"), iv_picture, null)
        }
        sbl_layout.setOnLayoutCloseListener { supportFinishAfterTransition() }
        iv_picture.setOnClickListener { setShowOrHideUIBar() }
        iv_picture.setOnLongClickListener {
            PermissionUtils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
            false
        }
        supportStartPostponedEnterTransition()  //加载图片成功后才重新开启元素共享动画，更连贯
    }

    override fun permissionGranted() {
        when (intent?.action) {
            "head" -> SavePictureUtils.savePicture(this, AppParamUtils.portrait_img_url + intent?.getStringExtra("url"))

            "bg" -> SavePictureUtils.savePicture(this, AppParamUtils.back_ground_img_url + intent?.getStringExtra("url"))

            else -> SavePictureUtils.savePicture(this, AppParamUtils.base_img_url + intent?.getStringExtra("url"))
        }
    }

    override fun permissionDenied() {
    }

    private fun setShowOrHideUIBar() {  //显示或隐藏状态栏和导航栏
        if (isHideUIBar) {
            val uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)  // 加了 SYSTEM_UI_FLAG_IMMERSIVE 不会因为触摸屏幕而调出导航栏
            window.decorView.systemUiVisibility = uiFlags
            isHideUIBar = false
        } else {
            ImmersionBar(this).setImmersionBar()
            isHideUIBar = true
        }
    }
}