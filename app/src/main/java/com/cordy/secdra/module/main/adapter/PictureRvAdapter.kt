package com.cordy.secdra.module.main.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.utils.ImageLoader

class PictureRvAdapter : BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {
    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        val ivPicture = helper.getView<ImageView>(R.id.iv_picture)
        ImageLoader.setBaseImageFromUrl(item.url, ivPicture)
    }
}