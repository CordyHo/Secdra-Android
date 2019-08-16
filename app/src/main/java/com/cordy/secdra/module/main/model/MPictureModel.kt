package com.cordy.secdra.module.main.model

import com.cordy.secdra.utils.UrlRequest

class MPictureModel() {

    fun getMainPictureFromUrl() {
        UrlRequest()
                .url("")
                .getDataFromUrlPost(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                    }

                    override fun onRequestFailure(errMsg: String) {
                    }

                })
    }
}