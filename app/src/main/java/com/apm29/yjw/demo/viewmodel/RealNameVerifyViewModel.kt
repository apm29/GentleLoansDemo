package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.api.CommonApi
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.getThreadSchedulers

class RealNameVerifyViewModel : BaseViewModel() {

    val bankCodes: MutableLiveData<BaseBean<Map<String, String>>> = MutableLiveData()

    fun bankCode() {
        mRetrofit.create(CommonApi::class.java)
                .bankCode()
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<Map<String, String>>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<Map<String, String>>) {
                                if (t.success()) {
                                    bankCodes.value = t
                                }
                            }
                        }
                )
    }

    val smsResult: MutableLiveData<BaseBean<*>> = MutableLiveData()

    fun sendBankSms(card: String, id: String, name: String, mobile: String, bankCode: String) {
        mRetrofit.create(UserApi::class.java)
                .smsCode(card, mobile, id, bankCode, name)
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<String>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<String>) {
                                smsResult.value = t
                            }
                        }
                )
    }

    val bindResult: MutableLiveData<BaseBean<*>> = MutableLiveData()

    fun bindCard(sms: String) {
        mRetrofit.create(UserApi::class.java)
                .bindCard(sms)
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<String>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<String>) {
                                if (t.success()) {
                                    bindResult.value = t
                                }
                            }
                        }
                )
    }

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

}
