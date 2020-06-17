package com.logan.android.ui.image.glide

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.image.glide.consts.GlideConsts.*
import com.logan.android.ui.image.glide.ext.GlideApp
import com.logan.android.ui.image.glide.ext.MyGlideUrl
import com.logan.android.ui.image.glide.transform.TransformActivity
import jp.wasabeef.glide.transformations.GrayscaleTransformation

/**
 * desc: glide 案例主页面 <br/>
 * time: 2020/5/20 5:38 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class GlideMainActivity : BaseActivity() {

    companion object {

        // 小质量图片
        val IMAGE_SMALL = URL_IMAGE_MAN_SHOE_75KB_800_800

        // 中等质量图片
        val IMAGE_MIDDLE = URL_IMAGE_SEASCAPE_900KB_2048_1360

        // 大质量图片
        val IMAGE_BIG = URL_IMAGE_DOG_3MB_5295_3355
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_main)

        val imageView = findViewById<ImageView>(R.id.iv_image)
        val buttonsModels = collectButtons(this, imageView)
        val viewContainer = findViewById<FlexboxLayout>(R.id.fl_view_container)

        showButtons(this, viewContainer, *buttonsModels)
    }

    fun collectButtons(context: Context, imageView: ImageView): Array<ButtonModel> {
        return arrayOf(
            // 1
            ButtonModel("Glide入门用法", View.OnClickListener {
                startActivity<GlideSimpleActivity>()
            }),

            // 2
            ButtonModel("Target用法", View.OnClickListener {
                startActivity<GlideTargetActivity>()
            }),

            // 3
            ButtonModel("进入图片列表", View.OnClickListener {
                startActivity<RecyclerViewCaseActivity>()
            }),

            // 4
            ButtonModel("Transform", View.OnClickListener {
                startActivity<TransformActivity>()
            }),

            // 9, Glide OOM
            ButtonModel("Glide OOM", View.OnClickListener {
                // 如果设置 ImageView 的 ScaleType 是 fitxy ，Glide 会默认按照图片实际大小加载。
                // 而scaleType其他模式会按照的 ImageView 的大小显示。

                imageView.scaleType = ImageView.ScaleType.FIT_XY // fitxy 容易OOM
                Glide.with(context).load(IMAGE_BIG).into(imageView)

                // 解决方案：
                // 如果非要设置 fitxy，那么使用 centerCrop() 和 fitCenter() 处理
                // Glide.with(context).load(IMAGE_BIG).centerCrop().into(imageView)
                // Glide.with(context).load(IMAGE_BIG).fitCenter().into(imageView)
            }),

            // 10，配置过渡动画
            ButtonModel("过渡动画", View.OnClickListener {
                val skipMemoryAndDiskCacheOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)

                Glide.with(context)
                    .load(URL_IMAGE_WATCH_172KB_1000_100).apply(skipMemoryAndDiskCacheOptions)
                    .transition(DrawableTransitionOptions.withCrossFade(800)) // 适用于Drawable，过渡动画持续800ms
                    // .transition(BitmapTransitionOptions.withCrossFade(800))// 适用于Bitmap，过渡动画持续800ms
                    // .transition(GenericTransitionOptions.with(animationId)) // 适用于自定义过渡效果，传入animationId
                    .into(imageView)
            }),

            // 12，获取Bitmap
            ButtonModel("通过listener()获取Bitmap", View.OnClickListener {
                // 12.1 通过listener()获取Bitmap
                Glide.with(context)
                    .asBitmap() // 指定格式为Bitmap
                    .load(IMAGE_SMALL)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?, model: Any?,
                            target: Target<Bitmap>?, isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?, model: Any?, target: Target<Bitmap>?,
                            dataSource: DataSource?, isFirstResource: Boolean
                        ): Boolean {
                            // 加载成功，resource 为 bitmap
                            return false // return true，下面的 into(imageView) 不会将图片显示出来
                        }
                    })
                    .into(imageView)
            }),
            ButtonModel("通过SimpleTarget获取Bitmap", View.OnClickListener {
                // 12.2 通过SimpleTarget获取Bitmap
                Glide.with(context)
                    .asBitmap() // 指定格式为Bitmap
                    .load(IMAGE_MIDDLE)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap, transition: Transition<in Bitmap>?
                        ) {
                            //加载成功，resource为加载到的bitmap
                            imageView.setImageBitmap(resource)
                        }
                    })

            }),

            // 14, 3.x版本链式调用
            ButtonModel("3.x版本链式调用", View.OnClickListener {
                // 12.2 通过SimpleTarget获取Bitmap
                GlideApp.with(context)
                    .load(IMAGE_MIDDLE)
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                    .override(300, 300)
                    .transition(DrawableTransitionOptions.withCrossFade(600))
                    .transform(GrayscaleTransformation())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    // .....
                    .into(imageView)
            }),

            // 15 重写GlideUrl
            ButtonModel("过滤url地址的token", View.OnClickListener {
                val optionsError: RequestOptions = RequestOptions()
                    .error(R.drawable.ic_error)

                Glide.with(context)
                    .load(MyGlideUrl("${URL_IMAGE_BG_PINK_172KB_1680_580}?token=${System.currentTimeMillis()}"))
                    .apply(optionsError)
                    .into(imageView)
            })

        )
    }

}
