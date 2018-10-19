package com.apm29.yjw.demo.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import cn.jpush.android.api.JPushInterface
import com.apm29.yjw.demo.arch.BaseActivity
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultActivityComponent
import com.apm29.yjw.demo.di.module.DefaultActivityModule
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.DefaultActivityViewModel
import com.apm29.yjw.gentleloansdemo.R

/**
 *
 */
class HostActivity : BaseActivity<DefaultActivityViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.host_activity
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultActivityComponent.builder()
                .defaultActivityModule(DefaultActivityModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //将绘制内容延伸到状态栏
            window.statusBarColor = resources.getColor(android.R.color.transparent)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.app_host_fragment).navigateUp()||super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras.let {
            val json = it?.getString(JPushInterface.EXTRA_EXTRA)
            val communicateViewModel = ViewModelProviders.of(this).get(CommunicateViewModel::class.java)
            communicateViewModel.onPushClick(json)
        }
    }

}
