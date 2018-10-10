package com.apm29.yjw.demo.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerAppComponent
import com.apm29.yjw.demo.di.module.AppModule
import com.apm29.yjw.demo.di.module.ConfigModule
import com.facebook.stetho.Stetho
import javax.inject.Inject

class AppDelegate(val context: Context) : App, AppLifecycle {
    @Inject
    lateinit var appComponentInjected: AppComponent


    override fun getAppComponent(): AppComponent {
        return appComponentInjected
    }

    override fun attachBaseContext(base: Context) {

    }

    override fun onCreate(application: Application) {
        DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .configModule(ConfigModule())
                .build()
                .inject(this)
        //install Stetho
        Stetho.initializeWithDefaults(application)
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallBackAdapter(appComponentInjected))

    }

    override fun onTerminate(application: Application) {
        ActivityManager.currentActivity = null
    }


}
@SuppressLint("StaticFieldLeak")
object ActivityManager{
    var currentActivity:Activity?=null
}
