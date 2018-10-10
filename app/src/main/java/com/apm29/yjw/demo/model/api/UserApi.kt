package com.apm29.yjw.demo.model.api

import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.LoginBean
import com.apm29.yjw.demo.model.ProfileBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.ArrayList

interface UserApi {
    @POST("/v1/user/profile")
    @Headers("Content-Type: application/json; charset=utf-8")
    fun profile(): Observable<BaseBean<ProfileBean>>

    @FormUrlEncoded
    @POST("/v1/user/send_sms")
    fun sendSMS(
            @Field("mobile") mobile: String
    ): Observable<BaseBean<String>>

    @FormUrlEncoded
    @POST("/v1/user/login")
    fun login(
            @Field("mobile") mobile: String,
            @Field("code") code: String
    ): Observable<BaseBean<LoginBean>>
}