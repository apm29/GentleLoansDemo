package com.apm29.yjw.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.app.ErrorHandledObserver
import com.apm29.yjw.demo.arch.BaseViewModel
import com.apm29.yjw.demo.model.BaseBean
import com.apm29.yjw.demo.model.LoanLog
import com.apm29.yjw.demo.model.api.UserApi
import com.apm29.yjw.demo.utils.threadAutoSwitch
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit


const val PAGE_SIZE = 10

class DefaultListViewModel : BaseViewModel() {
    var hasLoadAll = true

    var listChangePositionData: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    var page = 1

    fun loadLoanLogs(refresh: Boolean, list: ArrayList<LoanLog> = arrayListOf()) {
        loanLog()
                .threadAutoSwitch()
                .loadListInternal(refresh,list)

    }

    private fun mock(): Observable<BaseBean<List<LoanLog>>> {
        return Observable.just(1)
                .delay(2000,TimeUnit.MILLISECONDS)
                .map {
                    val data = arrayListOf<LoanLog>()
//                    val float = Random().nextFloat()
//                    var size = 9
//                    if (float>0.9f){
//                        size = 4
//                    }else if (float>0.95f){
//                        size =0
//                    }
//                    for (i in 0..size) {
//                        data.add(LoanLog())
//                    }
                    return@map BaseBean<List<LoanLog>>(data = data)
                }
    }

    private fun loanLog(): Observable<BaseBean<List<LoanLog>>> {
        return mRetrofit.create(UserApi::class.java)
                .applicationHistory(page)
    }

    protected fun <T> Observable<BaseBean<List<T>>>.loadListInternal(refresh: Boolean,list: ArrayList<T>) {
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
}
