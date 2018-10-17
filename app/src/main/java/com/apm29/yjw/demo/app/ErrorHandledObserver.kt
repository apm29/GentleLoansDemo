package com.apm29.yjw.demo.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class ErrorHandledObserver<T>(
        private val errorData: MutableLiveData<String>,
        private val responseErrorHandler: ResponseErrorHandler,
        private val mLoadingData: MutableLiveData<Boolean>?
) : Observer<T> {
    val handler =Handler(Looper.getMainLooper())
    val context: Context = responseErrorHandler.getContext()
    override fun onComplete() {
        handler.post {
            mLoadingData?.value = false
        }
    }

    override fun onSubscribe(d: Disposable) {
        handler.post {
            mLoadingData?.value = true
        }
    }

    abstract override fun onNext(t: T)

    override fun onError(e: Throwable) {
        handler.post {
            mLoadingData?.value = false
        }
        val error = responseErrorHandler.handleResponseError(context, e)
        errorMsg(error)
    }

    protected fun errorMsg(errorMsg: String) {
        errorData.value = errorMsg
    }

}