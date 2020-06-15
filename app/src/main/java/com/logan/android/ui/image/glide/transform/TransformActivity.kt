package com.logan.android.ui.image.glide.transform

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import kotlinx.android.synthetic.main.activity_glide_transform.*

/**
 * desc: glide Transform 案例 <br/>
 * time: 2020/6/15 3:16 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class TransformActivity : BaseActivity() {

    companion object {
        val BAIDU_LOGO = "https://www.baidu.com/img/bd_logo1.png"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_transform)

        val buttonsModels = collectButtons(this, iv_image)
        showButtons(this, fl_view_container, *buttonsModels)
    }

    fun collectButtons(context: Context, imageView: ImageView): Array<ButtonModel> {
        return arrayOf(
            // 1，imageView 宽、高 都是 wrap_content，默认设置FitCenter转换
            ButtonModel("默认转换FitCenter", View.OnClickListener {
                Glide.with(context)
                    // 540*258 图片，实际显示效果却充满了全屏。
                    // 因为 ImageView 默认的scaleType是FIT_CENTER，源码into会自动设置FitCenter
                    .load(BAIDU_LOGO).into(imageView)
            }),

            // 2，dontTransform()
            ButtonModel("dontTransform()", View.OnClickListener {
                Glide.with(context)
                    .load(BAIDU_LOGO)
                    .dontTransform() // 不使用变换，显示效果才是真实的尺寸？TODO 效果不佳，override(SIZE_ORIGINAL, SIZE_ORIGINAL)才可以。
                    .into(imageView)
            }),

            // 3，override()
            ButtonModel("override(SIZE_ORIGINAL)实际尺寸", View.OnClickListener {
                Glide.with(context)
                    .load(BAIDU_LOGO)
                    // 通过override()方将图片的宽和高，譬如： SIZE_ORIGINAL(原始尺寸)。
                    // 这样其它 Transform 就可以执行，而 dontTransform() 是禁止所有的Transform
                    .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .into(imageView)
            })

        )
    }

}