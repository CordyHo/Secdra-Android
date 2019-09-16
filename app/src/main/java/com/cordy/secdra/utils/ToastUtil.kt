package com.cordy.secdra.utils

import android.widget.Toast
import com.cordy.secdra.SecdraApplication

object ToastUtil {

    private var oldMsg: Any? = null
    private var time: Long = 0

    fun showToastShort(msg: Any?) {
        if (msg != oldMsg) {
            Toast.makeText(SecdraApplication.application, msg.toString(), Toast.LENGTH_SHORT).show()
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 1500) {
                Toast.makeText(SecdraApplication.application, msg.toString(), Toast.LENGTH_SHORT).show()
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }

    fun showToastLong(msg: Any?) {
        if (msg != oldMsg) {
            Toast.makeText(SecdraApplication.application, msg.toString(), Toast.LENGTH_LONG).show()
            time = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - time > 1500) {
                Toast.makeText(SecdraApplication.application, msg.toString(), Toast.LENGTH_LONG).show()
                time = System.currentTimeMillis()
            }
        }
        oldMsg = msg
    }
}