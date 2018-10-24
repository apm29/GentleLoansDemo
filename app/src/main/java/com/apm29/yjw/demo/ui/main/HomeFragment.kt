package com.apm29.yjw.demo.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
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

        ivMessage.setOnClickListener {
            navigateErrorHandled(R.id.pushMessageListFragment)
        }
    }

    override fun setTransitions() {
        setSharedElementTransitions(700)
    }
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val communicateViewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
        mViewModel.profile.observe(this, Observer {
            if (it.success()) {
                val profile = it.peekData()
                if (profile.is_real && profile.yys_auth) {
                    //to apply form
                    //navigateErrorHandled(R.id.action_mainFragment_to_formListFragment)
                    communicateViewModel.toDestinationId(R.id.registerFormFragment)
                } else {
                    val bundle = PreVerifyFragmentArgs.Builder()
                            .setVerifyProgress(VerifyProgress(profile.is_real, profile.yys_auth))
                            .setDestination(FORM)
                            .build()
                            .toBundle()
                    communicateViewModel.toDestinationId(R.id.preVerifyFragment,args = bundle)
                    //navigateErrorHandled(R.id.action_mainFragment_to_preVerifyFragment, bundle)
                }
            }
        })
    }

}