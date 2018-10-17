package com.apm29.yjw.demo.app

import android.app.Activity
import android.content.Context
import android.net.ParseException
import android.util.Log
import androidx.navigation.findNavController
import com.apm29.yjw.demo.app.exception.UserInfoExpiredException
import com.apm29.yjw.demo.arch.UserManager
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor(private val mContext: Context) : ResponseErrorHandler {
    override fun getContext(): Context {
        return mContext
    }

    override fun handleResponseError(context: Context, t: Throwable): String {
        Log.w("Catch-error", t.message?:"")
        t.printStackTrace()
        //这里不光是只能打印错误,还可以根据不同的错误作出不同的逻辑处理
        var msg = "未知错误"
        if (t is UnknownHostException) {
            msg = "网络不可用"
        } else if (t is SocketTimeoutException) {
            msg = "请求网络超时"
        } else if (t is HttpException) {
            msg = convertStatusCode(t)
        } else if (t is JsonParseException || t is ParseException || t is JSONException) {
            msg = "数据解析错误"
        } else if (t is UserInfoExpiredException) {
            msg = t.message?:msg
            if (context is Activity) {
                UserManager.toLogin(context.findNavController(R.id.app_host_fragment))
            }else if (context is AppApplication){
                val findNavController = ActivityManager.findHostActivity()?.findNavController(R.id.app_host_fragment)
                if (findNavController!=null) {
                    UserManager.toLogin(findNavController)
                }
            }
        } else {
            if (BuildConfig.DEBUG) {
                msg = t.message ?: msg
            }
        }
        return msg
    }

    private fun convertStatusCode(httpException: HttpException): String {
        return when {
            httpException.code() == 500 -> "服务器发生错误"
            httpException.code() == 404 -> "请求地址不存在"
            httpException.code() == 403 -> "请求被服务器拒绝"
            httpException.code() == 307 -> "请求被重定向到其他页面"
            else -> httpException.message()
        }
    }

}