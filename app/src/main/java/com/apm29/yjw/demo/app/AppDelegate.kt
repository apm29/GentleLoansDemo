package com.apm29.yjw.demo.app

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
import android.widget.Toast
import cn.fraudmetrix.octopus.aspirit.main.OctopusManager
import cn.jpush.android.api.JPushInterface
import com.apm29.yjw.demo.ui.dialog.UpdateDialog
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
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
        //Jpush
        initJPush(application)
        //数据魔盒初始化
        initMagicBox(application)

    }

    private fun initMagicBox(application: Application) {
        OctopusManager.getInstance().init(application, "youzd_mohe", "cf6d1c6894cc4386a31968dd17e7500b");
    }

    private fun initJPush(application: Application) {
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(application)
    }

    private fun initBugly(application: Application) {
        // 获取当前包名
        val packageName = application.packageName
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        //Beta.upgradeDialogLayoutId = R.layout.dialog_update_app
        setupBetaAndCheckUpgrade()
        Beta.initDelay = 1000*4
        Beta.autoCheckUpgrade = false
        Bugly.init(application, if (BuildConfig.DEBUG) "ec04f8922b" else "c0d80a011b", BuildConfig.DEBUG)
    }

    private fun setupBetaAndCheckUpgrade() {
        /*在application中初始化时设置监听，监听策略的收取*/
        Beta.upgradeListener = UpgradeListener { _, strategy, _, _ ->
            if (strategy != null) {
                UpdateDialog().show(ActivityManager.findHostActivity()?.supportFragmentManager,"update")
            } else {
                showToast("已经是最新版本")
            }
        }

        /* 设置更新状态回调接口 */
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onDownloadCompleted(p0: Boolean) {
                showToast("下载完成")
            }

            override fun onUpgradeSuccess(isManual: Boolean) {
            }

            override fun onUpgradeFailed(isManual: Boolean) {
                showToast("APP更新失败")
            }

            override fun onUpgrading(isManual: Boolean) {
                showToast("正在检查更新..")
            }

            override fun onUpgradeNoVersion(isManual: Boolean) {
                showToast("已经是最新版本")
            }
        }
        Beta.checkUpgrade(true, false)
    }
    fun showToast(message:String){
        Toast.makeText(AppApplication.context,message,Toast.LENGTH_LONG).show()
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

