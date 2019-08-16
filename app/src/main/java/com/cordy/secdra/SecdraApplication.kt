package com.cordy.secdra


import android.app.Application
import android.content.Context
import android.text.TextUtils

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.tencent.bugly.crashreport.CrashReport

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class SecdraApplication : Application() {

    companion object {

        var httpQueues: RequestQueue? = null
            private set

        var application: Context? = null

    }

    override fun onCreate() {
        super.onCreate()
        httpQueues = Volley.newRequestQueue(this)
        application = applicationContext
        initCrashReport()
    }

    private fun initCrashReport() {
        val appID = "e9ee7a2284"
        val packageName = application?.packageName
        val processName = getProcessName(android.os.Process.myPid())
        val strategy = CrashReport.UserStrategy(application)
        strategy.isUploadProcess = processName == null || processName == packageName
        CrashReport.initCrashReport(application, appID, false, strategy)
    }

    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }
}