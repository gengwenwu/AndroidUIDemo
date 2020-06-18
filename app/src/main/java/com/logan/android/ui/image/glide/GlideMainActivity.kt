package com.logan.android.ui.image.glide

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.image.glide.transform.TransformActivity

/**
 * desc: glide 案例主页面 <br/>
 * time: 2020/5/20 5:38 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class GlideMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_main)

        showButtons(
            this,
            findViewById<FlexboxLayout>(R.id.fl_view_container),
            *collectButtons()
        )
    }

    private fun collectButtons(): Array<ButtonModel> {
        return arrayOf(
            ButtonModel("Glide入门用法", View.OnClickListener {
                startActivity<GlideSimpleActivity>()
            }),

            ButtonModel("Target用法", View.OnClickListener {
                startActivity<GlideTargetActivity>()
            }),

            ButtonModel("Transform案例", View.OnClickListener {
                startActivity<TransformActivity>()
            }),

            ButtonModel("其它特殊案例", View.OnClickListener {
                startActivity<GlideOtherDemoActivity>()
            })
        )
    }

}
