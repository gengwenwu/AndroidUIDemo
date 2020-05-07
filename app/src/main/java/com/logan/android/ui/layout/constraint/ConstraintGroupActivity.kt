package com.logan.android.ui.layout.constraint

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.logan.android.ui.R

/**
 * desc: Constraint Group <br/>
 * time: 2020/5/7 2:02 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_group)

        // Group可以把多个控件归为一组，方便隐藏或显示一组控件(android.support.constraint.Group)。
        // 通过app:constraint_referenced_ids="textView1,textView3"指定多个组件id

        findViewById<Button>(R.id.btn_show).setOnClickListener {
            with(findViewById<Group>(R.id.group)) {
                if (this.visibility == View.GONE) {
                    this.visibility = View.VISIBLE
                } else {
                    this.visibility = View.GONE
                }
            }
        }

    }

}