package com.project.agora

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionsManager private constructor() {
    /**
     * Check if has permission
     * @param context
     * @param permission
     * @return
     */
    @Synchronized
    open fun hasPermission(context: Context?, permission: String): Boolean {
        return context != null && (ActivityCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Request permissions
     * @param activity
     * @param permissions
     * @param requestCode
     */
    @Synchronized
    fun requestPermissions(activity: Activity?, permissions: Array<String?>?, requestCode: Int) {
        ActivityCompat.requestPermissions(activity!!, permissions!!, requestCode)
    }

    companion object {
        private var mInstance: PermissionsManager? = null
        val instance: PermissionsManager?
            get() {
                if (mInstance == null) {
                    mInstance = PermissionsManager()
                }
                return mInstance
            }
    }
}