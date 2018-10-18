package com.apm29.yjw.demo.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.utils.subscribeErrorHandled
import com.apm29.yjw.demo.utils.threadAutoSwitch
import io.reactivex.Observable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import com.google.gson.Gson
import org.json.JSONArray
import com.contrarywind.interfaces.IPickerViewData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class InformationFormViewModel : BaseViewModel() {

    val locationData:MutableLiveData<Triple<List<String>,List<List<String>>,List<List<List<String>>>>> = MutableLiveData()

    fun loadLocationData() {
        var options1Items: ArrayList<String>
        var options2Items: ArrayList<ArrayList<String>>
        var options3Items: ArrayList<ArrayList<ArrayList<String>>>

        Observable.just(1)
                .delay(1000,TimeUnit.MILLISECONDS)
                .map {
                    options1Items = arrayListOf()
                    options2Items = arrayListOf()
                    options3Items = arrayListOf()
                    ActivityManager.findHostActivity()?.let {
                        val json = getJson(it, "province.json")
                        val jsonBean = parseData(json)

                        val size = jsonBean.size
                        for (i in 0 until size) {//遍历省份
                            options1Items.add(jsonBean[i].name?:"")
                            val cityBeanList = jsonBean[i].cityList
                            val cityStringList = arrayListOf<String>()

                            val areaOfCityList = arrayListOf<ArrayList<String>>()
                            for (j in 0 until (cityBeanList?.size?:0)){
                                val cityBean = cityBeanList?.get(j)
                                val cityName = cityBean?.name ?: ""
                                cityStringList.add(cityName)

                                val areaList = arrayListOf<String>()
                                areaList.addAll(cityBean?.area?: arrayListOf())

                                areaOfCityList.add(areaList)
                            }
                            options2Items.add(cityStringList)
                            options3Items.add(areaOfCityList)
                        }
                    }
                    Triple(options1Items,options2Items,options3Items)
                }
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData,mErrorHandlerImpl,mLoadingData){
                    if (it!=null){
                        locationData.value = it
                    }
                }
    }

    fun getJson(context: Context, fileName: String): String {

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

    fun parseData(result: String): ArrayList<JsonBean> {//Gson 解析
        val detail = ArrayList<JsonBean>()
        try {
            val gson = GsonBuilder().enableComplexMapKeySerialization()
                    .create()
            gson.fromJson<ArrayList<JsonBean>>(result,(object :TypeToken<ArrayList<JsonBean>>(){}).type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return detail
    }
}

class JsonBean : IPickerViewData {


    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */

    var name: String? = null
    var cityList: List<CityBean>? = null



    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    override fun getPickerViewText(): String? {
        return this.name
    }


    class CityBean {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */

        var name: String? = null
        var area: List<String>? = null
    }
}
