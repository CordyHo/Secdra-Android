package com.cordy.secdra.module.main.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cordy.secdra.R
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.RvItemClickListener
import com.cordy.secdra.utils.ImageLoader
import com.cordy.secdra.utils.PictureLoadCallBack
import com.cordy.secdra.widget.ScaleImageView
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("SetTextI18n")
class PictureRvAdapter(private val rvItemClickListener: RvItemClickListener?) :
        BaseQuickAdapter<JsonBeanPicture.DataBean.ContentBean, BaseViewHolder>(R.layout.item_rv_picture) {

    private lateinit var palette: Palette

    override fun convert(helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        val ivPicture = helper.getView<ScaleImageView>(R.id.iv_picture)
        val ivPortrait = helper.getView<CircleImageView>(R.id.iv_portrait)
        val tvName = helper.getView<TextView>(R.id.tv_name)
        ivPicture.setInitSize(item.width, item.height)  //重写IV的测量方法，设置图片宽高缩放到屏幕实际的宽高
        ImageLoader.setBaseImageWithoutPlaceholderCallbackFromUrl(item.url, ivPicture, object : PictureLoadCallBack {
            override fun onCallBack(bitmap: Bitmap) {
                if (helper.adapterPosition >= 0)
                    getPictureMainColor(bitmap, helper, item)
            }
        })
        ImageLoader.setPortrait200FromUrl(item.user?.head, ivPortrait)
        tvName.text = item.name
        helper.itemView.setOnClickListener {
            rvItemClickListener?.onItemClick(ivPicture, helper.adapterPosition)
        }
    }

    private fun getPictureMainColor(bitmap: Bitmap, helper: BaseViewHolder, item: JsonBeanPicture.DataBean.ContentBean) {
        palette = Palette.from(bitmap).generate()
        val color = palette.getMutedColor(Color.BLACK)
        item.color = color  //保存要改变vp背景的颜色，这样滑动退出的时候，SwipeableLayout背景色也不会随动
        //设置文字颜色和item的背景色，文字颜色要注意深浅色
        (helper.itemView as CardView).setCardBackgroundColor(color)
        helper.getView<TextView>(R.id.tv_name).setTextColor(palette.getLightVibrantColor(Color.WHITE))
    }
}