package com.cordy.secdra.module.permission.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.widget.ImmersionBar

/*仅用于被启动后请求权限并处理返回*/
class PermissionActivity : BaseActivity() {

    private var permissionName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar(this).setImmersionBar()
        super.onCreate(savedInstanceState)
        intent?.getStringExtra("permissionName")?.let { permissionName = it }
        requestPermission()
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(permissionName), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1)
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                permissionDenied(false)
            } else if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && !ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                //用户点了不再显示
                permissionDenied(true)
            } else
                permissionGranted()
    }

    private fun permissionGranted() {
        PermissionUtils.permissionGranted(permissionName)
        finish()
    }

    private fun permissionDenied(isNoLongerPrompt: Boolean) {
        PermissionUtils.permissionDenied(permissionName, isNoLongerPrompt)
        finish()
    }
}