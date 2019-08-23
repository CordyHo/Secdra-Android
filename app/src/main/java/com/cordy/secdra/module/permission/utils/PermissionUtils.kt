package com.cordy.secdra.module.permission.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.cordy.secdra.module.permission.interfaces.IPermissionCallback
import com.cordy.secdra.module.permission.view.PermissionActivity

object PermissionUtils : IPermissionCallback {

    var iPermissionCallback: IPermissionCallback? = null

    fun requestPermission(context: Context?, permissionName: String, iPermissionCallback: IPermissionCallback) {
        this.iPermissionCallback = iPermissionCallback
        if (Build.VERSION.SDK_INT > 22) {
            context?.run {
                val checkPermission = ContextCompat.checkSelfPermission(this, permissionName)
                if (checkPermission != PackageManager.PERMISSION_GRANTED)   //没有权限则启动权限Activity
                    startActivity(Intent(context, PermissionActivity::class.java).putExtra("permissionName", permissionName))
                else
                    permissionGranted()
            }
        } else
            permissionGranted()
    }

    override fun permissionGranted() {
        iPermissionCallback?.permissionGranted()
        iPermissionCallback = null  //接口要赋值空，防止内存泄漏
    }

    override fun permissionDenied() {
        iPermissionCallback?.permissionDenied()
        iPermissionCallback = null
    }
}