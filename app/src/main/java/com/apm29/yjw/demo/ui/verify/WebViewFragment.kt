package com.apm29.yjw.demo.ui.verify

import android.annotation.SuppressLint
import android.os.Bundle
import com.apm29.yjw.demo.arch.BaseFragment
import com.apm29.yjw.demo.di.component.AppComponent
import com.apm29.yjw.demo.di.component.DaggerDefaultFragmentComponent
import com.apm29.yjw.demo.di.module.DefaultFragmentModule
import com.apm29.yjw.demo.viewmodel.DefaultFragmentViewModel
import com.apm29.yjw.gentleloansdemo.R
import kotlinx.android.synthetic.main.webview_fragment.*
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.graphics.Bitmap
import android.os.Handler
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.webkit.JavascriptInterface
import androidx.navigation.fragment.findNavController
import com.apm29.yjw.demo.ui.verify.WebViewFragmentArgs


class WebViewFragment:BaseFragment<DefaultFragmentViewModel>() {
    override fun setupViewLayout(savedInstanceState: Bundle?): Int {
        return R.layout.webview_fragment
    }

    override fun setupModel(appComponent: AppComponent) {
        DaggerDefaultFragmentComponent.builder()
                .defaultFragmentModule(DefaultFragmentModule(this))
                .appComponent(appComponent)
                .build()
                .inject(this)
    }
    val handler = Handler()

    @SuppressLint("SetJavaScriptEnabled")
    override fun setupViews(savedInstanceState: Bundle?) {
        webYYS.settings.javaScriptEnabled = true
        webYYS.settings.domStorageEnabled = true
        webYYS.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            fun closePage() {
                handler.post{
                    findNavController().popBackStack()
                }
            }
        }, "junkedai")
        webYYS.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
            }
        }
        webYYS.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val headers = HashMap<String,String>(1)
                headers.put("Content-Type", "Application/Json")
                view.loadUrl(url, headers)
                println(url)
                return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                handler.post { showLoading() }
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                handler.post  { hideLoading() }
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }

        }
        val url = WebViewFragmentArgs.fromBundle(arguments).url
        webYYS.loadUrl(url)
    }

}