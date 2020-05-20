package com.logan.android.ui

import android.os.Bundle
import android.widget.Button
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.image.glide.GlideMainActivity
import com.logan.android.ui.layout.constraint.ConstraintMainActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 约束布局
        findViewById<Button>(R.id.btn_constraint).setOnClickListener {
            startActivity<ConstraintMainActivity>()
        }

        // glide
        findViewById<Button>(R.id.btn_glide).setOnClickListener {
            startActivity<GlideMainActivity>()
        }
    }

}
