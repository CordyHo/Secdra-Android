package com.cordy.secdra.module.user.model

import com.cordy.secdra.module.user.interfaces.IUserInterface
import com.cordy.secdra.utils.UrlRequest
import org.json.JSONObject

class MUserModel(private val iUserInterface: IUserInterface) {

    fun login(phone: String?, pw: String?) {
        UrlRequest()
                .url("account/signIn")
                .param("phone", phone)
                .param("password", pw)
                .getTokenFromUrlLoginPost(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                        iUserInterface.loginSuccess(JSONObject(jsonData).getString("message"))
                    }

                    override fun onRequestFailure(errMsg: String) {
                        iUserInterface.loginFailure(errMsg)
                    }
                })
    }
}