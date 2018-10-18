package com.apm29.yjw.demo.app

import android.content.Context

interface ResponseErrorHandler {
    fun handleResponseError(context: Context?, t: Throwable): String
    fun getContext(): Context
}