package com.apm29.yjw.demo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.about_us_fragment.*

class AboutUsFragment:BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.about_us_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupViews(savedInstanceState: Bundle?) {
        tvVersion.text = "v${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}"
    }
}