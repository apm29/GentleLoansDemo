package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apm29.yjw.demo.model.Event
import com.apm29.yjw.demo.ui.form.information.Photo

class CommunicateViewModel : ViewModel() {
    fun yysAuthSuccess() {
        yysResult.value = Event(true)
    }

    fun realAuthSuccess() {
        realResult.value = Event(true)
    }

    fun onPushClick(json: String?) {
        pushJsonData.value = Event(json)
    }

    fun onImageSaved(photo: Photo?) {
        photo?.let {
            mPhotoData.value = Event(photo)
        }
    }

    val yysResult: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val realResult: MutableLiveData<Event<Boolean>> = MutableLiveData()


    val pushJsonData: MutableLiveData<Event<String>> = MutableLiveData()

    val mPhotoData: MutableLiveData<Event<Photo>> = MutableLiveData()
}