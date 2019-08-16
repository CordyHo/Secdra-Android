package com.cordy.secdra.module.user.interfaces

interface IUserInterface {

    fun loginSuccess(msg: String)

    fun loginFailure(msg: String)
}