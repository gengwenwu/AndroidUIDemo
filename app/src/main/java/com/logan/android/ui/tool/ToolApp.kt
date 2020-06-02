package com.logan.android.ui.tool

import android.os.Looper
import androidx.annotation.MainThread

/**
 * desc: app 工具类 <br/>
 * time: 2020/6/2 3:45 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */

fun isMainThread() =
    Thread.currentThread() === Looper.getMainLooper().thread