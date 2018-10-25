package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.*
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.addDispose
import com.apm29.yjw.demo.utils.subscribeErrorHandled
import com.apm29.yjw.demo.utils.threadAutoSwitch
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class RegisterFormViewModel : BaseViewModel() {


    val applicantData = MutableLiveData<ApplicantInfo>()
    fun applicantInfo() {
        applicantData.value = ApplicantInfo(
                "yjw", "330681199112151718", 1, 1,
                "zjwz", "jishubu", "gcs", 1,
                "12", "100", 1, "12",
                "asasa", "asad", "asasd", "shdjsh",
                "sadasd", "asjhsdja", "asdasda"
        )
    }

    val assetsData: MutableLiveData<Pair<List<Estate>, List<Car>>> = MutableLiveData()

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
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, mLoadingData) {
                    assetsData.value = it to arrayListOf()
                }

    }

    val applicantInfoPostResultData:MutableLiveData<Event<BaseBean<ApplicantInfoUploadResult>>> = MutableLiveData()
    fun postApplicantInfo(personalInfo: PersonalInfo) {
        mRetrofit.create(UserApi::class.java)
                .personalInfo(mGson.toJson(personalInfo))
                .threadAutoSwitch()
                .addDispose(mDisposables)
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, mLoadingData) {
                    applicantInfoPostResultData.value = Event(it)
                }
    }


    val assetsPostResultData: MutableLiveData<Event<BaseBean<Any>>> = MutableLiveData()
    fun postAssetsInfo(estates: List<Estate>, cars: List<Car>) {
        mRetrofit.create(UserApi::class.java)
                .saveAssetsInfo(
                        mGson.toJson(AssetsPack(
                                estates, cars
                        ))
                )
                .addDispose(mDisposables)
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, mLoadingData) {
                    assetsPostResultData.value = Event(it)
                }

    }

}
