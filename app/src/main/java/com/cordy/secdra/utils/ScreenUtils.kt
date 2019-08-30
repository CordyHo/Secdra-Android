package com.cordy.secdra.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat

import com.cordy.secdra.R

/**
 * 屏幕相关工具类，可以获取屏幕宽高度，还有截取屏幕
 */
class ScreenUtils private constructor() {


    companion object {

        const val STATUS_BAR_TXT_DARK = 0
        const val STATUS_BAR_TXT_LIGHT = 1

        fun setStatusBariImmerse(context: Activity) {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            context.window.statusBarColor = Color.TRANSPARENT
        }

        fun setStatusBariImmerse(context: Activity, DarkOrLight: Int) {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            context.window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= 23) {
                if (DarkOrLight == STATUS_BAR_TXT_DARK)
                    context.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//状态栏深色文字
                else if (DarkOrLight == STATUS_BAR_TXT_LIGHT)
                    context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE //状态栏默认颜色文字
            }
        }

        fun setStatusBarTxtColor(context: Activity) {
            if (Build.VERSION.SDK_INT < 26) {
                context.window.statusBarColor = ContextCompat.getColor(context, R.color.black)
            } else {
                if (Build.VERSION.SDK_INT >= 26) {
                    context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or //状态栏深色文字
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR //导航栏三大金刚键深色图标
                    context.window.navigationBarColor = ContextCompat.getColor(context, R.color.colorPrimary)
                } else
                    context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR //状态栏深色文字
            }
        }

        /**
         * 获得屏幕高度
         *
         * @param context
         * @return
         */
        fun getScreenWidth(context: Context?): Int {
            val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay?.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }

        /**
         * 获得屏幕宽度
         *
         * @param context
         * @return
         */
        fun getScreenHeight(context: Context): Int {
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val outMetrics = DisplayMetrics()
            wm.defaultDisplay?.getMetrics(outMetrics)
            return outMetrics.heightPixels
        }

        /**
         * 获得状态栏的高度
         *
         * @param context
         * @return
         */
        fun getStatusHeight(context: Context): Int {

            var statusHeight = -1
            try {
                @SuppressLint("PrivateApi")
                val clazz = Class.forName("com.android.internal.R\$dimen")
                val `object` = clazz.newInstance()
                val height = Integer.parseInt(
                        clazz.getField("status_bar_height")
                                .get(`object`).toString()
                )
                statusHeight = context.resources.getDimensionPixelSize(height)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return statusHeight
        }

        fun getNavigationBarHeight(context: Context): Int {
            return if (!checkDeviceHasNavigationBar(context))
                0
            else {
                val resourceId: Int
                val rid = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
                if (rid != 0) {
                    resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                    context.resources.getDimensionPixelSize(resourceId)
                } else
                    0
            }
        }

        private fun checkDeviceHasNavigationBar(context: Context): Boolean {
            var hasNavigationBar = false
            val rs = context.resources
            val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id)
            }
            try {
                @SuppressLint("PrivateApi") val m =
                        Class.forName("android.os.SystemProperties").getMethod("get", String::class.java)
                @SuppressLint("PrivateApi") val navBarOverride =
                        m.invoke(Class.forName("android.os.SystemProperties"), "qemu.hw.mainkeys") as String
                if ("1" == navBarOverride) {
                    hasNavigationBar = false
                } else if ("0" == navBarOverride) {
                    hasNavigationBar = true
                }
            } catch (ignored: Exception) {
            }

            return hasNavigationBar
        }

        /**
         * 获取当前屏幕截图，包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithStatusBar(activity: Activity): Bitmap? {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            var bp: Bitmap? = null
            bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
            view.destroyDrawingCache()
            return bp

        }

        /**
         * 获取当前屏幕截图，不包含状态栏
         *
         * @param activity
         * @return
         */
        fun snapShotWithoutStatusBar(activity: Activity): Bitmap? {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            val bmp = view.drawingCache
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top
            val width = getScreenWidth(activity)
            val height = getScreenHeight(activity)
            var bp: Bitmap? = null
            bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
            view.destroyDrawingCache()
            return bp
        }

        /**
         * dp转px
         *
         * @param context
         * @param dpVal
         * @return
         */
        fun dp2px(context: Context?, dpVal: Float): Int {
            return TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context?.resources?.displayMetrics
            ).toInt()
        }

        /**
         * sp转px
         *
         * @param context
         * @param spVal
         * @return
         */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    spVal, context.resources.displayMetrics
            ).toInt()
        }

        /**
         * px转dp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /**
         * px转sp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }

        /**
         * 动态设置图片宽高
         */
        fun getBitmapConfiguration(bitmap: Bitmap?, imageView: ImageView, screenRadio: Float): FloatArray {
            val screenWidth = getScreenWidth(imageView.context)
            var rawWidth = 0f
            var rawHeight = 0f
            var width = 0f
            var changeHeight = 0f
            if (bitmap == null) {
                width = screenWidth / screenRadio
                changeHeight = width
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            } else {
                rawWidth = bitmap.width.toFloat()
                rawHeight = bitmap.height.toFloat()
                if (rawHeight > 10 * rawWidth) {
                    imageView.scaleType = ImageView.ScaleType.CENTER
                } else {
                    imageView.scaleType = ImageView.ScaleType.FIT_XY
                }
                val radio = rawHeight / rawWidth
                width = screenWidth / screenRadio
                changeHeight = radio * width
            }
            return floatArrayOf(width, changeHeight)
        }
    }
}