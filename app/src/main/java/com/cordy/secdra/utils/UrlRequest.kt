package com.cordy.secdra.utils

import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.cordy.secdra.SecdraApplication
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
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

    //Get请求
    fun getDataFromUrlGet(dataRequestResponse: DataRequestResponse) {
        val stringRequest = object : StringRequest(Method.GET, url, { response ->
            val jsonObject = JSONObject(response)
            if (jsonObject.getInt("status") == AppParamUtils.httpSuccess) {
                dataRequestResponse.onRequestSuccess(response)  //state 200 请求成功
            } else {
                val msg = jsonObject.getString("message")
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
        }

        stringRequest.tag = url
        //设置超时时长
        stringRequest.retryPolicy = DefaultRetryPolicy(10000, 0, 2f)
        SecdraApplication.httpQueues?.add(stringRequest)
    }

    //Post请求
    fun getDataFromUrlPost(dataRequestResponse: DataRequestResponse) {
        val stringRequest = object : StringRequest(Method.POST, url, { response ->
            val jsonObject = JSONObject(response)
            if (jsonObject.getInt("status") == AppParamUtils.httpSuccess) {
                dataRequestResponse.onRequestSuccess(response)  //state 200 请求成功
            } else {
                val msg = jsonObject.getString("message")
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

    fun getTokenFromUrlLoginPost(dataRequestResponse: DataRequestResponse) {
        val stringRequest = object : StringRequest(Method.POST, url, { response ->
            val jsonObject = JSONObject(response)
            if (jsonObject.getInt("status") == AppParamUtils.httpSuccess) {
                dataRequestResponse.onRequestSuccess(response)  //state 200 请求成功
            } else {
                val msg = jsonObject.getString("message")
                dataRequestResponse.onRequestFailure(msg)
            }
        }, { error ->
            dataRequestResponse.onRequestFailure("error：" + error?.networkResponse?.statusCode)
        }) {

            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {  //登录拿token
                return try {
                    val responseHeaders = response.headers
                    val rawCookies = responseHeaders["token"]
                    AccountManager.token = rawCookies
                    val dataString = String(response.data, Charset.forName("utf-8"))
                    Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response))
                } catch (e: UnsupportedEncodingException) {
                    Response.error(ParseError(e))
                }
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