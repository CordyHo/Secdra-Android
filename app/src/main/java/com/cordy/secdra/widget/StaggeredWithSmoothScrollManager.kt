package com.cordy.secdra.widget

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredWithSmoothScrollManager(spanCount: Int, orientation: Int) : StaggeredGridLayoutManager(spanCount, orientation) {

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val smoothScroller = TopSnappedSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private inner class TopSnappedSmoothScroller(context: Context) : LinearSmoothScroller(context) {

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@StaggeredWithSmoothScrollManager.computeScrollVectorForPosition(targetPosition)
        }

        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
}