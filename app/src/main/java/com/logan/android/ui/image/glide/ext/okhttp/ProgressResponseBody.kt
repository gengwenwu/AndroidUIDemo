package com.logan.android.ui.image.glide.ext.okhttp

import com.logan.android.ui.tool.log
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * desc: 进度条回调 <br/>
 * time: 2020/6/17 10:14 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ProgressResponseBody(url: String, val responseBody: ResponseBody) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    //  监听器
    private var progressListener: ProgressListener? = null


    init {
        progressListener = ProgressInterceptor.listenerMap[url]
    }


    override fun contentLength() = responseBody.contentLength()

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(ProgressSource(responseBody.source()));
        }

        return bufferedSource!!
    }


    private inner class ProgressSource(source: Source) : ForwardingSource(source) {

        var totalBytesRead: Long = 0
        var currentProcess: Long = 0


        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength = responseBody.contentLength()

            if (bytesRead == -1L) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }


            val progress = (100f * totalBytesRead / fullLength).toLong()
            progressListener?.let {
                if (progress != currentProcess) {
                    it.onProgress(progress.toInt())
                }
            }

            if (totalBytesRead == fullLength) {
                progressListener = null
            }

            currentProcess = progress
            return bytesRead
        }
    }

}