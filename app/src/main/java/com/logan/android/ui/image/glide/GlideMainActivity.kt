package com.logan.android.ui.image.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.image.glide.GlideConsts.*
import com.logan.android.ui.image.glide.ext.MyGlideUrl
import com.logan.android.ui.image.glide.target.DownloadImageTarget
import com.logan.android.ui.image.glide.transform.TransformActivity
import com.logan.android.ui.tool.isMainThread
import com.logan.android.ui.tool.log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.TimeUnit

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
            // 1，入门写法
            ButtonModel("简单用法", View.OnClickListener {
                Glide.with(context).load(IMAGE_SMALL).into(imageView)
            }),

            // 2, 占位符
            ButtonModel("默认占位符", View.OnClickListener {
                // 2.1 默认占位符 placeholder()。如果加载的图片时间比较长，那么可以设置一个默认的图片。
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                Glide.with(context).load(IMAGE_BIG).apply(options).into(imageView)
            }),
            ButtonModel("错误占位符", View.OnClickListener {
                // 2.2  错误占位符 error()。 如果图片不能成功加载，就显示特定的图片。
                val optionsError: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                Glide.with(context).load(URL_404).apply(optionsError).into(imageView)
            }),
            ButtonModel("错误占位符(errorUrl)", View.OnClickListener {
                // 从 Glide 4.3.0 开始，可以使用 error() 指定一个 RequestBuilder，
                // 在主请求load()失败的时候开始一次新的加载，即下面的 newUrlWhenLoadUrlError 地址
                val newUrlWhenLoadUrlError = URL_IMAGE_BG_PINK_172KB_1680_580
                Glide.with(context)
                    .load(URL_404)
                    .error(Glide.with(context).load(newUrlWhenLoadUrlError)) // 加载RequestBuilder
                    .into(imageView)
            }),
            ButtonModel("url==null,显示fallback图片", View.OnClickListener {
                // 2.3 处理load() url 为 null，
                //     可以通过设置 fallback() 来显示 url 为 null 情况下的图片。如下面的 R.drawable.ic_fallback
                val optionsFallback: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                    .fallback(R.drawable.ic_fallback)
                    .override(Target.SIZE_ORIGINAL) // 指定大小为图片原始大小，有更高OOM风险

                val nullUrl: String? = null
                Glide.with(context).load(nullUrl).apply(optionsFallback).into(imageView)

                // 上面 2.3 例子 图片显示流程为：
                //      1. 正在加载 url 的时候，显示图片 ic_default。
                //      2. 加载成功显示 url 指向的图片。
                //      3. 加载失败显示 ic_error。
                //      4. 如果 url 为 null（一定得是 null ），则显示图片 ic_fallback
            }),
            // 3, 加载指定大小的图片
            ButtonModel("加载指定大小的图片", View.OnClickListener {
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
            ButtonModel("加载原始图片", View.OnClickListener {
                // 如果不想让 Glide 帮我们计算并压缩要加载的图片，我就要加载原始图片大小，当然也是可以的，可以这样写：
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(Target.SIZE_ORIGINAL) // 指定大小为图片原始大小，有更高OOM风险

                Glide.with(context).load(IMAGE_BIG).apply(options).into(imageView)
                // 不建议这样做，因为比如原始图片大小 1024*1024，而 ImageView 是 128*128，
                // 那么两种加载方式占用的内存可能会相差几十倍。
            }),

            // 4，加载不同格式：Gif、Bitmap、Drawable、File
            ButtonModel("加载不同格式", View.OnClickListener {
                // 在 Glide4.0 中有一个 RequestBuilders 的泛型类，用于指定加载资源的格式，可以通过下面四种方法指定。
                // 默认情况下，如果我们不指定，则是得到一个 RequestBuilders 对象 (load()函数返回值)。
                // asDrawable()、asGif()、asBitmap()、asFile() 也能得到 RequestBuilders 对象。

                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)

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

            // 5、缩略图的使用
            // 加载的图片有高分辨率版本和低分辨率版本，高分辨加载耗时，所以 Glide 提供了 thumbnail() 优先加载缩略图。
            // thumbnail 方法的参数主要有以下两种：
            //      1， RequestBuilder thumbnailRequest
            //      2， float sizeMultiplier
            ButtonModel("缩略图RequestBuilder", View.OnClickListener {
                // 5.1 RequestBuilder 方式
                val highQualityImageUrl = URL_IMAGE_MOUNTAIN_2MB_2048_1367
                val lowQualityImageUrl = URL_IMAGE_WATCH_172KB_1000_100

                // 先加载显示 thumbnail() 方法中的 lowQualityImageUrl 图片，
                // 等到 highQualityImageUrl 图片加载完成之后，再显示 highQualityImageUrl。
                Glide.with(context).load(highQualityImageUrl)
                    .thumbnail(Glide.with(context).load(lowQualityImageUrl))
                    .into(imageView)
            }),
            ButtonModel("缩略图float", View.OnClickListener {
                // 5.2 参数为 float 方式 TODO 效果不明显?
                // 直接显示 highQualityImageUrl 图片的一般分辨率的图片，
                // 等到它完全加载之后，再显示 highQualityImageUrl 指向的完整图片。
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .thumbnail(0.1f) // 传入 0 到 1 之间的 float 值
                    .into(imageView)
            }),

            // 6, Glide 预加载、缓存到硬盘、以及加载监听
            ButtonModel("重载into()显示图片", View.OnClickListener {
                // 6.1 换一种方式加载图片
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable, transition: Transition<in Drawable>?
                        ) {
                            imageView.setImageDrawable(resource)
                        }
                    })
            }),
            ButtonModel("预加载图片", View.OnClickListener {
                // 6.2 预加载图片到缓存 preload()
                // 如果有两个页面A（当前页面） 和 B（跳转页面），在 B 页面中要使用 Glide 显示一个很大的图片，
                // 那么在 A 页面的时候就可以先把 B 页面中要加载的图片缓存下来，等到 B 页面的时候，就可直接读取显示。
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    // 下载监听
                    .listener(object : RequestListener<Drawable> {
                        // onResourceReady() 和 onLoadFailed() 都有一个布尔值的返回值，
                        //  1， 返回 false 就表示这个事件没有被处理，还会继续向下传递，
                        //  2， 返回 true 就表示这个事件已经被处理掉了，不会再继续向下传递。

                        // 举个简单例子：如果在 RequestListener 的 onResourceReady() 中返回了 true，
                        // 那么就不会再回调 Target 的 onResourceReady()，imageView也就不会显示加载到的图片了。
                        override fun onLoadFailed( // 加载失败
                            e: GlideException?, model: Any?,
                            target: Target<Drawable>?, isFirstResource: Boolean
                        ): Boolean {
                            showMsg("加载失败")
                            return false
                        }

                        override fun onResourceReady( // 加载成功
                            resource: Drawable?, model: Any?, target: Target<Drawable>?,
                            dataSource: DataSource?, isFirstResource: Boolean
                        ): Boolean {
                            showMsg("加载成功，可从缓存中既取既得")

                            // 不写下个页面了，就在这里取吧
                            Glide.with(context)
                                .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(imageView);
                            return false
                        }
                    })
                    .preload() // 图片预加载。加载图片的原始尺寸
                // .preload(720, 1280) // 指定加载图片的宽和高
            }),

            ButtonModel("下载图片", View.OnClickListener {
                // 6.3 下载图片到指定地址 asFile()，submit()
                Observable.timer(10, TimeUnit.MICROSECONDS)
                    .subscribeOn(Schedulers.io())
                    .map { createBitmapFromNetImage(context) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it == null) {
                            showMsg("图片下载失败，再尝试一下！")
                        } else {
                            imageView.setImageBitmap(it)
                        }
                    }
            }), ButtonModel("自定义Target", View.OnClickListener {
                // 6.4 下载图片到指定地址 asFile()，submit()
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .downloadOnly(DownloadImageTarget())
            }),

            // 7， 图片变换
            // 在Glide中，Transformations 可以获取资源并修改它，然后返回被修改后的资源。
            // 通常变换操作是用来完成 剪裁 或 对位图应用过滤器，但它也可以用于 转换GIF动画，甚至自定义的资源类型。
            // Glide 内置了几种变换，比如 ：
            //   (1), CenterCrop TODO Logan 几种类型区别， 参考，README.MD "2，Glide各种Transformation案例"
            //   (2), FitCenter
            //   (3), CircleCrop 圆形
            ButtonModel("图片变换(单次)", View.OnClickListener {
                // 7.1 单次变换 circleCrop()
                val options = RequestOptions().circleCrop()
                Glide.with(context).load(IMAGE_SMALL).apply(options).into(imageView)

                // 与上一句效果一样
//                Glide.with(context).load(IMAGE_SMALL)
//                    .apply(RequestOptions.circleCropTransform()).into(imageView)

                // 需要注意，如果下面的写法，是有问题的。
//                Glide.with(context).load(IMAGE_SMALL)
//                    .apply(RequestOptions.centerCropTransform()) // 忽略该效果
//                    .apply(RequestOptions.circleCropTransform()) // 仅执行该效果
//                    .into(imageView)
                // 如果想一次加载中变换多次，那么需要使用 MultiTransformation。
            }),
            ButtonModel("图片变换(多次)", View.OnClickListener {
                // 7.2 多次变换 MultiTransformation
                val multiTransform = MultiTransformation<Bitmap>(CenterCrop(), CircleCrop())
                val options = RequestOptions().transform(multiTransform)
                Glide.with(context).load(IMAGE_SMALL).apply(options).into(imageView)
            }), ButtonModel("wasabeef库的Transformation(单次)", View.OnClickListener {
                // 7.3 使用封装的库单次变换
                //  (1), 圆角变换
                //val options = RequestOptions().transform(RoundedCornersTransformation(10, 5))
                //  (2), 加入模糊变换
                // val options = RequestOptions().transform(BlurTransformation(blurRadius))
                //  (3), 加入灰白变换
                val options = RequestOptions().transform(GrayscaleTransformation())
                //  等等

                Glide.with(context).load(IMAGE_MIDDLE).apply(options).into(imageView)
            }),
            ButtonModel("wasabeef库的Transformation(多次)", View.OnClickListener {
                // 7.4  使用封装的库多次变换
                val transList = listOf(
                    RoundedCornersTransformation(10, 5),
                    BlurTransformation(5),
                    GrayscaleTransformation()
                )
                val options = RequestOptions().transform(MultiTransformation(transList))

                Glide.with(context).load(IMAGE_MIDDLE).apply(options).into(imageView)
            }),
            ButtonModel("禁用图片变换", View.OnClickListener {
                // 7.5  禁用图片变换: dontTransform()，下面的GrayscaleTransformation无效
                val options = RequestOptions().transform(GrayscaleTransformation()).dontTransform()

                Glide.with(context).load(IMAGE_MIDDLE).apply(options).into(imageView)
            }),

            // 8, 禁用磁盘和内存的缓
            ButtonModel("禁用磁盘和内存的缓存图片", View.OnClickListener {
                val skipMemoryAndDiskCacheOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)

                Glide.with(context).load(IMAGE_BIG)
                    .apply(skipMemoryAndDiskCacheOptions)
                    .into(imageView)
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

            // 11，清理缓存和内存
            ButtonModel("清理缓存和内存", View.OnClickListener {
                // 清空内存缓存，要求在主线程中执行
                Glide.get(context).clearMemory()

                // 清空磁盘缓存，要求在后台线程中执行
                Observable.timer(10, TimeUnit.MILLISECONDS).observeOn(Schedulers.io())
                    .doOnNext {
                        Glide.get(context).clearDiskCache()
                    }.subscribe()
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
                            return false
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

            // 13, 加载优先级
            ButtonModel("加载优先级", View.OnClickListener {
                // 可以对当前加载的图片，调整加载的优先级的。使用 priority()。
                // Priority 的枚举类型值为：LOW（低）、HIGH（高）、NORMAL（普通）、IMMEDIATE（立即）
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .priority(Priority.IMMEDIATE)
                    .into(imageView)
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
            }),

            // 16
            ButtonModel("进入图片列表", View.OnClickListener {
                startActivity<RecyclerViewCaseActivity>()
            }),

            // 17
            ButtonModel("Transform", View.OnClickListener {
                startActivity<TransformActivity>()
            })

        )
    }

    @WorkerThread
    private fun createBitmapFromNetImage(context: Context): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            // 也可以先把图片下载到硬盘上，asFile() 得到一个 File文件，这个时候要用到 submit() 下载。
            // submit()必修在子线程中执行
            val target: FutureTarget<File> =
                Glide.with(applicationContext) // 这里不要使用Activity Context，因为Activity销毁了，线程可能未执行完成
                    .asFile()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .submit() // 只会下载原始尺寸图片，并不会加载图片
            // .submit(720, 1280) // 下载指定大小尺寸图片

            val file: File = target.get() // get() 阻塞线程，开始获取网络文件。
            log("图片下载完成，地址:${file.getAbsolutePath()}， 主线程:${isMainThread()}")
            val inputStream = FileInputStream(file)
            bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
        }

        return bitmap
    }

}
