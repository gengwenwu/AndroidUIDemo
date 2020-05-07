package com.logan.android.ui.layout.constraint

import android.os.Bundle
import android.widget.Button
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity

/**
 * desc: Constraint layout 案例主入口 <br/>
 * time: 2020/5/6 11:43 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class ConstraintMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_main)

        findViewById<Button>(R.id.btn_barrier).setOnClickListener {
            startActivity<ConstraintBarrierActivity>()
        }

        findViewById<Button>(R.id.btn_center).setOnClickListener {
            startActivity<ConstraintCenterActivity>()
        }

        findViewById<Button>(R.id.btn_chain).setOnClickListener {
            startActivity<ConstraintChainActivity>()
        }

        findViewById<Button>(R.id.btn_margin).setOnClickListener {
            startActivity<ConstraintMarginActivity>()
        }

        findViewById<Button>(R.id.btn_relative).setOnClickListener {
            startActivity<ConstraintRelativeActivity>()
        }

        findViewById<Button>(R.id.btn_size).setOnClickListener {
            startActivity<ConstraintSizeActivity>()
        }

        findViewById<Button>(R.id.btn_other).setOnClickListener {
            startActivity<ConstraintOtherActivity>()
        }

    }

}