package com.cordy.secdra.utils

import com.cordy.secdra.module.main.bean.JsonBeanPicture

/*用来传递图片List到VP图片查看器的中间件*/

object PicturesListMiddleware {    //静态类

    private var pictureList: ArrayList<JsonBeanPicture.DataBean.ContentBean>? = null

    fun setPictureList(pictureList: ArrayList<JsonBeanPicture.DataBean.ContentBean>) {
        this.pictureList = pictureList.clone() as ArrayList<JsonBeanPicture.DataBean.ContentBean>  //克隆传过来的list，不然会导致直接引用...
    }

    fun getPictureList(): ArrayList<JsonBeanPicture.DataBean.ContentBean>? {
        return pictureList
    }

    fun clearPictureList() {  //清空PictureList，防止内存泄漏
        pictureList?.clear()
        pictureList = null
    }
}