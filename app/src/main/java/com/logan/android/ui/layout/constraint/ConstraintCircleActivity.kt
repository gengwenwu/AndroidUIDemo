package com.logan.android.ui.layout.constraint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint 角度定位 <br/>
 * time: 2020/5/6 2:08 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintCircleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_circle)

        // 角度定位：可以用一个角度和一个距离来约束两个组件的中心，使用下面属性：
        // app:layout_constraintCircle="@id/tv_tv1"
        // app:layout_constraintCircleAngle="120" // 从tv_tv1组件正上方顺时针120度
        // app:layout_constraintCircleRadius="100dp" // 距离150dp
    }

}