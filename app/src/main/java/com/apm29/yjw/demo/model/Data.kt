package com.apm29.yjw.demo.model

import com.apm29.yjw.demo.arch.UserLoginInfo
import com.apm29.yjw.demo.arch.UserType
import com.google.gson.annotations.SerializedName

data class BaseBean<T>(val code: Int = 200, val msg: String = "", private val data: T) {
    fun success() = code == 200
    fun getDataIfNotExpired(): T? {
        if (expired) {
            return null
        } else {
            expired = true
        }
        return data
    }

    fun peekData(): T {
        return data
    }

    private var expired: Boolean = false
    fun isDataExpired(): Boolean {
        return expired
    }

    fun expireData() {
        expired = true
    }
}

/**
 * Used as a wrapper for getDataIfNotExpired that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun getContentIfNotHandled(consumer: (content: T) -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            consumer(content)
        }
    }


    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content


    private val classesThatHandledTheEvent = HashSet<String>(0)

    /**
     * Returns the content and prevents its use again from the given class.
     */
    fun getContentIfNotHandled(classThatWantToUseEvent: Any): T? {
        val canonicalName = classThatWantToUseEvent::javaClass.get().canonicalName

        canonicalName?.let {
            return if (!classesThatHandledTheEvent.contains(canonicalName)) {
                classesThatHandledTheEvent.add(canonicalName)
                content
            } else {
                null
            }
        } ?: return null
    }
}

data class ProfileBean(

        /**
         * real_name : 姓名啊
         * mobile : 18258105990
         * id_card_no : 340823190912127515
         */

        @SerializedName("real_name")
        var realName: String?,
        @SerializedName("mobile")
        var mobile: String?,
        @SerializedName("id_card_no")
        var idCardNo: String?,
        @SerializedName("bank_name")
        var bankInfo: String?,
        var is_real: Boolean = false,

        /**
         * 客户端 user/profile 接口添加
         * yys_auth: 0 未认证 1 认证
         * yys_auth_url: 认证地址
         */
        @SerializedName("yys_auth")
        var yys_auth: Boolean = false,
        @SerializedName("yys_auth_url")
        var yys_auth_url: String? = null,


        var alipay_status: Boolean = false,
        var taobao_status: Boolean = false
)

data class LoginBean(
        @SerializedName("access_token") override var accessToken: String,
        override var userType: UserType = UserType.Common
) : UserLoginInfo

/**
 * 进件人信息
 */
data class ApplicantInfo(
        var name: String?,
        var idCard: String?,
        var gender: Int,
        var maritalStatus: Int,
        var company: String?,
        var department: String?,
        var level: String?,
        var staff: Int,
        var yearIncome: String?,
        var gjjMonth: String?,
        var payType: Int,
        var term: String?,
        var zxAccount: String?,
        var zxPass: String?,
        var zxVerify: String?,
        var gjjAccount: String?,
        var gjjPass: String?,
        var zwAccount: String?,
        var zwPass: String?
) {
    constructor() : this(null, null, 0, 0,
            null, null, null, 0, null,
            null, 0, null, null, null,null,
            null,null,null,null)
}
open class Assets
data class Estate(
        var owner:String?,
        var area:String?,
        var location:String?,
        var mortgage:Boolean?,
        var mortgageCreditor1:String?,
        var mortgageAmount1:String?,
        var mortgageCreditor2:String?,
        var mortgageAmount2:String?
):Assets(){

    init {
        if (mortgage==null){
            mortgage = false
        }
    }

    constructor():this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )
}

data class Car(
        var license: String?,
        var brand: String?,
        var color: String?
):Assets(){
    constructor():this(
            null,
            null,
            null
    )
}


data class LoanLog(
        @SerializedName("total_amount") var totalAmount:String?,
        @SerializedName("interest_rate")var interestRate:String?,
        @SerializedName("term")var term:String?,
        @SerializedName("repayment_type")var repaymentType:Int?,
        @SerializedName("repayment_type_comment")var repaymentTypeComment:String?,
        @SerializedName("actual_time")var actualTime:String?,
        @SerializedName("rest_amount")var restAmount:Double?,
        @SerializedName("total_interest")var totalInterest:Double?,
        @SerializedName("total_fine")var totalFine:Double?,
        @SerializedName("total_overdue")var totalOverdue:Double?,
        @SerializedName("bank_name")var bankName:String?,
        var id:String?,
        var status:Int?
){
    constructor():this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    )
}


