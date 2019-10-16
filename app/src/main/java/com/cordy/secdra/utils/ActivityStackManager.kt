package com.cordy.secdra.utils

import android.app.Activity

object ActivityStackManager {

    private val stackList = ArrayList<Activity>()

    fun addToStack(activity: Activity) {
        stackList.add(activity)
    }

    fun onDestroyRemove(activity: Activity?) {
        activity?.let {
            if (!it.isFinishing)
                it.finish()
        }
        stackList.removeAt(stackList.indexOf(activity))
    }

    fun removeByName(className: Class<*>) {  //这里不需要remove List，因为activity finish的时候会执行在onDestroy()里执行 onDestroyRemove() 方法，防止数组越界
        for (activity in stackList)
            if (activity.javaClass == className) {
                activity.finish()
                break
            }
    }

    fun checkInstanceIsExist(className: Class<*>): Boolean {
        var isExist = false
        for (activity in stackList)
            if (activity.javaClass == className) {
                isExist = true
                break
            }
        return isExist
    }

    fun getCurrentTopActivity(): Activity? {
        return stackList.lastOrNull()
    }

    fun getBottomActivity(): Activity? {
        return stackList.firstOrNull()
    }

    fun getActivityCount(): Int {
        return stackList.size
    }
}