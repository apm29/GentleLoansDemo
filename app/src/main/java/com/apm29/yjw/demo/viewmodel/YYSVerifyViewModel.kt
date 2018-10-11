package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.getThreadSchedulers

class YYSVerifyViewModel :BaseViewModel(){
    var profile: MutableLiveData<BaseBean<ProfileBean>> = MutableLiveData()

    fun profileVerify() {
        mRetrofit.create(UserApi::class.java)
                .profile()
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<ProfileBean>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<ProfileBean>) {
                                profile.value = t
                            }
                        }
                )
    }

    fun mockProfile(debugMock: Boolean) {
        mRetrofit.create(UserApi::class.java)
                .profile()
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<ProfileBean>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<ProfileBean>) {
                                if (debugMock) {
                                    t.peekData().yys_auth = true
                                }
                                profile.value = t
                            }
                        }
                )
    }
}
