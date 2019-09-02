@file:Suppress("DEPRECATION")

package com.cordy.secdra.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cordy.secdra.R
import com.cordy.secdra.SecdraApplication
import java.io.File

object SavePictureUtils {

    private val PICTURE_PATH = Environment.getExternalStorageDirectory().path + "/Pictures/"

    fun savePicture(context: Activity?, url: String?, headOrBgUrl: String = "") {   // headOrBgUrl 保存的是头像或者背景图时传入

        object : Thread() {
            override fun run() {
                try {
                    createPictureFolder()
                    val cacheFile = getGlidePictureFromCache(url, null, headOrBgUrl)  //得到Glide缓存图片，要在后台线程执行
                    val newFile = File("$PICTURE_PATH$url.jpg")    // 新建一个复制缓存图片的文件
                    newFile.run {
                        cacheFile?.copyTo(this, true)   //把缓存图片复制到新文件
                        context?.runOnUiThread {
                            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://${newFile.absolutePath}")))  //更新相册
                            ToastUtil.showToastLong(context.getString(R.string.saveSuccess))
                        }
                    }
                } catch (e: Exception) {
                    context?.runOnUiThread { ToastUtil.showToastLong(context.getString(R.string.saveFailure)) }
                }
            }
        }.start()
    }

    fun getGlidePictureFromCache(url: Any?, pictureLoadCallBack: PictureLoadCallBack?, headOrBgUrl: String = ""): File? {
        SecdraApplication.application?.run {
            return try {
                var prefix = headOrBgUrl
                if (prefix.isBlank())
                    prefix = AppParamUtils.base_img_url
                Glide.with(this)
                        .asFile()
                        .load(prefix + url)
                        .addListener(object : RequestListener<File> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(file: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                pictureLoadCallBack?.onCallBack(null, file)
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