package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.ApplicantInfo
import com.apm29.yjw.demo.model.Car
import com.apm29.yjw.demo.model.Estate

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

    val estateData: MutableLiveData<List<Estate>> = MutableLiveData()
    val carData: MutableLiveData<List<Car>> = MutableLiveData()

    fun assets() {
        //mock
        val list = arrayListOf<Estate>()
        for (i in 1..3) {
            list.add(Estate())
        }
        estateData.value = list
    }

}
