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


        // 1，chain
        // 如果两个或以上控件通过下面的方式约束在一起，就可以认为是它是一条链（图为横向的链)
        //   || <-- TextVew1  <--->  TextView2 <--->  TextView3 ||

        // Chain有两个方向横向、竖向。对应约束布局分别是：
        //      (1)，app:layout_constraintHorizontal_chainStyle
        //      (2)，app:layout_constraintVertical_chainStyle
        //
        // 它们分别对应有3个值，分别是：
        // (1)、spread：均分占比(两端不靠边)
        // (2)、spread_inside：均分占比(两端靠边)
        // (3)、packed：所有元素集中靠在一起，两端留空。
        // Chain配合margin使用的时候，先margin，剩下的距离再均分。


        // 2，在设置chain的同时，也可以设置权重，配置如下：
        //      (1)，app:layout_constraintHorizontal_weight // 横向
        //      (2)，app:layout_constraintVertical_weight // 竖向
        // 注意，如果设置上面两个配置，width必须是0dp才有效果。
    }

}