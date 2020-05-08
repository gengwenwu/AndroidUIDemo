package com.logan.android.ui.layout.constraint

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint 边距 <br/>
 * time: 2020/5/6 3:37 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintMarginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_margin)

        // 1， ConstraintLayout里面要实现margin，要注意两点：
        //      (1), 必须先约束控件在ConstraintLayout里的位置，否则单独设置margin属性无效。
        //      (2), margin 必须 >= 0
        //      常用的margin属性：
        //          android:layout_marginStart
        //          android:layout_marginEnd
        //          android:layout_marginLeft
        //          android:layout_marginTop
        //          android:layout_marginRight
        //          android:layout_marginBottom


        // 2，goneMargin用于约束的控件可见性设置gone的时候后使用的margin距离。
        // 常见的goneMargin：
        //      android:layout_goneMarginStart
        //      android:layout_goneMarginEnd
        //      android:layout_goneMarginLeft
        //      android:layout_goneMarginTop
        //      android:layout_goneMarginRight
        //      android:layout_goneMarginBottom


        findViewById<Button>(R.id.btn_show).setOnClickListener {
            findViewById<TextView>(R.id.tv_tv1).visibility = View.GONE
        }

    }

}