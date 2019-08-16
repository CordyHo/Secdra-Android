package com.cordy.secdra.utils

import com.android.volley.DefaultRetryPolicy
import com.android.volley.toolbox.StringRequest
import com.cordy.secdra.SecdraApplication
import org.json.JSONObject
import java.util.*

class UrlRequest {

    private val requestParams = HashMap<String?, String?>()
    private var url = ""

    fun url(url: String?): UrlRequest {
        this.url = AppParamUtils.base_url + url
        return this
    }

    fun param(key: String?, value: Any?): UrlRequest {
        requestParams[key] = value.toString()
        return this
    }

    //Post请求
    fun getDataFromUrlPost(dataRequestResponse: DataRequestResponse) {
        val stringRequest = object : StringRequest(Method.POST, url, { response ->
            val jsonObject = JSONObject(response)
            if (jsonObject.getInt("state") == AppParamUtils.httpSuccess) {
                dataRequestResponse.onRequestSuccess(response)  //state 0请求成功
            } else {
                val msg = jsonObject.getString("msg")
                if (!JumpToLoginUtils.jumpToLogin(msg))
                    dataRequestResponse.onRequestFailure(msg)
            }
        }, { error ->
            dataRequestResponse.onRequestFailure("error：" + error?.networkResponse?.statusCode)
        }) {

            override fun getHeaders(): Map<String?, String?> { //传头部
                requestParams[AppParamUtils.key_token] = AccountManager.token + ""
                return requestParams
            }

            override fun getParams(): Map<String?, String?> { //传Post参数
                return requestParams
            }
        }
        stringRequest.tag = url
        //设置超时时长
        stringRequest.retryPolicy = DefaultRetryPolicy(10000, 0, 2f)
        SecdraApplication.httpQueues?.add(stringRequest)
    }

    interface DataRequestResponse {

        fun onRequestSuccess(jsonData: String)

        fun onRequestFailure(errMsg: String)
    }
}