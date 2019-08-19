package com.cordy.secdra.module.main.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.widget.ScaleImageView

@SuppressLint("UseSparseArrays")
class PictureRvAdapter : BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {

    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        val ivPicture = helper.getView<ScaleImageView>(R.id.iv_picture)
        ivPicture.setInitSize(item.width, item.height)  //重写IV的测量方法，设置图片宽高缩放到屏幕实际的宽高
        ImageLoader.setBaseImageFromUrl(item.url, ivPicture)
    }
}