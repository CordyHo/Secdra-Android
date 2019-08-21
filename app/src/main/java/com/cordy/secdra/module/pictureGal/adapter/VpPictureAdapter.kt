package com.cordy.secdra.module.pictureGal.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.utils.ImageLoadCallBack
import com.cordy.secdra.utils.ImageLoader
import kotlinx.android.synthetic.main.item_vp_picture.view.*

@SuppressLint("InflateParams")
class VpPictureAdapter(private val context: Activity, private val beanList: ArrayList<JsonBeanPicture.DataBean.ContentBean>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val rootView = LayoutInflater.from(context).inflate(R.layout.item_vp_picture, null)
        val ivPictureThumb = rootView.iv_pictureThumb
        val ivPictureOrigin = rootView.iv_pictureOrigin
        val pbProgress = rootView.pb_progress
        ImageLoader.setBaseImageWithoutPlaceholderFromUrl(beanList[position].url, ivPictureThumb)  //小图，可能加转圈
        ImageLoader.setOriginBaseImageCallBackFromUrl(beanList[position].url, object : ImageLoadCallBack {  //原图
            override fun onBitmapCallBack(bitmap: Bitmap?) {
                context.runOnUiThread {
                    ivPictureOrigin.setImageBitmap(bitmap)
                    ivPictureThumb.visibility = View.GONE
                    pbProgress.visibility = View.GONE
                }
            }
        })
        container.addView(rootView)
        return rootView
    }

    override fun destroyItem(container: ViewGroup, position: Int, oj: Any) {
        container.removeView(container)
    }

    override fun getCount(): Int {
        return beanList.size
    }

    override fun isViewFromObject(view: View, oj: Any): Boolean {
        return view == oj
    }
}