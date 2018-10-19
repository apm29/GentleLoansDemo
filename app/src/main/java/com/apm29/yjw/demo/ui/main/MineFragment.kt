package com.apm29.yjw.demo.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.apm29.yjw.demo.app.ActivityManager
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.arch.UserManager
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.defaultAnim
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import com.tencent.bugly.beta.Beta
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

    @SuppressLint("SetTextI18n")
    override fun setupViews(savedInstanceState: Bundle?) {
        //必须是顶层的NaviController
        val findNavController = ActivityManager.findHostActivity()?.findNavController(R.id.app_host_fragment)

        logout.setOnClickListener {
            if (UserManager.logout()) {
                if (findNavController!=null) {
                    UserManager.toLogin(findNavController)
                }
            }
        }

        layoutLoanLog.setOnClickListener {
            navigateErrorHandled(
                    R.id.loanLogListFragment,
                    null,
                    navOptions {
                        anim(defaultAnim)
                    }
            )
        }
        layoutPersonalInfo.setOnClickListener {
            navigateErrorHandled(
                    R.id.personalInfoFragment,
                    null,
                    navOptions {
                        anim(defaultAnim)
                    }
            )
        }

        layoutAboutUs.setOnClickListener {
            navigateErrorHandled(
                    R.id.aboutUsFragment,
                    null,
                    navOptions {
                        anim(defaultAnim)
                    }
            )
        }

        layoutVersion.setOnClickListener {
            Beta.checkUpgrade(true,false)
        }
        //版本号
        tvVersion.text = "${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}"

        hideToolBarArrow()

    }
}