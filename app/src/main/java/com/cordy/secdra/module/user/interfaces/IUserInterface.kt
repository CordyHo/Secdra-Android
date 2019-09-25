package com.cordy.secdra.module.user.interfaces

import com.cordy.secdra.module.user.bean.JsonBeanUser

interface IUserInterface {

    fun loginSuccess(msg: String)

    fun loginFailure(msg: String)

    fun getUserInfoSuccess(jsonBeanUser: JsonBeanUser)

    fun getUserInfoFailure(msg: String)
}