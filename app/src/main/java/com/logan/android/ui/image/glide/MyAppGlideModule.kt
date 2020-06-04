package com.logan.android.ui.image.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.logan.android.ui.tool.log
import java.io.InputStream

/**
 * desc: 自定义Glide相关配置，在glide首次初始化调用 <br/>
 *
 * 注意：
 * 一个项目（包含主项目与依赖库）中只能存在一个继承AppGlideModule的自定义模块，如果有多个，则会报：
 *  com.android.dex.DexException: Multiple dex files define Lcom/bumptech/glide/GeneratedAppGlideModuleImpl异常。
 *
 *
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