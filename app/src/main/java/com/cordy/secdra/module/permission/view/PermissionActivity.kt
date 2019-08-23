package com.cordy.secdra.module.permission.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.cordy.secdra.R
import com.cordy.secdra.module.permission.utils.PermissionUtils
import com.cordy.secdra.utils.ToastUtil
import com.cordy.secdra.widget.ImmersionBar

/*仅用于被启动后请求权限并处理返回*/
class PermissionActivity : AppCompatActivity() {

    private var permissionName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar(this).setImmersionBar()
        super.onCreate(savedInstanceState)
        intent?.getStringExtra("permissionName")?.run { permissionName = intent?.getStringExtra("permissionName")!! }
        requestPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 //存储
            -> if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                //用户拒绝权限
                permissionDenied()
                AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setMessage(getString(R.string.requestStoragePermission))
                        .setPositiveButton(getString(R.string.giveCPermission)) { _, _ -> requestPermission() }
                        .setNegativeButton("取消", null).show()
            } else if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && !ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                //用户点了不再显示
                getAppDetailSettingIntent()
                ToastUtil.showToastLong(getString(R.string.requestStoragePermissionToast))
            } else
                permissionGranted()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT > 22)
            requestPermissions(arrayOf(permissionName), 1)
    }

    private fun permissionGranted() {
        PermissionUtils.permissionGranted()
        finish()
    }

    private fun permissionDenied() {
        PermissionUtils.permissionDenied()
        finish()
    }

    private fun getAppDetailSettingIntent() { //跳转到权限设置页面
        val localIntent = Intent()
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", packageName, null)
        localIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(localIntent)
        finish()
    }
}