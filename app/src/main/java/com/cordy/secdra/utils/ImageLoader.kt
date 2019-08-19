package com.cordy.secdra.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.cordy.secdra.R
import com.cordy.secdra.SecdraApplication.Companion.application

object ImageLoader {

    fun setBaseImageFromUrl(url: Any?, iv_image: ImageView) {  //普通图片小图
        val options = RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
        application?.run {
            Glide.with(this)
                    .load(AppParamUtils.base_img_url + url + AppParamUtils.thumb_base_image)
                    .transition(DrawableTransitionOptions().crossFade())
                    .apply(options)
                    .into(iv_image)
        }
    }

    fun setOriginBaseImageFromUrl(url: Any?, iv_image: ImageView) {  //普通图片原图
        val options = RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
        application?.run {
            Glide.with(this)
                    .load(AppParamUtils.base_img_url + url)
                    .transition(DrawableTransitionOptions().crossFade())
                    .apply(options)
                    .into(iv_image)
        }
    }

    fun setBackGroundImageFromUrl(url: Any?, iv_image: ImageView) {  //背景图原图
        val options = RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .fitCenter()
        application?.run {
            Glide.with(this)
                    .load(AppParamUtils.back_ground_img_url + url)
                    .transition(DrawableTransitionOptions().crossFade())
                    .apply(options)
                    .into(iv_image)
        }
    }

    fun setPortrait200FromUrl(url: Any?, iv_image: ImageView) {
        val options = RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .fitCenter()
        application?.run {
            Glide.with(this)
                    .load(AppParamUtils.portrait_img_url + url+AppParamUtils.thumb_portrait_img_200)
                    .apply(options)
                    .into(iv_image)
        }
    }

    fun setPortrait500FromUrl(url: Any?, iv_image: ImageView) {
        val options = RequestOptions()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                .fitCenter()
        application?.run {
            Glide.with(this)
                    .load(AppParamUtils.portrait_img_url + url+AppParamUtils.thumb_portrait_img_500)
                    .apply(options)
                    .into(iv_image)
        }
    }
}