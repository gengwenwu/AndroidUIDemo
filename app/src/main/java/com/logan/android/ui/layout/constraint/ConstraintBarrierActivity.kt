package com.logan.android.ui.layout.constraint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logan.android.ui.R

/**
 * desc: Constraint Barrier <br/>
 * time: 2020/5/7 2:02 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintBarrierActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_barrier)

        // Barrier可以在多个控件的一侧建立一个屏障 (androidx.constraintlayout.widget.Barrier)，通过下面属性：
        //  (1)，app:barrierDirection：为屏障所在的位置，可设置的值有：bottom、end、left、right、start、top。
        //  (2)，app:constraint_referenced_ids：为屏障引用的控件，可设置多个(用“,”隔开)：

    }

}