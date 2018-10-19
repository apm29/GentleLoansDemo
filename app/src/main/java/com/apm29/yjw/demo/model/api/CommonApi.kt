package com.apm29.yjw.demo.model.api

import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.Province
import io.reactivex.Observable
import retrofit2.http.Headers
import retrofit2.http.POST

interface CommonApi {
    @POST("/v1/common/bankcode")
    @Headers("Content-Type: application/json; charset=utf-8")
    fun bankCode(): Observable<BaseBean<Map<String, String>>>


    @POST("v1/common/area")
    @Headers("Content-Type: application/json; charset=utf-8")
    fun locationPCACode(): Observable<BaseBean<List<Province>>>
}