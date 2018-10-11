package com.apm29.yjw.demo.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment<DefaultFragmentViewModel>() {

    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.main_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        //NavigationUI.setupWithNavController(navigation,findNavController())
        navigation.setupWithNavController(Navigation.findNavController(requireActivity(),R.id.main_host_fragment))
    }

    override fun initData(savedInstanceState: Bundle?) {
    }


}
