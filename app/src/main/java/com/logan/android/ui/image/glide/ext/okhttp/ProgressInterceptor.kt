package com.logan.android.ui.image.glide.ext.okhttp

import okhttp3.Interceptor
import okhttp3.Response

/**
 * desc: 进度条拦截器 <br/>
 * time: 2020/6/17 10:09 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ProgressInterceptor : Interceptor {

    companion object {

        val listenerMap = HashMap<String, ProgressListener>()

        fun addListener(url: String, listener: ProgressListener) {
            listenerMap.put(url, listener)
        }

        fun removeListener(url: String) {
            listenerMap.remove(url)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        response.body()?.let {
            val url = request.url().toString()
            return response.newBuilder()
                .body(ProgressResponseBody(url, it)).build()
        }

        return response
    }

}