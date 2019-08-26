package com.cordy.secdra.module.pictureGal.view

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.permission.interfaces.IPermissionCallback
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.PictureLoadCallBack
import com.cordy.secdra.utils.SavePictureUtils
import com.github.chrisbanes.photoview.PhotoView
import com.serhatsurguvec.swipablelayout.SwipeableLayout
import kotlinx.android.synthetic.main.fragment_picture.view.*

class PicFragment : Fragment(), View.OnLongClickListener, IPermissionCallback, SwipeableLayout.OnLayoutCloseListener {

    private lateinit var activity: PicGalleryActivity
    private var bean = JsonBeanPicture.DataBean.ContentBean()
    private lateinit var pbProgress: ProgressBar
    private lateinit var ivPictureOrigin: PhotoView

    val sharedElement: View?
        get() = ivPictureOrigin

    fun newFragment(bean: JsonBeanPicture.DataBean.ContentBean?): Fragment? {
        val bundle = Bundle()
        bundle.putSerializable("bean", bean)
        this.arguments = bundle
        return this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activity = context as PicGalleryActivity
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
            override fun onCallBack() {
                pbProgress.visibility = View.GONE
            }
        })
    }

    override fun onLongClick(v: View?): Boolean {   //长按检查权限后保存图片
        PermissionUtils.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, this)
        return false
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
        ivPictureOrigin.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                ivPictureOrigin.viewTreeObserver.removeOnPreDrawListener(this)
                activity.supportStartPostponedEnterTransition()  //延迟元素共享动画，更连贯
                return true
            }
        })
    }
}