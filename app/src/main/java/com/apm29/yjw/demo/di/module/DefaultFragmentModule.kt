package com.apm29.yjw.demo.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.demo.viewmodel.RealNameVerifyViewModel
import com.apm29.yjw.demo.viewmodel.YYSVerifyViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
class DefaultFragmentModule(var fragment: Fragment) {

    @Provides
    fun providesMainViewModel(): DefaultFragmentViewModel {
        return ViewModelProviders.of(fragment).get(DefaultFragmentViewModel::class.java)
    }

    @Provides
    fun providesRealNameVerifyViewModel(): RealNameVerifyViewModel {
        return ViewModelProviders.of(fragment).get(RealNameVerifyViewModel::class.java)
    }

    @Provides
    fun providesYYSVerifyViewModel(): YYSVerifyViewModel {
        return ViewModelProviders.of(fragment).get(YYSVerifyViewModel::class.java)
    }
}