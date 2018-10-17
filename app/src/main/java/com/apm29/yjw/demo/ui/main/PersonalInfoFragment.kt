package com.apm29.yjw.demo.ui.main

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.showToast
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.personal_info_fragment.*

class PersonalInfoFragment:BaseFragment<DefaultFragmentViewModel>() {


    var mCount = 0
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.personal_info_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .appComponent(appComponent)
                .defaultFragmentModule(DefaultFragmentModule(this))
                .build()
                .inject(this)
    }

    val cancel = Runnable {
        mCount = 0
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        swipeRefresh.setOnRefreshListener{
            mViewModel.profileVerify()
        }

        layoutName.setOnClickListener {

            if (mCount>3){
                showToast(getString(R.string.text_easter_egg))
            }
            mCount+=1
            mHandler.removeCallbacks(cancel)
            mHandler.postDelayed(cancel,300)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.profileVerify()
        mViewModel.profile.observe(this, Observer {
            val (realName, mobile
                    , idCardNo, bankInfo
                    , _, _
                    , _, _, _
            ) = it.peekData()
            realName?.let {
                name->
                if (name.length>1){
                    val builder = StringBuilder()
                    for (i in 1 until name.length){
                        builder.append("*")
                    }
                    val maskedName = name.replaceRange(1 until name.length, builder.toString())
                    tvName.text = maskedName
                }
            }

            mobile?.let {
                num->
                if (num.length>7){
                    val maskedName = num.replaceRange(3 until 7, "****")
                    tvMoble.text = maskedName
                }
            }

            idCardNo?.let {
                num->
                if (num.length>14){
                    val maskedName = num.replaceRange(4 until 14, "**********")
                    tvId.text = maskedName
                }
            }
            bankInfo?.let {
                bank->
                tvBank.text = bank
            }

        })
    }

    override fun hideLoading() {
        super.hideLoading()
        swipeRefresh.isRefreshing =false
    }

}