package com.logan.android.ui.layout.constraint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint 模拟 相对布局 <br/>
 * time: 2020/5/6 11:43 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintRelativeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_relative)

        // 1，ConstraintLayout相对定位的常用属性：
        // layout_constraintLeft_toLeftOf
        // layout_constraintLeft_toRightOf  // 把自己组件的左边约束到某个指定组件的右边

        // layout_constraintRight_toLeftOf
        // layout_constraintRight_toRightOf

        // layout_constraintTop_toTopOf
        // layout_constraintTop_toBottomOf // 把自己组件的上面约束到某个指定组件的下面

        // layout_constraintBottom_toTopOf
        // layout_constraintBottom_toBottomOf

        // layout_constraintStart_toStartOf
        // layout_constraintStart_toEndOf

        // layout_constraintEnd_toStartOf
        // layout_constraintEnd_toEndOf

        // 2，两个TextView的高度不一致，但是又希望他们的文本对齐，就可以使用该属性
        // layout_constraintBaseline_toBaselineOf
    }

}