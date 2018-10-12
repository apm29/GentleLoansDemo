package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.ApplicantInfo

class RegisterFormViewModel:BaseViewModel() {


    val applicantData= MutableLiveData<ApplicantInfo>()
    fun applicantInfo() {
        applicantData.value = ApplicantInfo()
    }

}
