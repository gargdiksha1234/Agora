package com.project.agora

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object LogUtils {
    private val TAG = LogUtils::class.java.simpleName
    fun showErrorLog(tvLog: TextView?, content: String?) {
        showLog(tvLog, content)
    }

    fun showNormalLog(tvLog: TextView?, content: String?) {
        showLog(tvLog, content)
    }

    fun showLog(tvLog: TextView?, content: String?) {
        if (TextUtils.isEmpty(content) || tvLog == null) {
            return
        }
        val preContent = tvLog.text.toString().trim { it <= ' ' }
        val builder = StringBuilder()
        builder.append(formatCurrentTime())
            .append(" ")
            .append(content)
            .append("\n")
            .append(preContent)
        tvLog.post(Runnable { tvLog.text = builder })
    }

    fun showErrorToast(activity: Activity?, tvLog: TextView?, content: String?) {
        if (activity == null || activity.isFinishing) {
            Log.e(TAG, "Context is null...")
            return
        }
        if (TextUtils.isEmpty(content)) {
            return
        }
        activity.runOnUiThread(Runnable {
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
            showErrorLog(tvLog, content)
        })
    }

    fun showToast(activity: Activity?, tvLog: TextView?, content: String?) {
        if (TextUtils.isEmpty(content) || activity == null || activity.isFinishing) {
            return
        }
        activity.runOnUiThread(Runnable {
            Toast.makeText(activity, content, Toast.LENGTH_SHORT).show()
            showNormalLog(tvLog, content)
        })
    }

    private fun formatCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}