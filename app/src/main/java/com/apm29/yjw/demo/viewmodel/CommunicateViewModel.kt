package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apm29.yjw.demo.model.Event

class CommunicateViewModel:ViewModel() {
    fun yysAuthSuccess() {
        yysResult.value = Event(true)
    }

    fun realAuthSuccess() {
        realResult.value = Event(true)
    }

    fun onPushClick(json:String?){
        pushJsonData.value = Event(json)
    }

    val yysResult:MutableLiveData<Event<Boolean>> = MutableLiveData()
    val realResult:MutableLiveData<Event<Boolean>> = MutableLiveData()


    val pushJsonData:MutableLiveData<Event<String>> = MutableLiveData()
}