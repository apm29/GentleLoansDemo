package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.Observer
import androidx.test.InstrumentationRegistry
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.google.gson.Gson
import org.junit.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class DefaultViewModelTestUI {

    @Test
    fun profileVerify() {
        val context = InstrumentationRegistry.getContext()
        val observer:Observer<ProfileBean> = Observer {
            println(it)
        }
        val retrofit:Retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()

        val viewModel=DefaultActivityViewModel()
        viewModel.mRetrofit = retrofit
        viewModel.profile.observeForever(observer)
        viewModel.profileVerify()
    }
}