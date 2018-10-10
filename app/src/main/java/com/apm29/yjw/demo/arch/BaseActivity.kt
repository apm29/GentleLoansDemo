package com.apm29.yjw.demo.arch

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.app.FragmentLifecycleCallBackAdapter
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.ui.dialog.WarningDialog
import javax.inject.Inject
import androidx.appcompat.app.AppCompatDelegate
import com.apm29.yjw.gentleloansdemo.R


abstract class BaseActivity<VM : ViewModelContract.IViewModel> : AppCompatActivity(), ViewModelContract.IView {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    val tag: String
        get() = this::class.java.simpleName
    lateinit var fragmentLifecycle: FragmentLifecycleCallBackAdapter
    @Inject
    lateinit var mViewModel: VM

    open var observingError: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setupViewLayout(savedInstanceState))
        setupViews(savedInstanceState)
        if (observingError) {
            mViewModel.mErrorData.observe(this, Observer {
                WarningDialog.getInstance(getString(R.string.app_name), it)
                        .show(supportFragmentManager)
            })
        }

    }

    /**
     * called onCreated,before
     * setupViewLayout and setupViews
     */
    override fun buildupComponent(appComponent: AppComponent) {
        setupModel(appComponent)
        if (!::mViewModel.isInitialized) {
            throw IllegalAccessException("mViewModel hasn't been  initialized")
        }
        mViewModel.buildupComponent(appComponent)
        if (!::fragmentLifecycle.isInitialized) {
            fragmentLifecycle = FragmentLifecycleCallBackAdapter(appComponent)
        }
        //注册Fragment生命周期监听
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycle, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        //注销Fragment生命周期监听
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycle)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}