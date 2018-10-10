package com.apm29.yjw.demo.app

import com.apm29.yjw.gentleloansdemo.R
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions

/**
 *@see <a href="https://muyangmin.github.io/glide-docs-cn/doc/generatedapi.html">link</a>
 */
@GlideExtension
class AppGlideExtension private constructor() {
    companion object {
        @JvmStatic
        @GlideOption
        fun defaultPlaceHolder(requestOptions: RequestOptions, height: Int , wide: Int): RequestOptions {
            return requestOptions.placeholder(R.mipmap.error).override(wide,height)
        }
        @JvmStatic
        @GlideOption
        fun defaultPlaceHolder(requestOptions: RequestOptions): RequestOptions {
            return requestOptions.placeholder(R.mipmap.error)
        }

    }
}