@file:Suppress("DEPRECATION")

package com.cordy.secdra.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cordy.secdra.R
import com.cordy.secdra.SecdraApplication
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream


@SuppressLint("InlinedApi")
object SavePictureUtils {

    private var PICTURE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/"

    fun savePicture(context: Activity?, url: String?, headOrBgUrl: String = "") {   // headOrBgUrl 保存的是头像或者背景图时传入

        object : Thread() {
            override fun run() {
                if (Build.VERSION.SDK_INT >= 29)
                    savePictureFor10Up(context, url, headOrBgUrl)
                else
                    try {
                        savePictureFor10Down(context, url, headOrBgUrl)
                        context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveSuccess)) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveFailure)) }
                    }
            }
        }.start()
    }

    private fun savePictureFor10Down(context: Activity?, url: String?, headOrBgUrl: String = "") {  //Android 10以下
        createPictureFolder()
        val newFile = File("$PICTURE_PATH$url.jpg")  // 新建一个复制缓存图片的文件
        val cacheFile = getGlidePictureFromCache(url, headOrBgUrl)  //得到Glide缓存图片，要在后台线程执行
        cacheFile?.copyTo(newFile, true)   //把缓存图片复制到新文件
        //更新相册
        MediaScannerConnection.scanFile(context, arrayOf(newFile.absolutePath), arrayOf(MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")), null)
    }

    private fun savePictureFor10Up(context: Activity?, url: String?, headOrBgUrl: String = "") {   // Android 10以上
        // Android 10之后不允许使用File类来操作公有文件目录，要用ContentResolver和MediaStore
        val insertUri: Uri?  //需要保存的图片的路径uri
        val cacheFileUri = getGlidePictureFromCache(url, headOrBgUrl)?.path  //得到Glide缓存图片，要在后台线程执行
        val resolver = context?.contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "$url.jpg")
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/$url.jpg")  //设置文件类型为image
        insertUri = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        var inStream: FileInputStream? = null
        var outStream: OutputStream? = null
        val buffer: ByteArray
        var byteRead: Int?
        try {
            inStream = null
            outStream = null
            insertUri?.let { outStream = resolver?.openOutputStream(insertUri) }
            cacheFileUri?.let { inStream = FileInputStream(cacheFileUri) }
            buffer = ByteArray(1024)
            while (inStream?.read(buffer).also { byteRead = it } != -1)
                byteRead?.let { outStream?.write(buffer, 0, it) }
            context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveSuccess)) }
        } catch (e: Exception) {
            e.printStackTrace()
            context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveFailure)) }
        } finally {
            inStream?.close()
            outStream?.close()
        }
    }

    private fun getGlidePictureFromCache(url: Any?, headOrBgUrl: String? = ""): File? {
        SecdraApplication.application?.run {
            return try {
                var prefix = headOrBgUrl
                if (prefix.isNullOrBlank())
                    prefix = AppParamUtils.base_img_url
                Glide.with(this)
                        .asFile()
                        .load(prefix + url)
                        .addListener(object : RequestListener<File> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(file: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                return false
                            }
                        })
                        .submit()
                        .get()
            } catch (e: Exception) {
                null
            }
        } ?: run { return null }
    }

    private fun createPictureFolder(): Boolean {
        val file = File(PICTURE_PATH)
        return file.exists() || file.mkdirs()
    }
}