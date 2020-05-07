package com.logan.android.ui.layout.constraint

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.constraintlayout.widget.Placeholder
import com.logan.android.ui.R

/**
 * desc: Constraint 其它简单设置 <br/>
 * time: 2020/5/7 2:02 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintOtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_other)

        // 1，Group可以把多个控件归为一组，方便隐藏或显示一组控件(android.support.constraint.Group)。
        //    通过app:constraint_referenced_ids="textView1,textView3"指定多个组件id。
        findViewById<Button>(R.id.btn_show).setOnClickListener {
            with(findViewById<Group>(R.id.group)) {
                if (visibility == View.GONE) {
                    visibility = View.VISIBLE
                } else {
                    visibility = View.GONE
                }
            }
        }

        // 2，Placeholder指的是占位符。
        //    在Placeholder中可使用setContent()设置另一个控件的id，使这个控件移动到占位符的位置。
        findViewById<Button>(R.id.btn_show_4_place_holder).setOnClickListener {
            findViewById<Placeholder>(R.id.place_holder).setContentId(R.id.tv_tv4);
        }

    }

}