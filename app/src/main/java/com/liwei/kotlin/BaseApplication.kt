package com.liwei.kotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

class BaseApplication : Application() {

    private var wfCurrentActivity: WeakReference<Context>? = null

    companion object{

        var mInstance: BaseApplication? = null

        @JvmStatic
        fun getInstance(): BaseApplication? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    fun setCurrentActivity(Context: Activity) {
        wfCurrentActivity = WeakReference(Context)
    }

    fun getCurrentActivity(): Context? {
        return if (wfCurrentActivity != null) {
            wfCurrentActivity?.get()
        } else null
    }
}