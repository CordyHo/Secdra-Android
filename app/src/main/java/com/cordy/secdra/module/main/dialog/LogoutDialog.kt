package com.cordy.secdra.module.main.dialog

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.cordy.secdra.R
import com.cordy.secdra.module.user.view.LoginActivity
import com.cordy.secdra.utils.AccountManager

class LogoutDialog {

    fun show(context: Activity) {
        AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_icon)
                .setTitle(context.getString(R.string.logout))
                .setMessage(context.getString(R.string.isExit))
                .setPositiveButton(context.getString(R.string.confirm)) { _, _ ->
                    AccountManager.logoutWithClear()  //确定清除登录信息
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show()
    }
}