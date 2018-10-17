package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.getThreadSchedulers
import com.apm29.yjw.demo.utils.threadAutoSwitch

open class DefaultActivityViewModel : BaseViewModel() {
    var profile: MutableLiveData<ProfileBean> = MutableLiveData()

    fun profileVerify() {
        mRetrofit.create(UserApi::class.java)
                .profile()
                .threadAutoSwitch()
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<ProfileBean>>(mErrorData, mErrorHandlerImpl,mLoadingData) {
                            override fun onNext(t: BaseBean<ProfileBean>) {
                                t.getDataIfNotExpired()?.let {
                                    profile.value = it
                                }
                            }
                        }
                )
    }


}