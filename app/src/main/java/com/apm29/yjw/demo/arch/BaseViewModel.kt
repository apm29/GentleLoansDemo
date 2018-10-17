package com.apm29.yjw.demo.arch

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apm29.yjw.demo.app.ErrorHandlerImpl
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.model.Event
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import javax.inject.Inject

abstract class BaseViewModel : ViewModel(), ViewModelContract.IViewModel {

    override var mDisposables: CompositeDisposable = CompositeDisposable()
    override var mErrorData: MutableLiveData<String> =MutableLiveData()
    override var mLoadingData: MutableLiveData<Boolean> = MutableLiveData()
    override var mToastData: MutableLiveData<Event<String>> = MutableLiveData()

    protected val tag: String
        get() = this::class.java.simpleName

    init {
        Log.d(tag,"initialized")
    }

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mRetrofit: Retrofit
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mErrorHandlerImpl: ErrorHandlerImpl

    override fun buildupComponent(appComponent: AppComponent) {
        Log.d(tag,"buildupComponent")
        appComponent.inject(this)
    }

    override fun onCleared() {
        Log.d(tag, "cleared")
    }
}