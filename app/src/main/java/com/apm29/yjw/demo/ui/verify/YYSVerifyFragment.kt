package com.apm29.yjw.demo.ui.verify

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.utils.navigateErrorHandled
import com.apm29.yjw.demo.viewmodel.CommunicateViewModel
import com.apm29.yjw.demo.viewmodel.YYSVerifyViewModel
import com.apm29.yjw.gentleloansdemo.BuildConfig
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.yys_verify_fragment.*

class YYSVerifyFragment : BaseFragment<YYSVerifyViewModel>() {


    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.yys_verify_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }
    var debugMock:Boolean = true
    override fun setupViews(savedInstanceState: Bundle?) {
        mViewModel.profileVerify()
        btnVerify.setOnClickListener {
            if (isYYSAuth){
                //next
                val viewModel = ViewModelProviders.of(requireActivity()).get(CommunicateViewModel::class.java)
                viewModel.yysAuthSuccess()
            }else{
                toVerifyWeb()
            }
        }
        if (!BuildConfig.DEBUG){
            btnDebug.visibility  = View.GONE
        }
        btnDebug.setOnClickListener {
            mViewModel.mockProfile(debugMock)
            debugMock = !debugMock
            btnDebug.text = if (debugMock)"DEBUG-跳过验证" else "DEBUG-恢复验证"
        }
    }

    private fun toVerifyWeb() {
        val args = WebViewFragmentArgs.Builder()
                .setUrl(url)
                .build()
                .toBundle()
        navigateErrorHandled(R.id.webViewFragment, args)
    }

    private var isYYSAuth = false
    private var url = ""

    override fun initData(savedInstanceState: Bundle?) {
        mViewModel.profile.observe(this, Observer {
            if (it.success()) {
                isYYSAuth = it.peekData().yys_auth
                url = it.peekData().yys_auth_url?:""

                if (isYYSAuth){
                    //next
                    textHint.text = getString(R.string.hint_yys_auth_pass)
                    ivWarning.setAnimation(R.raw.success)
                    btnVerify.text = "下一步"
                }else{
                    textHint.text = getString(R.string.hint_yys_auth)
                    ivWarning.setAnimation(R.raw.warning)
                    btnVerify.text = "验证"
                }
                ivWarning.playAnimation()
            }
        })
    }

}