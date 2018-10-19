package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.Car
import com.apm29.yjw.demo.model.Estate
import com.apm29.yjw.demo.utils.subscribeErrorHandled
import com.apm29.yjw.demo.utils.threadAutoSwitch
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class InformationFormViewModel : BaseViewModel() {

    val assetsData: MutableLiveData<Pair<List<Estate>,List<Car>>> = MutableLiveData()

    fun assets() {
        //mock
        Observable.just(1)
                .map {
                    val list = arrayListOf<Estate>()
                    for (i in 1..2) {
                        list.add(Estate())
                    }
                    list
                }
                .delay(2000, TimeUnit.MILLISECONDS)
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData,mErrorHandlerImpl,mLoadingData){
                    assetsData.value = it to arrayListOf()
                }

    }

}



