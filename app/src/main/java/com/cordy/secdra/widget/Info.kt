package com.cordy.secdra.widget

import android.graphics.PointF
import android.graphics.RectF
import android.widget.ImageView

/**
 * Created by liuheng on 2015/8/19.
 */
class Info(rect: RectF, img: RectF, widget: RectF, base: RectF, screenCenter: PointF, internal var mScale: Float, internal var mDegrees: Float, internal var mScaleType: ImageView.ScaleType) {

    // 内部图片在整个手机界面的位置
     var mRect = RectF()

    // 控件在窗口的位置
     var mImgRect = RectF()

     var mWidgetRect = RectF()

     var mBaseRect = RectF()

     var mScreenCenter = PointF()

    init {
        mRect.set(rect)
        mImgRect.set(img)
        mWidgetRect.set(widget)
        mBaseRect.set(base)
        mScreenCenter.set(screenCenter)
    }
}
