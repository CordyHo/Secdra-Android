package com.cordy.secdra.module.search.model

import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.utils.UrlRequest
import com.google.gson.Gson

class MPictureSearchModel(private val iPictureInterface: IPictureInterface) {

    fun searchPictureFromUrl(content: String?, page: Int) {
        UrlRequest()
                .url("draw/paging?page=$page&tagList=$content")
                .getDataFromUrlGet(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                        if (page < 1)
                            iPictureInterface.getPictureListSuccess(Gson().fromJson(jsonData, JsonBeanPicture::class.java), false)
                        else
                            iPictureInterface.getPictureListSuccess(Gson().fromJson(jsonData, JsonBeanPicture::class.java), true)
                    }

                    override fun onRequestFailure(errMsg: String) {
                        iPictureInterface.getPictureListFailure(errMsg)
                    }
                })
    }
}