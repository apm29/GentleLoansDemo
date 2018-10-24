package com.apm29.yjw.demo.di.module

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.apm29.yjw.demo.di.scope.ActivityScope
import com.apm29.yjw.demo.viewmodel.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DefaultFragmentModule(var fragment: Fragment) {

    @ActivityScope
    @Provides
    fun providesMainViewModel(): DefaultFragmentViewModel {
        return ViewModelProviders.of(fragment).get(DefaultFragmentViewModel::class.java)
    }
    @ActivityScope
    @Provides
    fun providesRealNameVerifyViewModel(): RealNameVerifyViewModel {
        return ViewModelProviders.of(fragment).get(RealNameVerifyViewModel::class.java)
    }
    @ActivityScope
    @Provides
    fun providesYYSVerifyViewModel(): YYSVerifyViewModel {
        return ViewModelProviders.of(fragment).get(YYSVerifyViewModel::class.java)
    }
    @ActivityScope
    @Provides
    fun providesRegisterFormViewModel(): RegisterFormViewModel {
        return ViewModelProviders.of(fragment).get(RegisterFormViewModel::class.java)
    }
    @ActivityScope
    @Provides
    fun providesDefaultListViewModel(): DefaultListViewModel {
        return ViewModelProviders.of(fragment).get(DefaultListViewModel::class.java)
    }
    @ActivityScope
    @Provides
    fun providesInformationFormViewModel(): InformationFormViewModel {
        return ViewModelProviders.of(fragment).get(InformationFormViewModel::class.java)
    }
    @ActivityScope
    @Provides
    fun providesLoanDetailViewModel(): LoanDetailViewModel {
        return ViewModelProviders.of(fragment).get(LoanDetailViewModel::class.java)
    }
}