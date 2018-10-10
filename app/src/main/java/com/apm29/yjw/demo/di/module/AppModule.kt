package com.apm29.yjw.demo.di.module

import android.app.Application
import com.apm29.yjw.demo.app.AppDelegate
import com.apm29.yjw.demo.app.ErrorHandlerImpl
import com.apm29.yjw.demo.app.AppApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * 提供application实例 gson DataSource
 */
@Module
class AppModule(val application: Application) {
    @Singleton
    @Provides
    fun providesApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideGson(application: Application,  configuration: GsonConfiguration): Gson {
        val builder = GsonBuilder()
        configuration.configGson(application, builder)
        return builder.create()
    }

    @Singleton
    @Provides
    fun providesAppDelegate():AppDelegate{
        return (application as AppApplication).mAppDelegate
    }

    @Singleton
    @Provides
    fun provideErrorHandler(application: Application): ErrorHandlerImpl {
        return ErrorHandlerImpl(application)
    }

    interface GsonConfiguration{
        fun configGson(application: Application, builder: GsonBuilder)
    }
}