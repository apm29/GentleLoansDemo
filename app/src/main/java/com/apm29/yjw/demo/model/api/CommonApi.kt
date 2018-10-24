package com.apm29.yjw.demo.model.api

import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.Province
import io.reactivex.Observable
import okhttp3.RequestBody
import com.apm29.yjw.demo.model.ImageUploadResultBean
import com.apm29.yjw.demo.model.V1_USER_UPLOAD
import retrofit2.http.*

interface CommonApi {
    @POST("/v1/common/bankcode")
    @Headers("Content-Type: application/json; charset=utf-8")
    fun bankCode(): Observable<BaseBean<Map<String, String>>>


    @POST("v1/common/area")
    @Headers("Content-Type: application/json; charset=utf-8")
    fun locationPCACode(): Observable<BaseBean<List<Province>>>


    @FormUrlEncoded
    @POST(V1_USER_UPLOAD)
    fun upload(
            @Field("image") json: String
    ): Observable<BaseBean<ImageUploadResultBean>>

    @POST(V1_USER_UPLOAD)
    @Headers("Content-Type: text/plain;charset=utf-8")
    fun upload(
            @Body json: RequestBody
    ): Observable<BaseBean<ImageUploadResultBean>>
}