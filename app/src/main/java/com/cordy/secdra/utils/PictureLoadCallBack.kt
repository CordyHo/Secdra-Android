package com.cordy.secdra.utils

import android.graphics.Bitmap
import java.io.File

interface PictureLoadCallBack {
    fun onCallBack(bitmap: Bitmap?, file: File?)
}