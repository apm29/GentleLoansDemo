package com.apm29.yjw.demo.model.api

import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.LoginBean
import com.apm29.yjw.demo.model.ProfileBean
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST



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


    /**
     * 12. 发送绑定银行卡短信验证码
     * POST /v1/account/sms_code
     * Request Body
     * {
     * "biz_content": {
     * "bank_card_no": "6227003814170172871",
     * "mobile": "13067733262",
     * "id_card_no": "320321199008231211",
     * "bank_code": "0105",
     * "real_name": "倪大野"
     * },
     * "access_token": "0bf372d479ae201ca547b86f7ed781da"
     * }
     * @return
     */
    @FormUrlEncoded
    @POST("/v1/account/sms_code")
    fun smsCode(
            @Field("bank_card_no") bankCardNo: String,
            @Field("mobile") mobile: String,
            @Field("id_card_no") idCardNo: String,
            @Field("bank_code") bankCode: String,
            @Field("real_name") realName: String
    ): Observable<BaseBean<String>>

    /**
     * 11. 绑定银行卡
     * POST /v1/account/bind_card
     * Request Body
     * {
     * "biz_content": {
     * "code": "000000"
     * },
     * "access_token": "0bf372d479ae201ca547b86f7ed781da"
     * }
     */
    @FormUrlEncoded
    @POST("/v1/account/bind_card")
    fun bindCard(
            @Field("code") code: String
    ): Observable<BaseBean<String>>
}