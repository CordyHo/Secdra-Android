package com.cordy.secdra.module.user.view

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.databinding.ActivityPictureViewerBinding
import com.cordy.secdra.module.permission.interfaces.IPermissionCallback
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.utils.AppParamUtils
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.SavePictureUtils
import com.cordy.secdra.utils.ToastUtil
import com.cordy.secdra.widget.ImmersionBar

class PictureViewerActivity : BaseActivity<ActivityPictureViewerBinding>(), IPermissionCallback {

    private var isHideUIBar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        supportPostponeEnterTransition()  //延迟元素共享动画，更连贯，记得重新开启
        ImmersionBar(this).setImmersionBar()
        super.onCreate(savedInstanceState)
        when (intent?.action) {
            "head" -> ImageLoader.setPortraitFromUrl(intent?.getStringExtra("url"), vBinding.ivPicture)

            "bg" -> ImageLoader.setBackGroundImageFromUrl(intent?.getStringExtra("url"), vBinding.ivPicture)

            else   -> ImageLoader.setOriginBaseImageWithCallbackFromUrl(intent?.getStringExtra("url"), vBinding.ivPicture, null)
        }
        vBinding.sblLayout.setOnLayoutCloseListener { supportFinishAfterTransition() }
        vBinding.ivPicture.setOnClickListener { setShowOrHideUIBar() }
        vBinding.ivPicture.setOnLongClickListener {
            requestStoragePermission()
            false
        }
        supportStartPostponedEnterTransition()  //加载图片成功后才重新开启元素共享动画，更连贯
    }

    private fun requestStoragePermission() {
        PermissionUtils.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
    }

    override fun permissionGranted(permissionName: String) {
        if (permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE) when (intent?.action) {
            "head" -> SavePictureUtils.savePicture(this, intent?.getStringExtra("url"), AppParamUtils.portrait_img_url)

            "bg" -> SavePictureUtils.savePicture(this, intent?.getStringExtra("url"), AppParamUtils.back_ground_img_url)

            else   -> SavePictureUtils.savePicture(this, AppParamUtils.base_img_url + intent?.getStringExtra("url"))
        }
    }

    override fun permissionDenied(permissionName: String, isNoLongerPrompt: Boolean) {
        if (permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE && !isNoLongerPrompt) {
            //用户拒绝权限
            AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(getString(R.string.requestStoragePermission))
                .setPositiveButton(getString(R.string.giveCPermission)) { _, _ -> requestStoragePermission() }
                .setNegativeButton("取消", null)
                .show()
        } else if (permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE && isNoLongerPrompt) {
            PermissionUtils.gotoAppDetailSetting(this)
            ToastUtil.showToastLong(getString(R.string.requestStoragePermissionToast))
        }
    }

    private fun setShowOrHideUIBar() {  //显示或隐藏状态栏和导航栏
        if (isHideUIBar) {
            val uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE)  // 加了 SYSTEM_UI_FLAG_IMMERSIVE 不会因为触摸屏幕而调出导航栏
            window.decorView.systemUiVisibility = uiFlags
            isHideUIBar = false
        } else {
            ImmersionBar(this).setImmersionBar()
            isHideUIBar = true
        }
    }
}