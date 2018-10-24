package com.apm29.yjw.demo.di.module

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.apm29.yjw.demo.di.scope.ActivityScope
import com.apm29.yjw.demo.viewmodel.DefaultActivityViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DefaultActivityModule(var activity: FragmentActivity) {
    @ActivityScope
    @Provides
    fun providesDefaultActivityViewModel():DefaultActivityViewModel {
        return ViewModelProviders.of(activity).get(DefaultActivityViewModel::class.java)
    }
}