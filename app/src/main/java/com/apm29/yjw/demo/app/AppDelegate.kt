package com.apm29.yjw.demo.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerAppComponent
import com.apm29.yjw.demo.di.module.AppModule
import com.apm29.yjw.demo.di.module.ConfigModule
import com.facebook.stetho.Stetho
import com.tencent.bugly.Bugly
import javax.inject.Inject
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import android.text.TextUtils
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.tencent.bugly.beta.Beta
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


class AppDelegate(val context: Context) : App, AppLifecycle {
    @Inject
    lateinit var appComponentInjected: AppComponent


    override fun getAppComponent(): AppComponent {
        return appComponentInjected
    }

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
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
        //bugly
        initBugly(application)
    }

    private fun initBugly(application: Application) {
        // 获取当前包名
        val packageName = application.packageName
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        Beta.upgradeDialogLayoutId = R.layout.dialog_update_app
        Beta.initDelay = 1000*4
        Bugly.init(application, if (BuildConfig.DEBUG) "ec04f8922b" else "c0d80a011b", BuildConfig.DEBUG)
    }

    override fun onTerminate(application: Application) {
        ActivityManager.currentActivity = null
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim()
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}

@SuppressLint("StaticFieldLeak")
object ActivityManager {
    var currentActivity: Activity? = null
}
