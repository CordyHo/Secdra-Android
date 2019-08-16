package com.cordy.secdra.module.main.model

import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.utils.UrlRequest
import com.google.gson.Gson

class MPictureModel(private val iPictureInterface: IPictureInterface) {

    fun getMainPictureFromUrl(page: Int) {
        UrlRequest()
                .url("")
                .param("page",page)
                .getDataFromUrlPost(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                        iPictureInterface.getPictureListSuccess(Gson().fromJson(jsonData, JsonBeanPicture::class.java))
                    }

                    override fun onRequestFailure(errMsg: String) {
                        iPictureInterface.getPictureListFailure(errMsg)
                    }

                })
    }
}