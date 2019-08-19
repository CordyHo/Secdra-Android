package com.cordy.secdra.module.main.adapter

import android.annotation.SuppressLint
import android.util.SparseArray
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.widget.ScaleImageView

@SuppressLint("UseSparseArrays")
class PictureRvAdapter : BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {

    private val heightMap = SparseArray<Int?>()

    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        val ivPicture = helper.getView<ScaleImageView>(R.id.iv_picture)
        ivPicture.setInitSize(item.width, item.height)
        ImageLoader.setBaseImageFromUrl(item.url, ivPicture)
    }
}