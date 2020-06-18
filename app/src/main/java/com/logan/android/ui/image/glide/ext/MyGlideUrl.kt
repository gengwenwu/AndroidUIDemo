package com.logan.android.ui.image.glide.ext

import com.bumptech.glide.load.model.GlideUrl

/**
 * desc: 重写 GlideUrl <br/>
 * time: 2020/6/11 5:42 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class MyGlideUrl(val url: String) : GlideUrl(url) {

    private val urlNoTokenParameter: String


    init {
        // 过滤token
        urlNoTokenParameter = url.replace(findTokenParam(), "")
    }

    override fun getCacheKey(): String {
        // getCacheKey() 会多次调用，因此urlNoTokenParameter在init()初始化
        return urlNoTokenParameter
    }

    // 解决token一直变化，同一张图片总是请求网络的情况
    private fun findTokenParam(): String {
        var tokenParam = ""

        val tokenKeyIndex =
            if (url.indexOf("?token=") >= 0) url.indexOf("?token=")
            else url.indexOf("&token=")

        if (tokenKeyIndex != -1) {
            val nextAndIndex = url.indexOf("&", tokenKeyIndex + 1)
            if (nextAndIndex != -1) {
                tokenParam = url.substring(tokenKeyIndex + 1, nextAndIndex + 1)
            } else {
                tokenParam = url.substring(tokenKeyIndex)
            }
        }

        return tokenParam;
    }

}