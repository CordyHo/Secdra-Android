package com.cordy.secdra.utils

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.cordy.secdra.SecdraApplication.Companion.application


object ImageLoader {

    fun setBaseImageFromUrl(url: Any?, iv_image: ImageView) {  //普通图片小图
        // 防止占位图不隐藏！
        val crossFadeFactory =
            DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
        val options = RequestOptions()
            .fitCenter()
        application?.run {
            Glide.with(this)
                .load(AppParamUtils.base_img_url + url + AppParamUtils.thumb_base_image)
                .transition(DrawableTransitionOptions.with(crossFadeFactory))
                .apply(options)
                .into(iv_image)
        }
    }

    fun setBaseImageWithoutPlaceholderCallbackFromUrl(
        url: Any?,
        iv_image: ImageView?,
        pictureLoadCallBack: PictureLoadCallBack?
    ) {  //普通图片小图没有占位符
        val options = RequestOptions()
            .fitCenter()
        application?.run {
            Glide.with(this)
                .load(AppParamUtils.base_img_url + url + AppParamUtils.thumb_base_image)
                .transition(DrawableTransitionOptions().crossFade())
                .apply(options)
                .listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        drawable: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val bd = drawable as BitmapDrawable
                        pictureLoadCallBack?.onCallBack(bd.bitmap, null)
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(iv_image!!)
        }
    }

    fun setOriginBaseImageWithCallbackFromUrl(
        url: Any?,
        iv_image: ImageView,
        pictureLoadCallBack: PictureLoadCallBack?
    ) {  //普通图片原图
        val options = RequestOptions()
            .fitCenter()
        application?.run {
            Glide.with(this)
                .load(AppParamUtils.base_img_url + url)
                .apply(options)
                .skipMemoryCache(true)  //跳过内存缓存，防止OOM ？
                .transition(DrawableTransitionOptions().crossFade())
                .apply(options)
                .listener(object : RequestListener<Drawable> {

                    override fun onResourceReady(
                        drawable: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pictureLoadCallBack?.onCallBack(null, null)
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(iv_image)
        }
    }

    fun setBackGroundImageFromUrl(url: Any?, iv_image: ImageView) {  //背景图原图
        val options = RequestOptions()
            .fitCenter()
        application?.run {
            Glide.with(this)
                .load(AppParamUtils.back_ground_img_url + url)
                .transition(DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(iv_image)
        }
    }

    fun setPortraitFromUrl(url: Any?, iv_image: ImageView) {
        val options = RequestOptions()
            .dontAnimate()
            .fitCenter()
        application?.run {
            Glide.with(this)
                .load(AppParamUtils.portrait_img_url + url)
                .apply(options)
                .into(iv_image)
        }
    }

    fun setImageResource(resource: Any?, iv_image: ImageView) {
        val options = RequestOptions()
            .dontAnimate()
            .fitCenter()
        application?.run {
            Glide.with(this)
                .load(resource)
                .apply(options)
                .into(iv_image)
        }
    }
}