package com.apm29.yjw.demo.app

import com.apm29.yjw.demo.di.component.AppComponent

/**
 * 获取组件
 */
interface App{
    fun getAppComponent(): AppComponent
}