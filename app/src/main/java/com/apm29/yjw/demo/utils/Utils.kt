package com.apm29.yjw.demo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.AnimBuilder
import com.apm29.yjw.demo.app.AppApplication
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.app.ResponseErrorHandler
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.ui.widget.IconFontTextView
import com.apm29.yjw.gentleloansdemo.R
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.google.android.material.textfield.TextInputLayout
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

fun <T> Observable<T>.threadAutoSwitch(): Observable<T> {
    return this.compose(getThreadSchedulers())
}

fun <T> Observable<T>.subscribeErrorHandled(
        errorData: MutableLiveData<String>,
        responseErrorHandler: ResponseErrorHandler,
        mLoadingData: MutableLiveData<Boolean>?,
        onNextBlock: (T) -> Unit
) {
    return this.subscribe(
            object : ErrorHandledObserver<T>(errorData, responseErrorHandler, mLoadingData) {
                override fun onNext(t: T) {
                    onNextBlock(t)
                }
            }
    )
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
    msg?.let {
        if (toast == null) {
            toast = Toast.makeText(this.context, msg, Toast.LENGTH_LONG)
        }
        toast.also { it?.setText(msg) }?.show()
    }
}

fun TextView.getTextOrEmpty(): String {
    return this.text?.toString()?.trim() ?: ""
}


data class Verify(var error: String, var success: Boolean) {

    companion object {
        @JvmStatic
        fun ok(): Verify {
            return Verify("ok", true)
        }
    }

    fun verifyText(text: TextView, validate: (verify: Verify, text: String?, hint: String?) -> Verify = emptyVerify): Verify {
        if (!success) {
            return this
        }
        val content = text.text?.toString()
        val result = validate(this, content, text.hint?.toString())
        this.success = result.success
        this.error = result.error
        return this
    }


    fun verifyPicker(text: TextView, validate: (verify: Verify, text: String?, hint: String?) -> Verify = pickerEmptyVerify): Verify {
        if (!success) {
            return this
        }
        val content = text.text?.toString()
        val result = validate(this, content, text.hint?.toString())
        this.success = result.success
        this.error = result.error
        return this
    }

    fun verifyEdit(inputLayout: TextInputLayout, validate: (verify: Verify, text: String?, hint: String?) -> Verify = emptyVerify): Verify {
        if (!success) {
            return this
        }
        val content = inputLayout.editText?.text?.toString()
        val result = validate(this, content, inputLayout.editText?.hint?.toString())
        this.success = result.success
        this.error = result.error
        return this
    }

    private val emptyVerify: (Verify, String?, String?) -> Verify = { verify, text, hint ->
        if (!verify.success) {
            verify
        } else {
            verify.error = hint ?: "输入项不可为空"
            verify.success = !(text.isNullOrEmpty())
            verify
        }
    }

    private val pickerEmptyVerify: (verify: Verify, text: String?, String?) -> Verify = { verify, text, hint ->
        if (!verify.success) {
            verify
        } else {
            verify.error = hint ?: "选择项不可为空"
            verify.success = (!(text.isNullOrEmpty())) && text != AppApplication.context.getString(R.string.text_unselected)
            verify
        }
    }
}


val defaultAnim: AnimBuilder.() -> Unit = {
    enter = R.anim.slide_in_right
    exit = R.anim.slide_out_left
    popEnter = R.anim.slide_in_left
    popExit = R.anim.slide_out_right
}

fun IconFontTextView.ok() {
    this.text = context.getString(R.string.确定)
}

fun IconFontTextView.error() {
    this.text = context.getString(R.string.取消)
}

fun TextInputLayout.disabled(error: String? = null) {
    this.isEnabled = false
    this.isHintEnabled = false
//    this.editText?.hint= context.getString(R.string.text_uneditable_hint)
    error?.let {
        this.error = error
    }
}

fun TextInputLayout.setText(text: String?) {
    this.editText?.setText(text)
}

fun TextInputLayout.setText(@StringRes text: Int) {
    this.editText?.setText(text)
}


val genderList = arrayListOf("男", "女")
val maritalList = arrayListOf("未婚", "已婚", "离异", "丧偶")
val staffList = arrayListOf("事业", "企业", "公务员")
val payTypeList = arrayListOf("先息后本", "等额本息")

fun TextView.setupPicker(list: ArrayList<String>) {
    val pickerViewOption = OptionsPickerBuilder(context, OnOptionsSelectListener { options1, _, _, _ ->
        val tx = (list[options1])
        this.text = tx
    }).build<String>()

    pickerViewOption.setPicker(list)

    this.setOnClickListener {
        pickerViewOption.show()
    }
}

fun TextView.setTextByIndex(index: Int, list: ArrayList<String>) {
    if (index in 0..(list.size - 1)) {
        this.text = list[index]
    } else {
        this.text = context.getString(R.string.text_unselected)
    }
}

fun TextView.getIndexByText(list: ArrayList<String>): Int {
    return list.indexOf(text.trim())
}
