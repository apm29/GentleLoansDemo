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

    val yysResult:MutableLiveData<Event<Boolean>> = MutableLiveData()
    val realResult:MutableLiveData<Event<Boolean>> = MutableLiveData()
}