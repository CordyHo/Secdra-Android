package com.cordy.secdra.module.pictureGal.view

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.permission.interfaces.IPermissionCallback
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.PictureLoadCallBack
import com.cordy.secdra.utils.SavePictureUtils
import com.cordy.secdra.utils.ToastUtil
import com.cordy.secdra.widget.ImmersionBar
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.fragment_picture.view.*
import java.io.File

class PicFragment : Fragment(), View.OnLongClickListener, IPermissionCallback, View.OnClickListener {

    private lateinit var activity: PicGalleryActivity
    private var bean = JsonBeanPicture.DataBean.ContentBean()
    private lateinit var pbProgress: ProgressBar
    private lateinit var ivPictureOrigin: PhotoView
    private var isHideUIBar = false

    val sharedElement: View?
        get() = ivPictureOrigin

    fun newFragment(bean: JsonBeanPicture.DataBean.ContentBean?): Fragment {
        val bundle = Bundle()
        bundle.putSerializable("bean", bean)
        this.arguments = bundle
        return this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as PicGalleryActivity
        setShowOrHideUIBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bean = arguments?.getSerializable("bean") as JsonBeanPicture.DataBean.ContentBean
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_picture, container, false)
        initView(rootView)
        setPicture()
        return rootView
    }

    private fun setPicture() {
        ImageLoader.setOriginBaseImageWithCallbackFromUrl(bean.url, ivPictureOrigin, object : PictureLoadCallBack {
            override fun onCallBack(bitmap: Bitmap?, file: File?) {
                pbProgress.visibility = View.GONE
                activity.supportStartPostponedEnterTransition()  //加载图片成功后才重新开启元素共享动画，更连贯
            }
        })
    }

    private fun setShowOrHideUIBar() {  //显示或隐藏状态栏和导航栏
        if (isHideUIBar) {
            val uiFlags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)  // 加了 SYSTEM_UI_FLAG_IMMERSIVE 不会因为触摸屏幕而调出导航栏
            activity.window.decorView.systemUiVisibility = uiFlags
            isHideUIBar = false
        } else {
            ImmersionBar(activity).setImmersionBar()
            isHideUIBar = true
        }
    }

    override fun onLongClick(v: View?): Boolean {   //长按检查权限后保存图片
        requestStoragePermission()
        return false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_pictureOrigin -> setShowOrHideUIBar()
        }
    }

    private fun requestStoragePermission() {
        PermissionUtils.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
    }

    override fun permissionGranted(permissionName: String) {  //授予了权限
        if (permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE)
            SavePictureUtils.savePicture(activity, bean.url)
    }

    override fun permissionDenied(permissionName: String, isNoLongerPrompt: Boolean) {
        if (permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE && !isNoLongerPrompt) {
            //用户拒绝权限
            AlertDialog.Builder(activity)
                    .setCancelable(true)
                    .setMessage(getString(R.string.requestStoragePermission))
                    .setPositiveButton(getString(R.string.giveCPermission)) { _, _ -> requestStoragePermission() }
                    .setNegativeButton("取消", null)
                    .show()
        } else if (permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE && isNoLongerPrompt) {
            PermissionUtils.gotoAppDetailSetting(activity)
            ToastUtil.showToastLong(R.string.requestStoragePermissionToast)
        }
    }

    private fun initView(rootView: View) {
        rootView.sbl_layout.setOnLayoutCloseListener { activity.onBackPressed() }  //下滑关闭Activity
        pbProgress = rootView.pb_progress
        ivPictureOrigin = rootView.iv_pictureOrigin
        ivPictureOrigin.setOnLongClickListener(this)
        ivPictureOrigin.setOnClickListener(this)
    }
}