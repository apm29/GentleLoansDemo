package com.apm29.yjw.demo.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.model.VerifyProgress
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.RealNameVerifyViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.pre_verify_fragment.*

class PreVerifyFragment: BaseFragment<RealNameVerifyViewModel>() {


    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.pre_verify_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        val verifyProgress:VerifyProgress? = PreVerifyFragmentArgs.fromBundle(arguments).verifyProgress
        showToast(verifyProgress.toString())
        verifyProgress?.let {
            viewPagerVerify.adapter = object : FragmentPagerAdapter(fragmentManager){
                override fun getItem(position: Int): Fragment {
                    val clazz = verifyProgress.getPageType(position)
                    return clazz.newInstance()
                }

                override fun getCount(): Int {
                    return verifyProgress.pageCount
                }

                override fun getPageTitle(position: Int): CharSequence? {
                    return verifyProgress.pageTitles[position]
                }
            }
        }

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}