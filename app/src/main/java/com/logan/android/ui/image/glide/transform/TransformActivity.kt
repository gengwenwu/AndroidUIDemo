package com.logan.android.ui.image.glide.transform

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.image.glide.consts.GlideConsts.URL_IMAGE_SEASCAPE_900KB_2048_1360
import com.logan.android.ui.image.glide.transform.custom.CircleCrop
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_glide_transform.*

/**
 * desc: glide Transform 案例 <br/>
 *
 *  在Glide中，Transformations 可以获取资源并修改它，然后返回被修改后的资源。
 *  通常变换操作是用来完成 剪裁、模糊、圆角等操作，也可以用于转换GIF动画，甚至自定义的资源类型。
 *
 *  变换的框架流程：
 *         首先：获取到原始的资源（如：图片）；
 *         然后：对资源进行变换，再将变换完成后的资源返回给Glide；
 *         最终：将资源交给由Glide处理（如：图片显示）。
 *
 * Glide 内置了多种变换，比如 ：
 *   (1), FitCenter // 效果: 会将图片按照原始的长宽比充满全屏。 如果 ImageView 使用默认的ScaleType，Glide默认就是fitCenter。
 *   (2), CircleCrop // 效果：原图的中心区域向外进行裁剪后的图片。
 *   (3), CenterCrop 参考，README.MD "Glide各种Transformation案例"
 *   (4)， 等等
 *
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

            // 4，Glide 默认提供的 centerCrop() 转换
            ButtonModel("centerCrop()", View.OnClickListener {
                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360)
                    .centerCrop() // centerCrop效果：原图的中心区域向外进行裁剪后的图片。
                    .into(imageView)
            }),

            // 5，centerCrop 可以配合override()来实现更加丰富的效果，比如: 指定图片裁剪的比例。
            ButtonModel("centerCrop()指定宽高裁剪", View.OnClickListener {
                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360)
                    .centerCrop()
                    .override(500, 500)
                    .into(imageView)
            }),

            // 6， 自定义Transformation
            ButtonModel("自定义Transformation", View.OnClickListener {
                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360)
                    .apply(RequestOptions().transform(MultiTransformation(CircleCrop())))
                    //.override(700, 700)
                    .into(imageView)
            }),

            // 7， 单次变换 CenterCrop()
            ButtonModel("单次变换", View.OnClickListener {
                val options = RequestOptions().centerCrop()
                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360).apply(options)
                    .into(imageView)

                // 与上一句效果一样
//                Glide.with(context).load(IMAGE_SMALL)
//                    .apply(RequestOptions.centerCropTransform()).into(imageView)

                // 需要注意，如果下面的写法，是有问题的。
//                Glide.with(context).load(IMAGE_SMALL)
//                    .apply(RequestOptions.circleCropTransform()) // 忽略该效果
//                    .apply(RequestOptions.centerCropTransform()) // 仅执行该效果
//                    .into(imageView)
                // 如果想一次加载中变换多次，那么需要使用 MultiTransformation。
            }),

            // 8,多次变换 MultiTransformation
            ButtonModel("多次变换", View.OnClickListener {
                val multiTransform = MultiTransformation(
                    CenterCrop(),
                    com.bumptech.glide.load.resource.bitmap.CircleCrop()
                )

                val options = RequestOptions().transform(multiTransform)
                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360).apply(options)
                    .into(imageView)
            }),

            // 9 使用wasabeef库的转换
            ButtonModel("wasabeef库 - 单次转换", View.OnClickListener {

                //  (1), 圆角变换
                //val options = RequestOptions().transform(RoundedCornersTransformation(10, 5))
                //  (2), 加入模糊变换
                // val options = RequestOptions().transform(BlurTransformation(blurRadius))
                //  (3), 加入灰白变换
                val options = RequestOptions().transform(GrayscaleTransformation())
                //  等等

                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360).apply(options)
                    .into(imageView)
            }),

            // 10，使用封装的库多次变换
            ButtonModel("wasabeef库 - 多次转换", View.OnClickListener {
                val transList = listOf(
                    BlurTransformation(5),
                    GrayscaleTransformation(),
                    RoundedCornersTransformation(150, 200)
                )
                val options = RequestOptions().transform(MultiTransformation(transList))

                Glide.with(context).load(URL_IMAGE_SEASCAPE_900KB_2048_1360).apply(options)
                    .into(imageView)
            })

        )
    }

}