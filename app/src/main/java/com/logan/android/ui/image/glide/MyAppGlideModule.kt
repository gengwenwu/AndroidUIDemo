package com.logan.android.ui.image.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.logan.android.ui.tool.log

/**
 * desc: 自定义模块, Glide 4.0 之前版本的用法 <br/>
 * time: 2020/6/2 5:58 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */

@GlideModule
class MyAppGlideModule : AppGlideModule() {

    /**
     * 修改默认配置，如：缓存配置
     **/
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        log("=== MyAppGlideModule -> applyOptions() ")

        // 1，内存缓存配置（不建议配置，Glide 会自动根据手机配置进行分配）

        // 设置内存缓存大小
        builder.setMemoryCache(LruResourceCache(350 * 1024L))
        // 设置Bitmap池大小
        builder.setBitmapPool(LruBitmapPool(350 * 1024L))

        // 2，磁盘缓存配置（默认缓存大小250M，默认保存在内部存储中）
        //  2.1 设置磁盘缓存保存在外部存储，且指定缓存大小
        // TODO Logan  ExternalPreferredCacheDiskCacheFactory 还有哪些？
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context, 300 * 1024L))

        // 2.2 设置磁盘缓存保存在自己指定的目录下，且指定缓存大小
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

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // 替换组件，如网络请求组件
        log("=== MyAppGlideModule -> registerComponents() ")
    }

}