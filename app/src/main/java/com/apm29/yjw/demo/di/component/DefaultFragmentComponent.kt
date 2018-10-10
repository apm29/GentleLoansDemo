package com.apm29.yjw.demo.di.component

import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.di.scope.ActivityScope
import com.apm29.yjw.demo.ui.main.*
import com.apm29.yjw.demo.ui.splash.MainFragment
import com.apm29.yjw.demo.ui.splash.LoginFragment
import dagger.Component


/**
 * 默认的公用component,
 * 需要注入的类,添加对应的inject方法,注入DefaultModule和APPComponent
 */
@ActivityScope
@Component(
        modules = [DefaultFragmentModule::class], dependencies = [AppComponent::class]
)
interface DefaultFragmentComponent {
    fun inject(any: MainFragment)
    fun inject(any: HomeFragment)
    fun inject(any: MineFragment)
    fun inject(any:RealNameVerifyFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(preVerifyFragment: PreVerifyFragment)
    fun inject(yysVerifyFragment: YYSVerifyFragment)
}