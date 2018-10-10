package com.apm29.yjw.demo.app

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.apm29.yjw.demo.di.component.AppComponent

class AppApplication:MultiDexApplication(),App{
    val mAppDelegate:AppDelegate by lazy {
        AppDelegate(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var context:Context
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        mAppDelegate.attachBaseContext(base)
        context = base
    }

    override fun onCreate() {
        super.onCreate()
        mAppDelegate.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
    }

    override fun getAppComponent(): AppComponent {
        return (mAppDelegate as App).getAppComponent()
    }
}