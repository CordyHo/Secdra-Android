package com.cordy.secdra.module.permission.interfaces

import java.io.Serializable

interface IPermissionCallback : Serializable {

    fun permissionGranted()

    fun permissionDenied()

}