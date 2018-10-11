package com.apm29.yjw.demo.di.component

import com.apm29.yjw.demo.di.module.DefaultActivityModule
import com.apm29.yjw.demo.di.scope.ActivityScope
import com.apm29.yjw.demo.ui.splash.LoginFragment
import com.apm29.yjw.demo.ui.HostActivity
import com.apm29.yjw.demo.ui.splash.SplashFragment
import dagger.Component


/**
 * 默认的公用component,
 * 需要注入的类,添加对应的inject方法,注入DefaultModule和APPComponent
 */
@ActivityScope
@Component(
        modules = [DefaultActivityModule::class], dependencies = [AppComponent::class]
)
interface DefaultActivityComponent {
    fun inject(any: HostActivity)
}