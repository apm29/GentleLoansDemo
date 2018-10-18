package com.apm29.yjw.demo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.AnimBuilder
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.app.AppApplication
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.app.ResponseErrorHandler
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.model.AreaItem
import com.apm29.yjw.demo.model.CityItem
import com.apm29.yjw.demo.model.Province
import com.apm29.yjw.demo.ui.widget.IconFontTextView
import com.apm29.yjw.gentleloansdemo.R
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.contrarywind.interfaces.IPickerViewData
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

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
        errorData: MutableLiveData<String>?,
        responseErrorHandler: ResponseErrorHandler?,
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

fun clearAllFocus(itemView: View) {
    if (itemView is ViewGroup) {
        itemView.children.forEach {
            clearAllFocus(it)
        }
    } else {
        itemView.clearFocus()
    }
}

val mortgageList = arrayListOf("已抵押", "未抵押")
val genderList = arrayListOf("男", "女")
val maritalList = arrayListOf("未婚", "已婚", "离异", "丧偶")
val staffList = arrayListOf("事业", "企业", "公务员")
val payTypeList = arrayListOf("先息后本", "等额本息")

fun TextView.setupOneOptPicker(list: ArrayList<String>, defaultSelection: Int = -1, selectedOp: ((Int, String, View) -> Unit)? = null) {
    val pickerViewOption = OptionsPickerBuilder(context, OnOptionsSelectListener { options1, _, _, _ ->
        val tx = (list[options1])
        this.text = tx
        selectedOp?.invoke(options1, list[options1], this)
    }).build<String>()

    if (defaultSelection >= 0) {
        pickerViewOption.setSelectOptions(defaultSelection)
    }
    pickerViewOption.setPicker(list)
    this.setOnClickListener {
        pickerViewOption.show()
    }
}

@SuppressLint("SetTextI18n")
fun TextView.setupLocationPicker(
        /**
         * 默认选择项
         */
        defaultSelect: Triple<Int, Int, Int>? = null,
        /**
         * 选择地址回调
         */
        selectedOp: ((Triple<Int, Int, Int>, String, String, View) -> Unit)? = null
) {
    loadLocationData {
        setupLocationPickerInternal(it, selectedOp, defaultSelect)
    }
}

private fun TextView.setupLocationPickerInternal(it: Triple<List<Province>, List<List<CityItem>>, List<List<List<AreaItem>>>>, selectedOp: ((Triple<Int, Int, Int>, String, String, View) -> Unit)?, defaultSelect: Triple<Int, Int, Int>?) {
    val (component1, component2, component3) = it
    val pickerOptions = OptionsPickerBuilder(context) { p1, p2, p3, _ ->
        val result = "${component1[p1].pickerViewText} - ${component2[p1][p2].pickerViewText} -  ${component3[p1][p2][p3].pickerViewText}"
        text = result
        selectedOp?.invoke(Triple(p1, p2, p3), component3[p1][p2][p3].code, result, this)
    }.build<IPickerViewData>()
    pickerOptions.setPicker(component1, component2, component3)
    if (defaultSelect != null) {
        pickerOptions.setSelectOptions(defaultSelect.first, defaultSelect.second, defaultSelect.third)
    }
    //户口地址
    this.setOnClickListener { _ ->
        pickerOptions.show()
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

/**-------------------------获取三级联动PCA信息---------------------------*/

fun loadLocationData(
        callBack: ((result: Triple<List<Province>, List<List<CityItem>>, List<List<List<AreaItem>>>>) -> Unit)? = null
) {

    Observable.just(1)
            .map {
                val options1Items: ArrayList<Province> = arrayListOf()
                val options2Items: ArrayList<ArrayList<CityItem>> = arrayListOf()
                val options3Items: ArrayList<ArrayList<ArrayList<AreaItem>>> = arrayListOf()
                ActivityManager.findHostActivity()?.let { context ->
                    val json = getPCAJson(context)
                    val jsonBean = parsePCAData(json)

                    val size = jsonBean.size

                    options1Items.addAll(jsonBean)
                    jsonBean.forEach { province ->
                        val cityList: ArrayList<CityItem> = province.city ?: arrayListOf()
                        options2Items.add(cityList)

                        val secondaryAreaList = arrayListOf<ArrayList<AreaItem>>()
                        cityList.forEach { city ->
                            val areaList = city.area
                            secondaryAreaList.add(areaList ?: arrayListOf())
                        }

                        options3Items.add(secondaryAreaList)
                    }
                }
                Triple(options1Items, options2Items, options3Items)
            }
            .threadAutoSwitch()
            .subscribeErrorHandled(null, null, null) {
                if (it != null) {
                    callBack?.invoke(it)
                }
            }
}

private fun getPCAJson(context: Context, fileName: String = "pca-code.json"): String {

    val stringBuilder = StringBuilder()
    try {
        val assetManager = context.assets
        val bf = BufferedReader(InputStreamReader(
                assetManager.open(fileName)))
        var line: String? = bf.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = bf.readLine()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return stringBuilder.toString()
}

private fun parsePCAData(result: String): ArrayList<Province> {//Gson 解析
    var detail: ArrayList<Province> = ArrayList()
    try {
        val gson = GsonBuilder().enableComplexMapKeySerialization()
                .create()
        detail = gson.fromJson<ArrayList<Province>>(result, object : TypeToken<ArrayList<Province>>() {}.type)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return detail
}
/**-------------------------获取三级联动PCA信息---------------------------*/