package com.cordy.secdra.utils

import com.cordy.secdra.SecdraApplication

object JumpToLoginUtils {

    fun jumpToLogin(errData: String): Boolean {
        return if (errData.contains("token", true) || errData.contains("失效", true)) {
            SecdraApplication.application?.run {
                //   startActivity(Intent(SecdraApplication.application, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                AccountManager.logoutWithClear()
            }
            true
        } else
            false
    }
}