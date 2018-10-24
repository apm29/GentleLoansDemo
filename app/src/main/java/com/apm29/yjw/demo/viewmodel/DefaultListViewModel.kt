package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.*
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.threadAutoSwitch
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlin.collections.ArrayList


class DefaultListViewModel : BaseViewModel() {
    var hasLoadAll = true

    var listChangePositionData: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    var page = 1

    private fun <T> Observable<BaseBean<List<T>>>.loadListInternal(refresh: Boolean, list: ArrayList<T>) {
        this.threadAutoSwitch()
                .subscribe(
                        object : ErrorHandledObserver<BaseBean<List<T>>>(mErrorData, mErrorHandlerImpl, mLoadingData) {
                            override fun onSubscribe(d: Disposable) {
                                super.onSubscribe(d)
                                if (refresh) {
                                    page = 1
                                    val endSize = list.size
                                    list.removeAll(list)
                                    listChangePositionData.value =  endSize to 0
                                } else {
                                    page += 1
                                }
                            }
                            override fun onNext(t: BaseBean<List<T>>) {
                                hasLoadAll = (t.peekData().isEmpty() == true) || t.peekData().size < PAGE_SIZE
                                val startSize = list.size
                                list.addAll(t.peekData())
                                val endSize = list.size

                                listChangePositionData.value = startSize to endSize
                            }
                        }
                )
    }

    fun loadLoanLogs(refresh: Boolean, list: ArrayList<LoanLog> = arrayListOf()) {
        if(refresh){
            page = 1
        }
        getLoanLogObservable()
                .threadAutoSwitch()
                .loadListInternal(refresh,list)

    }

    private fun getLoanLogObservable(): Observable<BaseBean<List<LoanLog>>> {
        return mRetrofit.create(UserApi::class.java)
                .applicationHistory(page)
    }

    private fun getScheduleObservable(id:Int):Observable<BaseBean<List<RepaymentSchedule>>>{
        return mRetrofit.create(UserApi::class.java)
                .paymentPlan( page,id)
    }

    fun loadLoanSchedule(refresh: Boolean, id:Int,lists: ArrayList<RepaymentSchedule>) {
        if(refresh){
            page = 1
        }
        getScheduleObservable(id)
                .threadAutoSwitch()
                .loadListInternal(refresh,lists)
    }

    fun loadRepaymentRecord(refresh: Boolean, id: Int, lists: ArrayList<RepaymentRecord>) {
        if (refresh){
            page =1
        }
        getLoanRepaymentRecordObservable(id)
                .threadAutoSwitch()
                .loadListInternal(refresh,lists)
    }

    private fun getLoanRepaymentRecordObservable(id: Int) = mRetrofit.create(UserApi::class.java)
            .paymentHistory(page, id)
}
