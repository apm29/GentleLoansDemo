package com.apm29.yjw.demo.ui.main

import android.os.Bundle
import androidx.navigation.findNavController
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.arch.UserManager
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.mine_fragment.*

class MineFragment: BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.mine_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        logout.setOnClickListener {
            if (UserManager.logout()) {
                //必须是顶层的NaviController
                val findNavController = ActivityManager.currentActivity?.findNavController(R.id.splash_host_fragment)
                if (findNavController!=null) {
                    UserManager.toLogin(findNavController)
                }

            }
        }
    }
}