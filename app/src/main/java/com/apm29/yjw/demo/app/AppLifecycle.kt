package com.apm29.yjw.demo.app

import android.app.Application
import android.content.Context

/**
 * ================================================
 * 用于代理 [Application] 的生命周期
 *
 * @see AppDelegate
 * Created by JessYan on 18/07/2017 17:43
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface AppLifecycle {
    fun attachBaseContext(base: Context)

    fun onCreate(application: Application)

    fun onTerminate(application: Application)
}