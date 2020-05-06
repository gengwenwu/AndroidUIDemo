package com.logan.android.ui.layout.constraint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint 组件尺寸约束 <br/>
 * time: 2020/5/6 2:08 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintSizeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_circle)

        // 控件的尺寸(高宽)可以通过四种不同方式指定：
        // 1，使用指定的尺寸
        //      10dp、100dp等等

        // 2，使用wrap_content。此时可以使用下列属性来控制最大、最小的高度或宽度：
        //      android:minWidth  //最小的宽度
        //      android:minHeight   //最小的高度
        //      android:maxWidth  //最大的宽度
        //      android:maxHeight  //最大的高度

        // 3，使用0dp (替代match_parent)
        //  官方不推荐在ConstraintLayout中使用match_parent，
        //  可以设置 0dp (MATCH_CONSTRAINT) 配合约束代替match_parent。

        // 4，宽高比
        // 当宽或高至少有一个尺寸被设置为0dp时，可以通过属性layout_constraintDimensionRatio设置宽高比。
        //  app:layout_constraintDimensionRatio="2:3" // 默认：宽:高=2:3
        //  app:layout_constraintDimensionRatio="W,2:3"  // 指的是：宽:高=2:3
        //  app:layout_constraintDimensionRatio="H,2:3"  // 指的是：高:宽=2:3

    }

}