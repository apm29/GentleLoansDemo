package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.LoginBean
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.getThreadSchedulers

class DefaultFragmentViewModel:BaseViewModel() {

    var smsResult: MutableLiveData<BaseBean<String>> = MutableLiveData()
    var loginResult: MutableLiveData<BaseBean<LoginBean>> = MutableLiveData()
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
    fun sendLoginVerifySMS(mobile: String) {
        mRetrofit.create(UserApi::class.java)
                .sendSMS(mobile)
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<String>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<String>) {
                                mErrorData.value = t.msg
                                smsResult.value = t
                            }
                        }
                )
    }

    fun doLogin(mobile: String,smsCode:String){
        mRetrofit.create(UserApi::class.java)
                .login(mobile,smsCode)
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<LoginBean>>(mErrorData,mErrorHandlerImpl){
                            override fun onNext(t: BaseBean<LoginBean>) {
                                mErrorData.value = t.msg
                                loginResult.value = t
                            }
                        }
                )
    }

}