package com.logan.android.ui.image.glide.ext

import android.os.Parcel
import android.os.Parcelable
import com.bumptech.glide.load.model.GlideUrl
import com.logan.android.ui.tool.log
import java.net.URL

/**
 * desc: 重写 GlideUrl <br/>
 * time: 2020/6/11 5:42 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class MyGlideUrl(val url: String) : GlideUrl(url) {

    override fun getCacheKey(): String {
        // TODO 该函数多次调用，可以优化。¬
        return url.replace(findTokenParam(), "")
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