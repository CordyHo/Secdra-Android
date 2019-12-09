package com.cordy.secdra.module.main.model

import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.utils.AccountManager
import com.cordy.secdra.utils.UrlRequest
import com.google.gson.Gson

class MPictureModel(private val iPictureInterface: IPictureInterface) {

    fun getMainPictureFromUrl(page: Int) {
        UrlRequest()
                .url("draw/paging")
                .param("page", page)
                .param("userid", AccountManager.userId)
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