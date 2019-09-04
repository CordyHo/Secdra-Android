package com.cordy.secdra.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

class PhotoViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {   //vp和photoView滑动冲突，这里防止崩溃
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
        } catch (e: ArrayIndexOutOfBoundsException) {
        }
        return false
    }
}
