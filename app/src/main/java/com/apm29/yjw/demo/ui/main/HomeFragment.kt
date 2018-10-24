package com.apm29.yjw.demo.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.FORM
import com.apm29.yjw.demo.model.VerifyProgress
import com.apm29.yjw.demo.ui.verify.PreVerifyFragmentArgs
import com.apm29.yjw.demo.utils.defaultAnim
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.home_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        animLoading.progress = 1f
        animLoading.setOnClickListener {
            animLoading.playAnimation()
        }

        btnApply.setOnClickListener {
            //profile
            mViewModel.profileVerify()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mViewModel.profile.observe(this, Observer {
            if (it.success()) {
                val profile = it.peekData()
                if (profile.is_real && profile.yys_auth) {
                    //to apply form
                    navigateErrorHandled(R.id.registerFormFragment,null, navOptions {
                        anim(defaultAnim)
                    })
                } else {
                    val bundle = PreVerifyFragmentArgs.Builder()
                            .setVerifyProgress(VerifyProgress(profile.is_real, profile.yys_auth))
                            .setDestination(FORM)
                            .build()
                            .toBundle()
                    navigateErrorHandled(R.id.preVerifyFragment, bundle)
                }
            }
        })
    }

}