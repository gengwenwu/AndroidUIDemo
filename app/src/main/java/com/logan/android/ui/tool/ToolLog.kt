package com.logan.android.ui.tool

import android.util.Log

/**
 * desc: Log 工具 <br/>
 * time: 2020/6/2 3:42 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */


fun log(message: String, tag: String = "MyLog") {
    Log.v(tag, message)
}
