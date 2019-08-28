package com.cordy.secdra.module.pictureGal.view

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.permission.interfaces.IPermissionCallback
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.PictureLoadCallBack
import com.cordy.secdra.utils.SavePictureUtils
import com.cordy.secdra.widget.ImmersionBar
import com.github.chrisbanes.photoview.PhotoView
import com.serhatsurguvec.swipablelayout.SwipeableLayout
import kotlinx.android.synthetic.main.fragment_picture.view.*

class PicFragment : Fragment(), View.OnLongClickListener, IPermissionCallback, SwipeableLayout.OnLayoutCloseListener, View.OnClickListener {

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
        ImageLoader.setOriginBaseImageWithCallbackFromUrl(bean.url, ivPictureOrigin, object : PictureLoadCallBack {  //原图
            override fun onCallBack(bitmap: Bitmap) {
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
        PermissionUtils.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
        return false
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_pictureOrigin -> setShowOrHideUIBar()
        }
    }

    override fun OnLayoutClosed() {  //下滑关闭Activity
        activity.onBackPressed()
    }

    override fun permissionGranted() {  //授予了权限
        SavePictureUtils.savePicture(activity, bean.url)
    }

    override fun permissionDenied() {
    }

    private fun initView(rootView: View) {
        rootView.sbl_layout.setOnLayoutCloseListener(this)
        pbProgress = rootView.pb_progress
        ivPictureOrigin = rootView.iv_pictureOrigin
        ivPictureOrigin.setOnLongClickListener(this)
        ivPictureOrigin.setOnClickListener(this)
    }
}