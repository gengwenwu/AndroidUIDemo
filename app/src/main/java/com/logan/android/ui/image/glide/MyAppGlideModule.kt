package com.logan.android.ui.image.glide

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.logan.android.ui.tool.log
import java.io.File
import java.io.InputStream

/**
 * desc: 自定义模块, Glide 4.0 之前版本的用法 <br/>
 * time: 2020/6/2 5:58 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */

@GlideModule
class MyAppGlideModule : AppGlideModule() {

    /**
     * 主要修改Glide默认配置，如：缓存配置
     **/
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        log("=== MyAppGlideModule -> applyOptions() ")

        // 1，内存缓存配置（不建议配置，Glide 会自动根据手机配置进行分配）
        //    1.1 设置内存缓存大小
        // builder.setMemoryCache(LruResourceCache(350 * 1024L))
        //    2.1 设置Bitmap池大小
        // builder.setBitmapPool(LruBitmapPool(350 * 1024L))


        // 2，磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）
        //    2.1 设置磁盘缓存保存在外部存储，且指定缓存大小
        // TODO Logan ExternalPreferredCacheDiskCacheFactory 还有哪些？
        // builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, 300 * 1024L))

        //   2.2 设置磁盘缓存保存在自己指定的目录下，且指定缓存大小
//        builder.setDiskCache(
//            DiskLruCacheFactory(
//                DiskLruCacheFactory.CacheDirectoryGetter {
//                    val diskCacheFolder = File("自定义保存路径")
//                    diskCacheFolder
//                },
//                diskCacheSize
//            )
//        )

    }

    /**
     * 主要用来Glide默认组件。如：网络请求组件
     **/
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        log("=== MyAppGlideModule -> registerComponents() ")
        // 替换 网络请求组件
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())

        // Glide中默认组件, 有很多组件可以替换，但一般没这种需求。
//        registry.register(File::class.java, ParcelFileDescriptor::class.java, FileDescriptorFileLoader.Factory())
//        registry.register(File::class.java, InputStream::class.java, StreamFileLoader.Factory())
//        registry.register(Int::class.java, ParcelFileDescriptor::class.java, FileDescriptorResourceLoader.Factory())
//        registry.register(Int::class.java, InputStream::class.java, StreamResourceLoader.Factory())
//        registry.register(Int::class.java, ParcelFileDescriptor::class.java, FileDescriptorResourceLoader.Factory())
//        registry.register(Int::class.java, InputStream::class.java, StreamResourceLoader.Factory())
//        registry.register(String::class.java, ParcelFileDescriptor::class.java, FileDescriptorStringLoader.Factory())
//        registry.register(String::class.java, InputStream::class.java, StreamStringLoader.Factory())
//        registry.register(Uri::class.java, ParcelFileDescriptor::class.java, FileDescriptorUriLoader.Factory())
//        registry.register(Uri::class.java, InputStream::class.java, StreamUriLoader.Factory())
//        registry.register(URL::class.java, InputStream::class.java, StreamUrlLoader.Factory())
//        registry.register(GlideUrl.class, InputStream::class.java, HttpUrlGlideUrlLoader.Factory())
//        registry.register(Byte[]::class.java, InputStream::class.java, StreamByteArrayLoader.Factory())

    }

}