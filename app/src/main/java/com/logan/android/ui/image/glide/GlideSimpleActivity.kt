package com.logan.android.ui.image.glide

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.image.glide.GlideMainActivity.Companion.IMAGE_BIG
import com.logan.android.ui.image.glide.GlideMainActivity.Companion.IMAGE_SMALL
import com.logan.android.ui.image.glide.consts.GlideConsts.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * desc: glide 入门案例 <br/>
 * time: 2020/5/20 5:38 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class GlideSimpleActivity : BaseActivity() {

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

            // ======== 1，最简单用法
            ButtonModel("简单用法", View.OnClickListener {
                Glide.with(context).load(IMAGE_SMALL).into(imageView)
            }),


            // ======== 2, 占位符

            // 2.1 默认占位符 placeholder()。如果加载的图片时间比较长，那么可以设置一个默认的图片。
            ButtonModel("默认占位符", View.OnClickListener {
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                Glide.with(context).load(IMAGE_BIG).apply(options).into(imageView)
            }),

            // 2.2  错误占位符 error()。 如果图片不能成功加载，就显示特定的图片。
            ButtonModel("错误占位符 - 字符串", View.OnClickListener {
                val optionsError: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                Glide.with(context).load(URL_404).apply(optionsError).into(imageView)
            }),

            // 从 Glide 4.3.0 开始，可以使用 error() 指定一个 RequestBuilder，
            ButtonModel("错误占位符 - errorUrl", View.OnClickListener {
                // 在主请求load()失败的时候开始一次新的加载，即下面的 newUrlWhenLoadUrlError 地址
                val newUrlWhenLoadUrlError = URL_IMAGE_BG_PINK_172KB_1680_580
                Glide.with(context)
                    .load(URL_404)
                    .error(Glide.with(context).load(newUrlWhenLoadUrlError)) // 加载RequestBuilder
                    .into(imageView)
            }),

            // 2.3 处理load() url 为 null，可以通过设置 fallback() 来显示 url 为 null 情况下的图片。
            ButtonModel("显示fallback图片", View.OnClickListener {
                val optionsFallback: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                    .fallback(R.drawable.ic_fallback) // 主url=null，显示这个图片

                val nullUrl: String? = null
                Glide.with(context).load(nullUrl).apply(optionsFallback).into(imageView)

                // 上面 2.3 例子 图片显示流程为：
                //      1. 正在加载 url 的时候，显示图片 ic_default。
                //      2. 加载成功显示 url 指向的图片。
                //      3. 加载失败显示 ic_error。
                //      4. 如果 url 为 null（一定得是 null ），则显示图片 ic_fallback
            }),


            // ======== 3, 加载指定大小的图片
            ButtonModel("显示指定尺寸图片", View.OnClickListener {
                // 关于图片尺寸
                // 情况一： 如果加载的图片尺寸是 1080*1920 的，但显示的 ImageView 的大小却是 100 * 100的，
                // 这个时候，不用做任何操作的，因为 Glide 会自动的根据 ImageView 的大小来决定加载图片的大小。

                // 情况二：无视imageView大小，指定图片尺寸。
                // 如：ImageView 是 100*100 的，但要加载的图片大小为 88*88，可以通过override()指定加载的尺寸，
                // 但是这种方式 layout_width、layout_height不能同时制定尺寸，否则不生效。
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(144, 144)  // 指定144*144

                Glide.with(context).load(URL_IMAGE_DUST_DOG_8KB_144_144)
                    .apply(options).into(imageView)

                // 注意的是
                // 虽然设置加载图片的大小，但 placeholder 和 error 的尺寸是不会变的，依旧根据我们的 ImageView 自动计算的。
            }),

            ButtonModel("显示原始尺寸图片", View.OnClickListener {
                // 如果不想让 Glide 帮我们计算并压缩要加载的图片，但是加载原始图片尺寸，也是可以的，如下：
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(Target.SIZE_ORIGINAL) // SIZE_ORIGINAL 指定图片原始尺寸，但是有更高OOM风险

                Glide.with(context).load(IMAGE_BIG).apply(options).into(imageView)
                // 因为OOM风险，所以不建议这样做，因为比如原始图片大小 1024*1024，而 ImageView 是 128*128，
                // 那么两种加载方式占用的内存可能会相差几十倍。
            }),


            // ======== 4，加载不同格式：Gif、Bitmap、Drawable、File
            ButtonModel("加载不同格式", View.OnClickListener {
                // 在 Glide4.0 中有一个 RequestBuilders 的泛型类，用于指定加载资源的格式，可以通过下面四种方法指定。
                // asDrawable()、asGif()、asBitmap()、asFile() 能得到 RequestBuilders 对象。
                // 默认情况下，如果我们不指定，则是得到一个 RequestBuilders 对象 ( load()函数返回值)。

                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(Target.SIZE_ORIGINAL)

                // 加载 gif
                Glide.with(context)
                    .asGif() // 指定格式为gif（如果没有特殊要求，可以不指定，因为glide会自动识别图片格式）
                    .load(URL_GIF_JC_AD_125KB_1680_580).apply(options)
                    .into(imageView)

                // 加载 bitmap
                // Glide.with(context).asBitmap().load(IMAGE_BIG).apply(options).into(imageView)

                // 加载 drawable
                // Glide.with(context).asDrawable().load(IMAGE_BIG).apply(options).into(imageView)

                // 加载 File（见下面的 6.3 例子）
                // Glide.with(context).asFile().load(IMAGE_BIG)
            }),


            // ======== 5、缩略图的使用
            // 加载的图片有高分辨率版本和低分辨率版本，高分辨加载耗时，所以 Glide 提供了 thumbnail() 优先加载缩略图。
            // thumbnail() 方法有两种重载 ：
            //      1， RequestBuilder thumbnailRequest
            //      2， float sizeMultiplier

            // 5.1 RequestBuilder 方式
            ButtonModel("缩略图 - RequestBuilder", View.OnClickListener {
                val highQualityImageUrl = URL_IMAGE_MOUNTAIN_2MB_2048_1367
                val lowQualityImageUrl = URL_IMAGE_WATCH_172KB_1000_100

                // 先加载显示 thumbnail() 方法中的 lowQualityImageUrl 图片，
                // 等到 highQualityImageUrl 图片加载完成之后，再显示 highQualityImageUrl。
                Glide.with(context).load(highQualityImageUrl)
                    .thumbnail(Glide.with(context).load(lowQualityImageUrl))
                    .into(imageView)
            }),

            // 5.2 参数为 float 方式
            ButtonModel("缩略图 - float", View.OnClickListener {
                // 直接显示 highQualityImageUrl 图片的一般分辨率的图片，
                // 等到它完全加载之后，再显示 highQualityImageUrl 指向的完整图片。
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .thumbnail(0.1f) // 传入 0 到 1 之间的 float 值
                    .into(imageView)
            }),

            // ======== 6, 禁用磁盘和内存的缓
            ButtonModel("禁用磁盘和内存的缓存图片", View.OnClickListener {
                val skipMemoryAndDiskCacheOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)

                Glide.with(context).load(IMAGE_BIG)
                    .apply(skipMemoryAndDiskCacheOptions)
                    .into(imageView)
            }),

            // ======== 7，清理缓存和内存
            ButtonModel("清理缓存和内存", View.OnClickListener {
                // 清空内存缓存，要求在主线程中执行
                Glide.get(context).clearMemory()

                // 清空磁盘缓存，要求在后台线程中执行
                Observable.timer(10, TimeUnit.MILLISECONDS).observeOn(Schedulers.io())
                    .doOnNext {
                        Glide.get(context).clearDiskCache()
                    }.subscribe()
            }),

            // ======== 8, 加载优先级
            ButtonModel("加载优先级", View.OnClickListener {
                // 可以对当前加载的图片，调整加载的优先级的。使用 priority()。
                // Priority 的枚举类型值为：LOW（低）、HIGH（高）、NORMAL（普通）、IMMEDIATE（立即）
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .priority(Priority.IMMEDIATE)
                    .into(imageView)
            })

        )
    }

}