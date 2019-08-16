package com.cordy.secdra.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.cordy.secdra.R
import com.cordy.secdra.SecdraApplication

object ImageLoader {

    fun setImageFromUrl(url: Any?, iv_image: ImageView) {
        val options = RequestOptions()
                .error(R.mipmap.ic_launcher)
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
        Glide.with(SecdraApplication.application!!)
                .load(url)
                .transition(DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(iv_image)
    }

    fun setPortraitFromUrl(url: Any?, iv_image: ImageView) {
        val options = RequestOptions()
                .fitCenter()
                .error(R.mipmap.ic_launcher)
                .dontAnimate()
                .placeholder(R.mipmap.ic_launcher)
        Glide.with(SecdraApplication.application!!)
                .load(url)
                .apply(options)
                .into(iv_image)
    }
}