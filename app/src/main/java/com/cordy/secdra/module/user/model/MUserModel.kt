package com.cordy.secdra.module.user.model

import com.cordy.secdra.module.user.interfaces.IUserInterface
import com.cordy.secdra.utils.UrlRequest
import org.json.JSONObject

class MUserModel(private val iUserInterface: IUserInterface) {

    fun login(email: String?, pw: String?) {
        UrlRequest()
                .url("/login/login")
                .param("email", email)
                .param("password", pw)
                .getTokenFromUrlLoginPost(object : UrlRequest.DataRequestResponse {
                    override fun onRequestSuccess(jsonData: String) {
                        iUserInterface.loginSuccess(JSONObject(jsonData).getString("msg"))
                    }

                    override fun onRequestFailure(errMsg: String) {
                        iUserInterface.loginFailure(errMsg)
                    }
                })
    }
}