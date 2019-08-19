package com.cordy.secdra.module.main.adapter

import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.utils.ImageLoader

class PictureRvAdapter : BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {

    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        val ivPicture = helper.getView<ImageView>(R.id.iv_picture)
        val tvMsg = helper.getView<TextView>(R.id.tv_msg)
        ImageLoader.setBaseImageFromUrl(item.url, ivPicture)

        helper.itemView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                println("第${helper.adapterPosition}张图，宽：${helper.itemView.width}，高：${helper.itemView.height}")
                tvMsg.text = "宽：${helper.itemView.width}，高：${helper.itemView.height}"
                helper.itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}