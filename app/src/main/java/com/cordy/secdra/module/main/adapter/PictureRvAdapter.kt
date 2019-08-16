package com.cordy.secdra.module.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture

class PictureRvAdapter : BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {
    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        item.url
    }
}