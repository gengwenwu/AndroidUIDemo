package com.logan.android.ui.layout.constraint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint 角度定位 <br/>
 * time: 2020/5/6 5:33 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintChainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_circle)

        // 如果两个或以上控件通过下面的方式约束在一起，就可以认为是他们是一条链（图为横向的链)
        //   || <-- TextVew1  <--->  TextView2 <--->  TextView3 ||

        // Chain有两个方向横向、竖向。对应约束布局分别是：
        //      (1)，app:layout_constraintHorizontal_chainStyle
        //      (2)，app:layout_constraintVertical_chainStyle
        //
        // 它们分别对应有3个值，分别是：
        // (1)、spread：均分占比(两端不靠边)
        // (2)、spread_inside：均分占比(两端靠边)
        // (3)、packed：所有元素集中靠在一起，两端留空。

    }

}