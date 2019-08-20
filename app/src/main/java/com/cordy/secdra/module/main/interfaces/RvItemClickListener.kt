package com.cordy.secdra.module.main.interfaces

import android.widget.ImageView
import com.cordy.secdra.module.main.bean.JsonBeanPicture

interface RvItemClickListener {
    fun onItemClick(ivPicture: ImageView, pos: Int)
}