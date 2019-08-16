package com.cordy.secdra.module.main.model

import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.utils.UrlRequest
import com.google.gson.Gson

class MPictureModel(private val iPictureInterface: IPictureInterface) {

    fun getMainPictureFromUrl(page: Int) {
        UrlRequest()
                .url("draw/paging")
                .param("page", page)
                .param("pageable", "{page:0,size:20,sort:\"createDate,desc\"}")
                .getDataFromUrlGet(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                        println("成功$jsonData")
                        iPictureInterface.getPictureListSuccess(Gson().fromJson(jsonData, JsonBeanPicture::class.java))
                    }

                    override fun onRequestFailure(errMsg: String) {
                        println("失败$errMsg")
                        iPictureInterface.getPictureListFailure(errMsg)
                    }

                })
    }
}