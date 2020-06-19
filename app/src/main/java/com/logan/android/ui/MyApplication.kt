package com.logan.android.ui

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import com.bumptech.glide.Glide

/**
 * desc: Application <br/>
 * time: 2020/6/19 2:11 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 *
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // registerComponentCallbacks(componentCallbacks2)
        // unregisterComponentCallbacks(componentCallbacks2)
    }

    // TrimMemory 和 LowMemory 如何配置？
    /**
     * Android 4.0 引入该API
     *
     * 1，为什么要引入该API？
     *   尽可能的让 App 在系统内，占用足够小的内存资源，就可以降低被杀的概率，从而下次启动的时候走热启动的方式，提升用户的体验。
     *
     * 2，主要作用
     *  是提醒开发者，在系统内存不足的时候，应该通过释放部分不重要的内存资源，从而避免被 Android 系统服务杀掉。<br/>
     *  该函数会被多次调用，传入的level参数可能不一样，表示系统内存紧急状况。 <br/>
     *
     * 3，入参参数
     * @param level  有 7 种类型值，大致可以分为三类：
     *
     * // (1), App 正在前台运行时
     * TRIM_MEMORY_RUNNING_MODERATE // 表示 App 正常运行，并且不会被杀掉，但是目前手机内存已经有点低了，系统可能会根据 LRU List 来开始杀进程。
     * TRIM_MEMORY_RUNNING_LOW // 表示 App正常运行，并且不会被杀掉。但是目前手机内存已经非常低了。
     * TRIM_MEMORY_RUNNING_CRITICAL // 表示 App 正在正常运行，但是系统已经开始根据 LRU List 的缓存规则杀掉了一部分缓存的进程。这个时候应该尽可能的释放掉不需要的内存资源，否者系统可能会继续杀掉其他缓存中的进程。
     *
     * // (2), UI置于后台
     * TRIM_MEMORY_UI_HIDDEN // App 的所有 UI 界面被隐藏，最常见的就是 App 被 home 键或者 back 键，置换到后台了。
     *
     * // (3), App 被置于后台，在 Cached 状态下的回调
     * TRIM_MEMORY_BACKGROUND // 表示 App 退出到后台，并且已经处于 LRU List 比较靠后的位置，暂时前面还有一些其他的 App 进程，暂时不用担心被杀掉。
     * TRIM_MEMORY_MODERATE // 表示 App 退出到后台，并且已经处于 LRU List 中间的位置，如果手机内存仍然不够的话，还是有被杀掉的风险的。
     * TRIM_MEMORY_COMPLETE // 表示 App 退出到后台，并且已经处于 LRU List 比较考靠前的位置，并且手机内存已经极低，随时都有可能被系统杀掉。
     *
     * 4, 可以监听 onTrimMemory() 的组件有：
     *       (1), Application
     *       (2), Activity
     *       (3), Fragment
     *       (4), Service
     *       (5), ContentProvider
     *
     * 5，参考
     *     https://mp.weixin.qq.com/s?__biz=MzIxNjc0ODExMA==&mid=2247484311&idx=1&sn=1fe0416bed4137dd45c6e9c153bb14f4
     **/
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // 1，清除图片缓存
        // 2，多余的 Activity，只保留 Root Activity
    }

    /**
     *
     * 对于低版本的Android 4.0设备，监听此回调。
     * 它大概可以等同于 onTrimMemory(level), level 级别为 TRIM_MEMORY_COMPLETE 的回调。
     **/
    override fun onLowMemory() {
        super.onLowMemory()
    }

    private val componentCallbacks2 = object : ComponentCallbacks2 {
        override fun onLowMemory() {
            // do something¬¬
        }

        override fun onConfigurationChanged(newConfig: Configuration) {

        }

        override fun onTrimMemory(level: Int) {

        }
    }

}