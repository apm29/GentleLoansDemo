package com.apm29.yjw.demo.model

import android.os.Parcel
import android.os.Parcelable
import com.apm29.yjw.demo.arch.user.UserLoginInfo
import com.apm29.yjw.demo.arch.user.UserType
import com.google.gson.Gson
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
open class Event<out T>(private val content: T?) {

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

    fun getContentIfNotHandled(consumer: (content: T?) -> Unit) {
        if (!hasBeenHandled) {
            hasBeenHandled = true
            consumer(content)
        }
    }


    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T? = content


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
            null, 0, null, null, null, null,
            null, null, null, null)
}

open class Assets
data class Estate(
        var owner: String?,
        var area: String?,
        var location: String?,
        var mortgage: Boolean?,
        var mortgageCreditor1: String?,
        var mortgageAmount1: String?,
        var mortgageCreditor2: String?,
        var mortgageAmount2: String?,
        @SerializedName("application_id")  val applicationId:Int?
) : Assets() {

    init {
        if (mortgage == null) {
            mortgage = false
        }
    }

    constructor() : this(
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

data class Car(
        var license: String?,
        var brand: String?,
        var color: String?,
        @SerializedName("application_id")  val applicationId:Int?
) : Assets() {
    constructor() : this(
            null,
            null,
            null,
            null
    )
}

data class AssetsPack(
        val estates:List<Estate>,
        val cars:List<Car>
)


data class LoanLog(
        @SerializedName("total_amount") var totalAmount: String?,
        @SerializedName("interest_rate") var interestRate: String?,
        @SerializedName("term") var term: String?,
        @SerializedName("repayment_type") var repaymentType: Int?,
        @SerializedName("repayment_type_comment") var repaymentTypeComment: String?,
        @SerializedName("actual_time") var actualTime: String?,
        @SerializedName("rest_amount") var restAmount: Double?,
        @SerializedName("total_interest") var totalInterest: Double?,
        @SerializedName("total_fine") var totalFine: Double?,
        @SerializedName("total_overdue") var totalOverdue: Double?,
        @SerializedName("bank_name") var bankName: String?,
        var id: Int?,
        var status: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(totalAmount)
        parcel.writeString(interestRate)
        parcel.writeString(term)
        parcel.writeValue(repaymentType)
        parcel.writeString(repaymentTypeComment)
        parcel.writeString(actualTime)
        parcel.writeValue(restAmount)
        parcel.writeValue(totalInterest)
        parcel.writeValue(totalFine)
        parcel.writeValue(totalOverdue)
        parcel.writeString(bankName)
        parcel.writeValue(id)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanLog> {
        override fun createFromParcel(parcel: Parcel): LoanLog {
            return LoanLog(parcel)
        }

        override fun newArray(size: Int): Array<LoanLog?> {
            return arrayOfNulls(size)
        }
    }


}


data class PersonalInfo(
        @SerializedName("real_name") val name: String?,
        @SerializedName("id_card_no") val idCard: String?,
        @SerializedName("gender") val gender: Int?,
        @SerializedName("marital_status") val maritalStatus: Int?,
        @SerializedName("company_name") val company: String?,
        @SerializedName("department") val department: String?,
        @SerializedName("position_level") val level: String?,
        @SerializedName("staffing") val staff: Int?,
        @SerializedName("year_income") val yearIncome: String?,
        @SerializedName("foundation_month_amount") val gjjMonth: String?,
        @SerializedName("repayment_type") val payType: Int?,
        @SerializedName("term") val term: String?,
        @SerializedName("credit_account") val zxAccount: String?,
        @SerializedName("credit_account_password") val zxPass: String?,
        @SerializedName("credit_account_code") val zxVerify: String?,
        @SerializedName("foundation_account") val gjjAccount: String?,
        @SerializedName("foundation_account_password") val gjjPass: String?,
        @SerializedName("gov_affairs_account") val zwAccount: String?,
        @SerializedName("gov_affairs_account_password") val zwPass: String?,
        @SerializedName("agent_id") val agentId: String?
)

data class ImageUploadResultBean(
        @SerializedName("path") val path: String
)


data class RepaymentSchedule(
        val id: Int?,
        val total_amount: String?,
        val status: String?,
        val type: String?,
        val interest_amount: Double?,
        val overdue_amount: Double?,
        val fine_amount: Double?,
        val expect_time: String?,
        val term: String?
        /**
         * {
         * "total_amount": "2100.00",
         * "status": 0,
         * "type": 1,
         * "overdue_amount": 100,
         * "fine_amount": "0.00",
         * "interest_amount": "100.00",
         * "expect_time": "12月03日",
         * "term": "5\/5期"
         * }
         */
)

data class RepaymentRecord(
        val id: Int?,
        val total_amount: Double?,
        val loans_amount: Double?,
        val overdue_amount: Double?,
        val fine_amount: Double?,
        val interest_amount: Double?,
        val term: String?,
        val actual_time_Ymd: String?,
        val actual_time_His: String?

        /**
         * {
         * "total_amount": "2100.00",
         * "loans_amount": "2000.00",
         * "interest_amount": "100.00",
         * "overdue_amount": 100,
         * "fine_amount": "0.00",
         * "term": "1\/5期",
         * "actual_time_Ymd": "2018.07.05",
         * "actual_time_His": "14:11:59"
         * }
         */
)

data class PushMessage (
    /**
     * {
     * "id": 1,
     * "jump": "cutrecord",
     * "param": "{\"param\":\"278\"}",
     * "content": "您有一笔手续费扣款，点击查看详情",
     * "create_time": "2018-07-25 17:55:21"
     * }
     */
    var id: Int?,
    @SerializedName("create_time")
    var createTime: String?,
    var content: String?,
    var jump: String?,
    var param: String?,
    var isRead:Boolean = false

){
    val paramObj:Param
    init {
        paramObj = Gson().fromJson(param,Param::class.java)
    }
    class Param {
        var param: String? = null
    }
}

