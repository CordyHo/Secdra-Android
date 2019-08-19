package com.cordy.secdra.module.main.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.widget.ScaleImageView
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("SetTextI18n")
class PictureRvAdapter : BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {

    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        val ivPicture = helper.getView<ScaleImageView>(R.id.iv_picture)
        val ivPortrait = helper.getView<CircleImageView>(R.id.iv_portrait)
        val tvMsg = helper.getView<TextView>(R.id.tv_msg)
        ivPicture.setInitSize(item.width, item.height)  //重写IV的测量方法，设置图片宽高缩放到屏幕实际的宽高
        ImageLoader.setBaseImageFromUrl(item.url, ivPicture)
        ImageLoader.setPortrait200FromUrl(item.user?.head, ivPortrait)
        tvMsg.text = item.user?.name
    }
}