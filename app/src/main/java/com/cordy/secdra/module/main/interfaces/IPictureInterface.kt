package com.cordy.secdra.module.main.interfaces

import com.cordy.secdra.module.main.bean.JsonBeanPicture

interface IPictureInterface {

    fun getPictureListSuccess(jsonBeanPicture: JsonBeanPicture, isLoadMore: Boolean)

    fun getPictureListFailure(msg: String?)
}