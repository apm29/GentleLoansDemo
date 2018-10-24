package com.apm29.yjw.demo.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.apm29.yjw.demo.model.Event
import com.apm29.yjw.demo.model.JumpData
import com.apm29.yjw.demo.model.Photo

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

    fun toDestinationId(id:Int,args: Bundle? = null , navOptions: NavOptions? = null,extras: Navigator.Extras?=null){
        mJumpData.value = Event(JumpData(id,args,navOptions,extras))
    }

    val yysResult: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val realResult: MutableLiveData<Event<Boolean>> = MutableLiveData()


    val pushJsonData: MutableLiveData<Event<String>> = MutableLiveData()

    val mPhotoData: MutableLiveData<Event<Photo>> = MutableLiveData()

    val mJumpData:MutableLiveData<Event<JumpData>> = MutableLiveData()
}