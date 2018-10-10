package com.apm29.yjw.demo.arch

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import com.apm29.yjw.demo.di.component.AppComponent
import io.reactivex.disposables.CompositeDisposable

interface ViewModelContract {
    interface IView {
        /**
         * called at Activity's onCreate and Fragment's onAttach
         */
        fun buildupComponent(appComponent: AppComponent)

        @LayoutRes
        fun setupViewLayout(savedInstanceState: Bundle?): Int

        fun setupModel(appComponent: AppComponent)

        fun setupViews(savedInstanceState: Bundle?)

        fun showLoading()
        fun hideLoading()
    }

    interface IViewModel {
        var mDisposables: CompositeDisposable
        var mErrorData: MutableLiveData<String>

        /**
         * called just after #IView.buildComponent(AppComponent)
         */
        fun buildupComponent(appComponent: AppComponent)

    }
}

