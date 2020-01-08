package com.cordy.secdra.utils

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
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

object SavePictureUtils {

    fun savePicture(context: Activity?, url: String?, headOrBgUrl: String = "") {   // headOrBgUrl 保存的是头像或者背景图时传入

        object : Thread() {
            override fun run() {
                // Android 10之后不允许使用File类来操作公有文件目录，建议用ContentResolver和MediaStore，大概可以兼容全版本
                val resolver = context?.contentResolver
                val values = ContentValues()  //配置需要保存图片的相关属性
                values.put(MediaStore.Images.Media.DISPLAY_NAME, "$url.jpg")  //设置文件名字，貌似只有Android 10 才能正常显示文件名
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")  //设置文件类型为image，貌似默认保存到Pictures这个文件夹里
                val insertUri: Uri? = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)  //生成要保存的文件的uri，即路径
                var inStream: FileInputStream? = null
                var outStream: OutputStream? = null
                try {
                    var byteRead: Int?
                    val cacheFileUri = getGlidePictureFromCache(url, headOrBgUrl)?.path  //得到Glide缓存图片，要在后台线程执行
                    insertUri?.let { outStream = resolver.openOutputStream(insertUri) }  //要保存的图片生成一个输出流，即一个空的文件
                    cacheFileUri?.let { inStream = FileInputStream(cacheFileUri) }   //原图片生成输入流
                    val buffer = ByteArray(1024)
                    while (inStream?.read(buffer).also { byteRead = it } != -1)
                        byteRead?.let { outStream?.write(buffer, 0, it) }   //写入要保存的图片
                    context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveSuccess)) }
                } catch (e: Exception) {
                    e.printStackTrace()
                    context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveFailure)) }
                } finally {
                    inStream?.close()
                    outStream?.close()
                }
            }
        }.start()
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
}