package com.apm29.yjw.demo.app

import android.content.Context
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class ErrorHandledObserver<T> (private val errorData:MutableLiveData<String>,private val responseErrorHandler: ResponseErrorHandler) : Observer<T> {
    val context: Context = responseErrorHandler.getContext()
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    abstract override fun onNext(t: T)

    override fun onError(e: Throwable) {
        val error = responseErrorHandler.handleResponseError(context, e)
        errorMsg(error)
    }

    protected fun errorMsg(errorMsg:String){
        errorData.value = errorMsg
    }

}