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
import com.logan.android.ui.image.glide.GlideConsts.URL_IMAGE_SEASCAPE_900KB_2048_1360
import kotlinx.android.synthetic.main.activity_glide_transform.*

/**
 * desc: glide Transform 案例 <br/>
 * time: 2020/6/15 3:16 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class TransformActivity : BaseActivity() {

    companion object {
        //
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
            // 1，imageView 宽、高 都是 wrap_content，默认Glide会设置成 FitCenter 转换 (将图片按照原始的长宽比充满全屏)
            ButtonModel("默认转换FitCenter", View.OnClickListener {
                Glide.with(context)
                    // 540*258 图片，实际显示效果却充满了全屏。
                    // 因为 ImageView 默认的scaleType是FIT_CENTER，源码into会自动设置FitCenter
                    .load(BAIDU_LOGO)
                    .into(imageView)
            }),

            // 2，dontTransform()
            ButtonModel("dontTransform()", View.OnClickListener {
                Glide.with(context)
                    .load(BAIDU_LOGO)
                    .dontTransform() // 不使用变换，显示效果是图片真实的尺寸？TODO 实际效果并非这样，override(SIZE_ORIGINAL, SIZE_ORIGINAL)才可以。
                    .into(imageView)
            }),

            // 3，override()
            ButtonModel("override(SIZE_ORIGINAL)实际尺寸", View.OnClickListener {
                Glide.with(context)
                    .load(BAIDU_LOGO)
                    // 通过override()方将图片的宽和高，譬如：SIZE_ORIGINAL(原始尺寸)。
                    // 这样其它 Transform 就可以执行，而 dontTransform() 是禁止所有的Transform
                    .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .into(imageView)
            }),

            // 4，Glide 默认提供 centerCrop() 和 fitCenter() 两种转换
            ButtonModel("centerCrop()", View.OnClickListener {
//                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360)
//                    // 如果 ImageView 使用默认的ScaleType，Glide默认就是fitCenter，此时可以不写这句代码。
//                    // FitCenter的效果是: 会将图片按照原始的长宽比充满全屏。
//                    .fitCenter()
//                    .into(imageView)

                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360)
                    // centerCrop效果：原图的中心区域进行裁剪后得到的图片。
                    .centerCrop()
                    .into(imageView)
            }),

            // 5，centerCrop 可以配合override()来实现更加丰富的效果，比如: 指定图片裁剪的比例。
            ButtonModel("centerCrop()指定宽高裁剪", View.OnClickListener {
                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360)
                    .centerCrop()
                    .override(500, 500)
                    .into(imageView)
            })

        )
    }

}