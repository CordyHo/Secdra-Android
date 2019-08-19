package com.cordy.secdra.module.main.model

import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.utils.AccountManager
import com.cordy.secdra.utils.UrlRequest
import com.google.gson.Gson

class MPictureModel(private val iPictureInterface: IPictureInterface) {

    fun getMainPictureFromUrl(page: Int) {
        UrlRequest()
                .url("draw/paging?page=$page&userid=${AccountManager.userId}")
                //     .param("pageable", "{page:0,size:20,sort:\"createDate,desc\"}")
                .getDataFromUrlGet(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                        if (page < 1)
                            iPictureInterface.getPictureListSuccess(Gson().fromJson(jsonData, JsonBeanPicture::class.java))
                        else
                            iPictureInterface.getMorePictureListSuccess(Gson().fromJson(jsonData, JsonBeanPicture::class.java))
                    }

                    override fun onRequestFailure(errMsg: String) {
                        if (page < 1)
                            iPictureInterface.getPictureListFailure(errMsg)
                        else
                            iPictureInterface.getMorePictureListFailure(errMsg)
                    }

                })
    }
}