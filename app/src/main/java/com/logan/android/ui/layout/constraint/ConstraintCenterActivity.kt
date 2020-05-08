package com.logan.android.ui.layout.constraint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint 居中 <br/>
 * time: 2020/5/6 4:11 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintCenterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_center)

        // 1，全局居中。在RelativeLayout中，把控件放在布局中间只要把layout_centerInParent设为true。 在ConstraintLayout中的写法是：
        // app:layout_constraintBottom_toBottomOf="parent"
        // app:layout_constraintLeft_toLeftOf="parent"
        // app:layout_constraintRight_toRightOf="parent"
        // app:layout_constraintTop_toTopOf="parent"

        // 2， 左右居中。RelativeLayout水平居中layout_centerHorizontal=true，相当于在ConstraintLayout约束控件的左右为parent的左右：
        // app:layout_constraintEnd_toEndOf="parent"
        // app:layout_constraintStart_toStartOf="parent"

        // 3， 垂直居中。RelativeLayout垂直居中layout_centerVertical=true，ConstraintLayout约束控件的上下为parent的上下：
        //  app:layout_constraintBottom_toBottomOf="parent"
        //  app:layout_constraintTop_toTopOf="parent"

        // 4，居中的同时使用margin属性，是先margin后，再居中。
        // 譬如：layout_marginLeft="100dp"向右偏移了100dp后，再对控件居中。

        // 5，水平偏移属性：layout_constraintHorizontal_bias
        //    垂直偏移属性：layout_constraintVertical_bias
        //    值范围为 0-1，假如layout_constraintHorizontal_bias为0，则组件在布局的最左侧；为1，则在最右侧；为0.5，则水平居中。
        //   注意：这种偏移，是除去组件的宽度(或高度)后，剩下的距离划分为10等份后，根据设置的比例分配。与Margin属性有区别。

    }

}