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
import com.logan.android.ui.image.glide.recycler.RecyclerViewCaseActivity
import jp.wasabeef.glide.transformations.GrayscaleTransformation

/**
 * desc: glide 其它案例 <br/>
 * time: 2020/5/20 5:38 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class GlideOtherDemoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_main)

        val imageView = findViewById<ImageView>(R.id.iv_image)
        val buttonsModels = collectButtons(this, imageView)
        val viewContainer = findViewById<FlexboxLayout>(R.id.fl_view_container)

        showButtons(this, viewContainer, *buttonsModels)
    }

    private fun collectButtons(context: Context, imageView: ImageView): Array<ButtonModel> {
        return arrayOf(
            // ======= 1, Glide OOM
            ButtonModel("FIT_XY - Glide OOM问题", View.OnClickListener {
                // 如果设置 ImageView 的 ScaleType 是 fitxy ，Glide 会默认按照图片实际大小加载。
                // 而scaleType其他模式会按照的 ImageView 的大小显示。

                imageView.scaleType = ImageView.ScaleType.FIT_XY // fitxy 容易OOM
                Glide.with(context).load(IMAGE_BIG).into(imageView)

                // 解决方案：
                // 如果非要设置 fitxy，那么使用 centerCrop() 和 fitCenter() 处理
                // Glide.with(context).load(IMAGE_BIG).centerCrop().into(imageView)
                // Glide.with(context).load(IMAGE_BIG).fitCenter().into(imageView)
            }),

            // ======= 2，获取Bitmap
            // 2.1 通过listener()获取Bitmap
            ButtonModel("获取Bitmap - 通过listener()", View.OnClickListener {
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

            // ======= 2.2 通过SimpleTarget获取Bitmap
            ButtonModel("获取Bitmap - 通过SimpleTarget", View.OnClickListener {
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

            // 3, 3.x版本链式调用
            ButtonModel("3.x版本链式调用", View.OnClickListener {
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

            // 4，过滤掉url指定参数
            ButtonModel("过滤掉url指定参数", View.OnClickListener {
                val optionsError: RequestOptions = RequestOptions()
                    .error(R.drawable.ic_error)

                Glide.with(context)
                    // 关键在于重写GlideUrl
                    .load(MyGlideUrl("${URL_IMAGE_BG_PINK_172KB_1680_580}?token=${System.currentTimeMillis()}"))
                    .apply(optionsError)
                    .into(imageView)
            }),

            // 5，缓解RecyclerView Glide加载卡顿问题
            ButtonModel("缓解RecyclerView Glide加载卡顿问题", View.OnClickListener {
                startActivity<RecyclerViewCaseActivity>()
            })

        )
    }

}
