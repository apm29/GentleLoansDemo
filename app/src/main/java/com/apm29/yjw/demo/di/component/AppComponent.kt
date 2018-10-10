package com.apm29.yjw.demo.di.component

import android.app.Application
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.app.AppDelegate
import com.apm29.yjw.demo.di.module.AppModule
import com.apm29.yjw.demo.di.module.ConfigModule
import com.google.gson.Gson
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton


/**
 * 提供一些全局实例
 */
@Singleton
@Component(modules = [AppModule::class,ConfigModule::class])
interface AppComponent{
    fun gson(): Gson
    fun inject(delegate: AppDelegate)
    fun application(): Application
    fun retrofit(): Retrofit
    fun inject(viewModel:BaseViewModel)
    fun inject(baseActivity:Any)
}