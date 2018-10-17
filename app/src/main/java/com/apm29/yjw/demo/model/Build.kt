package com.apm29.yjw.demo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.apm29.yjw.demo.ui.verify.RealNameVerifyFragment
import com.apm29.yjw.demo.ui.verify.YYSVerifyFragment
import com.apm29.yjw.demo.viewmodel.ALIPAY_CHANNEL_CODE
import com.apm29.yjw.demo.viewmodel.TAOBAO_CHANNEL_CODE
import java.lang.IllegalArgumentException

class VerifyProgress(var isReal: Boolean, var isYYS: Boolean) :Parcelable{

    var pageCount:Int
    var pageTitles:ArrayList<String>

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
        pageCount = parcel.readInt()
        parcel.readStringList(pageTitles)
    }


    init {
        var sum = 0
        pageTitles = ArrayList()
        if (!isYYS){
            sum+=1
            pageTitles.add("运营商验证")
        }
        if (!isReal){
            sum+=1
            pageTitles.add("实名认证")
        }
        pageCount = sum
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isReal) 1 else 0)
        parcel.writeByte(if (isYYS) 1 else 0)
        parcel.writeInt(pageCount)
        parcel.writeStringList(pageTitles)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "VerifyProgress(isReal=$isReal, isYYS=$isYYS, pageCount=$pageCount, pageTitles=$pageTitles)"
    }

    fun getPageType(position: Int):Class<out Fragment> {
        return when(pageTitles[position]){
            "运营商验证"-> YYSVerifyFragment::class.java
            "实名认证"-> RealNameVerifyFragment::class.java
            else -> throw IllegalArgumentException("未知的页面类型")
        }
    }

    companion object CREATOR : Parcelable.Creator<VerifyProgress> {
        override fun createFromParcel(parcel: Parcel): VerifyProgress {
            return VerifyProgress(parcel)
        }

        override fun newArray(size: Int): Array<VerifyProgress?> {
            return arrayOfNulls(size)
        }
    }




}

data class DataMagicBox(
        var idCardNo: String, var mobile: String,var realName: String,var channelCode: String
){
    val type:Int = when (channelCode){
        ALIPAY_CHANNEL_CODE ->0
        TAOBAO_CHANNEL_CODE ->1
        else->-1
    }
}