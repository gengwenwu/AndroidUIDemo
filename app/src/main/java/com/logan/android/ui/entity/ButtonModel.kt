package com.logan.android.ui.entity

import android.view.View

/**
 * desc: button 案例model <br/>
 * time: 2020/5/21 3:52 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */

/**
 * @param buttonText        button 文案
 * @param onClickListener   button点击事件
 **/
class ButtonModel(
    var buttonText: String,
    var onClickListener: View.OnClickListener
)