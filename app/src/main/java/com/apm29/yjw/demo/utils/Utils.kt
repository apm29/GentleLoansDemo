package com.apm29.yjw.demo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.*
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
import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.text.DecimalFormat


fun Fragment.findHostNavController(): NavController? {
    return ActivityManager.findHostActivity()?.findNavController(R.id.app_host_fragment)
}

fun Activity.findHostNavController(): NavController? {
    return ActivityManager.findHostActivity()?.findNavController(R.id.app_host_fragment)
}

fun Fragment.navigateErrorHandled(
        destination: Int,
        args: Bundle? = null,
        options: NavOptions? = navOptions {
            anim(defaultAnim)
        },
        extra: Navigator.Extras? = null
) {
    try {
        findHostNavController()?.navigate(destination, args, options, extra)
    } catch (e: Exception) {
        CrashReport.postCatchedException(e)
        e.printStackTrace()
    }
}

/**
 * 获取全局组件库
 */
fun Context.getAppComponent(): AppComponent {
    return (this.applicationContext as AppApplication).getAppComponent()
}

/**
 * 线程转换
 */
fun <T> getThreadSchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer {
        return@ObservableTransformer it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

/**
 * 线程切换快捷方法
 */
fun <T> Observable<T>.threadAutoSwitch(): Observable<T> {
    return this.compose(getThreadSchedulers())
}

/**
 * 自动错误处理的订阅方法
 */
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

/*--------------------------------------------------------------------------------*/
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
/*--------------------------------------------------------------------------------*/

fun TextView.getTextOrEmpty(): String {
    return this.text?.toString()?.trim() ?: ""
}

fun TextInputLayout.getTextOrEmpty(): String {
    return this.editText?.text?.toString()?.trim() ?: ""
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

fun View.disableAll() {
    if (this is ViewGroup) {
        this.children.forEach {
            if (it is TextInputLayout) {
                it.disabled()
            }
            it.disableAll()
        }
    } else {
        this.isEnabled = false
    }
}

fun TextInputLayout.setText(text: String?) {
    this.editText?.setText(text)
}

fun TextInputLayout.setText(@StringRes text: Int) {
    this.editText?.setText(text)
}

/*--------------------------------------------------------------------------------*/
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

/*--------------------------------------------------------------------------------*/
val defaultAnim: AnimBuilder.() -> Unit = {
    enter = R.anim.slide_in_right
    exit = R.anim.slide_out_left
    popEnter = R.anim.slide_in_left
    popExit = R.anim.slide_out_right
}
/*--------------------------------------------------------------------------------*/


fun clearAllFocus(itemView: View) {
    if (itemView is ViewGroup) {
        itemView.children.forEach {
            clearAllFocus(it)
        }
    } else {
        itemView.clearFocus()
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

/*--------------------------------------------------------------------------------*/
val mortgageList = arrayListOf("已抵押", "未抵押")
val genderList = arrayListOf("男", "女")
val maritalList = arrayListOf("未婚", "已婚", "离异", "丧偶")
val staffList = arrayListOf("事业", "企业", "公务员")
val payTypeList = arrayListOf( "等额本息","先息后本")

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

/**
 * 从本地读取json来设置LocationPicker
 */
@SuppressLint("SetTextI18n")
fun TextView.setupLocationPickerLocal(
        /**
         * 默认选择项
         */
        defaultSelect: Triple<Int, Int, Int>? = null,
        /**
         * 选择地址回调
         */
        selectedOp: ((Triple<Int, Int, Int>, String, String, View) -> Unit)? = null
) {
    loadLocalPCAData {
        setupLocationPickerInternal(it, selectedOp, defaultSelect)
    }
}

/**
 * 获取数据后设置Picker-location
 */
fun TextView.setupLocationWithData(
        data: List<Province>,
        /**
         * 默认选择项
         */
        defaultSelect: Triple<Int, Int, Int>? = null,
        /**
         * 选择地址回调
         */
        selectedOp: ((Triple<Int, Int, Int>, String, String, View) -> Unit)? = null) {
    locationDataProcess(data) {
        setupLocationPickerInternal(it, selectedOp, defaultSelect)
    }
}

/**
 * 设置PickerOptions，Click Listener等
 */
private fun TextView.setupLocationPickerInternal(
        data: Triple<List<Province>, List<List<CityItem>>, List<List<List<AreaItem>>>>,
        selectedOp: ((Triple<Int, Int, Int>, String, String, View) -> Unit)?,
        defaultSelect: Triple<Int, Int, Int>?
) {
    val (component1, component2, component3) = data
    val pickerOptions = OptionsPickerBuilder(context) { p1, p2, p3, _ ->
        val result = "${component1[p1].pickerViewText} ${component2[p1][p2].pickerViewText} ${component3[p1][p2][p3].pickerViewText}".trim()
        //设置文字
        text = result
        //将code 作为tag设置给textView
        val code = component3[p1][p2][p3].code
        setTag(R.id.TAG_LOCATION_CODE, code)
        //调用CallBack
        selectedOp?.invoke(Triple(p1, p2, p3), code, result, this)
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


/**-------------------------获取三级联动PCA信息---------------------------*/

/**
 * 处理Province列表数据，返回一个PickerOptions 可用的Triple数据
 */
private fun locationDataProcess(provinceList: List<Province>, callBack: ((result: Triple<List<Province>, List<List<CityItem>>, List<List<List<AreaItem>>>>) -> Unit)?) {
    Observable.just(provinceList)
            .map {
                val options1Items: ArrayList<Province> = arrayListOf()
                val options2Items: ArrayList<ArrayList<CityItem>> = arrayListOf()
                val options3Items: ArrayList<ArrayList<ArrayList<AreaItem>>> = arrayListOf()
                options1Items.addAll(provinceList)
                provinceList.forEach { province ->
                    val cityList: ArrayList<CityItem> = province.city ?: arrayListOf()
                    //城市列表为空时加入一个默认数据
                    if (cityList.isEmpty()) {
                        cityList.add(CityItem(area = arrayListOf(), name = "", code = province.code))
                    }
                    options2Items.add(cityList)
                    val secondaryAreaList = arrayListOf<ArrayList<AreaItem>>()
                    cityList.forEach { city ->
                        val areaList = city.area ?: arrayListOf()
                        //区县列表为空时加入一个默认数据
                        if (areaList.isEmpty()) {
                            areaList.add(AreaItem(code = city.code, name = ""))
                        }
                        secondaryAreaList.add(areaList ?: arrayListOf())
                    }

                    options3Items.add(secondaryAreaList)
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


fun loadLocalPCAData(
        callBack: ((result: Triple<List<Province>, List<List<CityItem>>, List<List<List<AreaItem>>>>) -> Unit)? = null
) {
    ActivityManager.findHostActivity()?.let { context ->
        val json = getPCAJson(context)
        val jsonBean = parsePCAData(json)
        locationDataProcess(jsonBean, callBack)
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


/**-------------------------IMAGE---------------------------*/

//把bitmap转换成String
fun bitmap2Base64(filePath: String): String {
    val bm = getSmallBitmap(filePath)
    val outputStream = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    val bytes = outputStream.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

// 根据路径获得图片并压缩，返回bitmap用于显示
fun getSmallBitmap(filePath: String): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(filePath, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, 480, 800)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false

    return BitmapFactory.decodeFile(filePath, options)
}

//计算图片的缩放值
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
        val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
    }
    return inSampleSize
}

//double
val formatter= DecimalFormat("#,##0.00")
fun Double?.toStringDot2F():String{
    if (this == null){
        return formatter.format(0.00f)
    }
    return formatter.format(this)
}
fun Double?.hasNonZeroValue():Boolean{
    return if(this == null){
        false
    }else{
        this > 0
    }
}