package com.apm29.yjw.demo.ui.form.register

import android.os.Bundle
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.viewmodel.RegisterFormViewModel
import com.apm29.yjw.gentleloansdemo.R

class FamilyAssetsFragment :BaseFragment<RegisterFormViewModel>(){
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.family_assets_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {

    }
}