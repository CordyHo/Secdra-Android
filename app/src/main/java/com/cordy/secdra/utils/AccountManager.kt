package com.cordy.secdra.utils

object AccountManager {
    private const val SIGN_TAG = "SIGN_TAG"
    private const val TOKEN = "TOKEN"
    private const val USER_DETAILS = "USER_DETAILS"

    val isSignIn: Boolean
        get() = PreferencesHelper.getAppFlag(SIGN_TAG)

    var token: String?
        get() = PreferencesHelper.getCustomAppProfile(TOKEN)     //getToken
        set(token) = PreferencesHelper.addCustomAppProfile(TOKEN, token)      //setToken

    var userDetails: String?
        get() = PreferencesHelper.getCustomAppProfile(USER_DETAILS)  //get用户信息
        set(userDetails) = PreferencesHelper.addCustomAppProfile(USER_DETAILS, userDetails)    //set用户信息

    //保存用户登录状态，登录后调用
    fun setSignState(state: Boolean) {
        PreferencesHelper.setAppFlag(SIGN_TAG, state)
    }

    fun logoutWithClear() {
        // 移除SharedPreferences保存的信息
        PreferencesHelper.setAppFlag(SIGN_TAG, false)
        PreferencesHelper.removeAppFlag(TOKEN)
        PreferencesHelper.removeAppFlag(USER_DETAILS)
    }
}