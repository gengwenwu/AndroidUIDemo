package com.logan.android.ui.image.glide.ext.okhttp

import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.logan.android.ui.tool.log
import okhttp3.Call
import okhttp3.OkHttpClient
import java.io.InputStream


/**
 * desc: 尝试写ModelLoader，复制了 OkHttpUrlLoader 代码 <br/>
 * time: 2020/6/16 5:50 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class OkHttpGlideUrlLoader
    (val okHttpClient: Call.Factory?) : ModelLoader<GlideUrl, InputStream> {

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        return LoadData(model, OkHttpStreamFetcher(okHttpClient, model))
    }

    class Factory
    @JvmOverloads constructor(private val client: Call.Factory? = internalClient) :
        ModelLoaderFactory<GlideUrl, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpGlideUrlLoader(
                client
            )
        }

        override fun teardown() {
        }

        companion object {
            @Volatile
            private var internalClient: Call.Factory? = null
                get() {
                    if (field == null) {
                        synchronized(Factory::class.java) {
                            if (field == null) {
                                field = OkHttpClient()
                            }
                        }
                    }
                    return field
                }
        }
    }

    override fun handles(model: GlideUrl) = true

}