package com.cordy.secdra.module.main.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.databinding.ItemRvPictureBinding
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.RvItemClickListener
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.PictureLoadCallBack
import java.io.File

class PictureRvAdapter(private val rvItemClickListener: RvItemClickListener?) : LoadMoreModule,
        BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseDataBindingHolder<ItemRvPictureBinding>>(R.layout.item_rv_picture) {

    private lateinit var palette: Palette

    override fun convert(holder: BaseDataBindingHolder<ItemRvPictureBinding>, item: JsonBeanPicture.DataBean.ContentBean) {
        holder.dataBinding?.info = item
        val ivPicture = holder.dataBinding?.ivPicture
        val ivPortrait = holder.dataBinding?.ivPortrait
        ivPicture?.setInitSize(item.width, item.height)  //重写IV的测量方法，设置图片宽高缩放到屏幕实际的宽高
        ImageLoader.setBaseImageWithoutPlaceholderCallbackFromUrl(item.url, ivPicture, object : PictureLoadCallBack {
            override fun onCallBack(bitmap: Bitmap?, file: File?) {
                if (holder.adapterPosition >= 0)
                    getPictureMainColor(bitmap, holder, holder.adapterPosition)
            }
        })
        ImageLoader.setPortraitFromUrl(item.user?.head, ivPortrait!!)
        holder.itemView.setOnClickListener {
            rvItemClickListener?.onItemClick(ivPicture!!, holder.adapterPosition)
        }
    }

    private fun getPictureMainColor(bitmap: Bitmap?, helper: BaseViewHolder, pos: Int) {
        bitmap?.let { palette = Palette.from(it).generate() }
        val color = palette.getMutedColor(Color.BLACK)
        data[pos].color = color  //保存要改变vp背景的颜色，这样滑动退出的时候，swipableLayout背景色也不会随动
        //设置文字颜色和item的背景色，文字颜色要注意深浅色
        (helper.itemView as CardView).setCardBackgroundColor(color)
        helper.getView<TextView>(R.id.tv_name).setTextColor(palette.getLightVibrantColor(Color.WHITE))
    }
}