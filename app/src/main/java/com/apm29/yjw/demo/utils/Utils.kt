package com.apm29.yjw.demo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.AnimBuilder
import com.apm29.yjw.demo.app.AppApplication
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.ui.widget.IconFontTextView
import com.apm29.yjw.gentleloansdemo.R
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 获取全局组件库
 */
fun Context.getAppComponent(): AppComponent {
    return (this.applicationContext as AppApplication).getAppComponent()
}

fun <T> getThreadSchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer {
        return@ObservableTransformer it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

var toast: Toast? = null

@SuppressLint("ShowToast")
fun Context.showToast(msg: String) {
    if (toast == null) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    }
    toast.also { it?.setText(msg) }?.show()
}

@SuppressLint("ShowToast")
fun Fragment.showToast(msg: String?) {
    if (toast == null) {
        toast = Toast.makeText(this.context, msg, Toast.LENGTH_LONG)
    }
    toast.also { it?.setText(msg) }?.show()
}

fun TextView.getTextOrEmpty(): String {
    return this.text?.toString()?.trim() ?: ""
}


data class Verify(var error: String, var success: Boolean) {

    companion object {
        fun OK(): Verify {
            return Verify("OK", true)
        }
    }

    fun verifyText(text: TextView): Verify {
        if (!success) {
            return this
        }
        this.error = text.hint.toString()
        this.success = !(text.text?.toString()?.trim().isNullOrEmpty())
        return this
    }
}


val defaultAnim :AnimBuilder.() -> Unit = {
    enter = R.anim.slide_in_right
    exit = R.anim.slide_out_left
    popEnter = R.anim.slide_in_left
    popExit = R.anim.slide_out_right
}

val genderList = arrayListOf("男","女")

fun IconFontTextView.ok(){
    this.text = context.getString(R.string.确定)
}
fun IconFontTextView.error(){
    this.text = context.getString(R.string.取消)
}