package com.cordy.secdra.module.permission.interfaces

interface IPermissionCallback {

    fun permissionGranted(permissionName: String)

    fun permissionDenied(permissionName: String, isNoLongerPrompt: Boolean)  //true 拒绝不再提示

}