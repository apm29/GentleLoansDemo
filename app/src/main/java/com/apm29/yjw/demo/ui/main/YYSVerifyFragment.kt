package com.apm29.yjw.demo.ui.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.webkit.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.viewmodel.YYSVerifyViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.yys_verify_fragment.*
import java.util.*

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

    override fun setupViews(savedInstanceState: Bundle?) {

    }


    override fun initData(savedInstanceState: Bundle?) {

//        mViewModel.profile.observe(this, Observer {
//            if (it.success() && !it.isDataExpired()) {
//                val profileBean = it.getDataIfNotExpired()
//                webVerify.loadUrl(profileBean?.yys_auth_url)
//                webVerify.settings.javaScriptEnabled = true
//                webVerify.settings.domStorageEnabled = true
//                webVerify.addJavascriptInterface(object : Any() {
//                    @JavascriptInterface
//                    fun closePage() {
//                        handler.post {
//                            findNavController().navigateUp()
//                        }
//                    }
//                }, "junkedai")
//                webVerify.webChromeClient = object : WebChromeClient() {
//                    override fun onReceivedTitle(view: WebView, title: String) {
//                        super.onReceivedTitle(view, title)
////                        setTitle(title)
//                    }
//                }
//                webVerify.webViewClient = object : WebViewClient() {
//                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                        val headers = HashMap<String, String>(1)
//                        headers["Content-Type"] = "Application/Json"
//                        view.loadUrl(url, headers)
//                        println(url)
//                        return true
//                    }
//
//                    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
//                        super.onPageStarted(view, url, favicon)
//                        handler.post { showLoading() }
//                    }
//
//                    override fun onPageFinished(view: WebView, url: String) {
//                        super.onPageFinished(view, url)
//                        handler.post { hideLoading() }
//                    }
//
//                    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
//                        handler.proceed()
//                    }
//
//                }
//                webVerify.loadUrl(profileBean?.yys_auth_url)
//            }
//        })
//
//        mViewModel.profileVerify()
    }

}