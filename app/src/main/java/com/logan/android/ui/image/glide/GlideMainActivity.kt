package com.logan.android.ui.image.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
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
import com.logan.android.ui.tool.dp2px
import com.logan.android.ui.tool.isMainThread
import com.logan.android.ui.tool.log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
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
        val IMAGE_URL_PREFIX =
            "https://raw.githubusercontent.com/gengwenwu/AndroidUIDemo/master/app/src/main/res/drawable"

        val IMAGE_DOG_3MB_5295_3355 = "/res_dog_3mb_5295_3355.jpg"

        // 错误image地址
        val ERROR_URL = "http://tiebapic.baidu.com/09fac7db.jpg"

        //
        val URL_GIF_125_KB_1680_580 =
            "https://imcut.jollychic.com/uploads/jollyimg/imageService/img/bizz/2020/02/01/12/30/fe62f691-d2cb-4ebf-8fac-a4f423acd7ac.gif"

        //
        val URL_IMAGE_172_KB_1000_1000 =
            "https://imcut.jollychic.com//uploads/jollyimg/imageService/img/goods/2019/12/08/13/05/57488a38-b12c-4d83-bff9-1d0e89d17304.jpg"

        //
        val URL_IAMGE_8_KB_144_144 =
            "https://imglf5.nosdn.127.net/img/blJBWWNxcUllaVllbTlLdWF2YWdsQWdrdEFhUFRXZE1hWHphWWdKZm8xd3dwdkN1QkVTQ05nPT0.jpg?imageView&thumbnail=180x180&quality=90&type=jpg"

        //
        val URL_IMAGE_75_KB_800_800 =
            "https://imcut.jollychic.com//uploads/jollyimg/imageService/img/goods/2019/08/21/14/30/b6db0215-5a1d-4c80-974c-9c0d7fb6255c.jpg"

        //
        val URL_IMAGE_900_KB = "https://farm6.staticflickr.com/5344/9109117816_4de9739e71_k.jpg"

        //
        val URL_IMAGE_2_MB =
            "https://images.unsplash.com/photo-1479689836735-bd21a38cbffd?ixlib=rb-0.3.5&q=99&fm=jpg&crop=entropy&cs=tinysrgb&w=2048&fit=max&s=ac300ebd9dd7b3beb51635a80fd4347c"


        // 小质量图片
        val IMAGE_SMALL = URL_IMAGE_75_KB_800_800

        // 中等质量图片
        val IMAGE_MIDDLE = URL_IMAGE_900_KB

        // 大质量图片
        val IMAGE_BIG = IMAGE_URL_PREFIX + IMAGE_DOG_3MB_5295_3355
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_main)

        val imageView = findViewById<ImageView>(R.id.iv_image)
        val buttonsModels = collectionButtons(this, imageView)
        val viewContainer = findViewById<FlexboxLayout>(R.id.fl_view_container)
        createButtons(this, viewContainer, *buttonsModels)
    }

    fun collectionButtons(context: Context, imageView: ImageView): Array<ButtonModel> {
        return arrayOf(
            // 1，入门写法
            ButtonModel("简单用法", View.OnClickListener {
                Glide.with(context).load(IMAGE_SMALL).into(imageView)
            }),

            // 2, 占位符
            ButtonModel("默认占位符", View.OnClickListener {
                // 2.1 默认占位符
                // 如果加载的图片时间比较长，那么可以设置一个默认的图片
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                Glide.with(context).load(IMAGE_BIG).apply(options).into(imageView)
            }),
            ButtonModel("错误占位符", View.OnClickListener {
                // 2.2  错误占位符
                // 如果图片不能成功加载，就显示特定的图片
                val optionsError: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                Glide.with(context).load(ERROR_URL).apply(optionsError).into(imageView)
            }),
            ButtonModel("错误占位符(errorUrl)", View.OnClickListener {
                // 从 Glide 4.3.0 开始，可以使用 error api 来指定一个 RequestBuilder，
                // 在主请求失败的时候开始一次新的加载，即：加载 errorUrl 地址。
                val backupUrlWhenLoadUrlError =
                    "https://imcut.jollychic.com/uploads/jollyimg/imageMaterialLib/201906/28/IL201906281038536038.jpg"

                Glide.with(context)
                    .load(ERROR_URL)
                    .error(Glide.with(context).load(backupUrlWhenLoadUrlError))
                    .into(imageView)
            }),
            ButtonModel("url==null则显示fallback图片", View.OnClickListener {
                // 2.3 load() 方法 url 应该不是 null ，但是如果有可能为 null 的情况，
                //     你可以通过设置 fallback() 方法来显示 url 为 null 的图片，
                val optionsFallback: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                    .fallback(R.drawable.ic_fallback)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)

                val nullUrl: String? = null
                Glide.with(context).load(nullUrl).apply(optionsFallback).into(imageView)

                // 上面 2.3 例子 图片显示流程为：
                //      1. 正在加载 url 的时候，显示图片 ic_default。
                //      2. 加载成功显示 url 指向的图片。
                //      3. 加载失败显示 ic_error。
                //      4. 如果 url 为 null（一定得是 null ），则显示图片 ic_fallback。
            }),
            // 3, 加载指定大小的图片
            ButtonModel("加载指定大小的图片", View.OnClickListener {
                // 关于图片尺寸
                // 情况一： 如果加载的图片尺寸是 1080*1920 的，但显示的 ImageView 的大小却是 100 * 100的，
                // 这个时候，不用做任何操作的，因为 Glide 会自动的根据 ImageView 的大小来决定加载图片的大小。

                // 情况二：如果 ImageView 是 100*100 的，但要加载的图片大小为 88*88，那可以通过override()指定加载的尺寸，
                // 但是这种方式 layout_width、layout_height不能同时制定尺寸，否则不生效：
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(144, 144)

                Glide.with(context).load(URL_IAMGE_8_KB_144_144).apply(options).into(imageView)

                // 注意的是
                // 虽然设置加载图片的大小，但 placeholder 和 error 的尺寸是不会变的，依旧根据我们的 ImageView 自动计算的。
            }),
            ButtonModel("加载原始图片", View.OnClickListener {
                // 如果不想让 Glide 帮我们计算并压缩要加载的图片，我就要加载原始图片大小，当然也是可以的，你可以这样写：
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                // TODO: 2020/5/22 Logan  SIZE_ORIGINAL 内存？还是图片尺寸？原理？好像没生效。

                Glide.with(context).load(IMAGE_BIG).apply(options).into(imageView)
                // 不建议这样做，因为比如原始图片大小 1024*1024，而 ImageView 是 128*128，
                // 那么两种加载方式占用的内存可能会相差几十倍。
            }),

            // 4，加载不同格式：Gif、Bitmap、Drawable、File
            ButtonModel("加载不同格式", View.OnClickListener {
                // 在 Glide4.0 中有一个 RequestBuilders 的泛型类，用于指定加载资源的格式，可以通过下面四种方法指定。
                // 默认情况下，如果我们不指定，则是得到一个 RequestBuilders 对象。
                // 1, asDrawable() 得到 RequestBuilders 对象。
                // 2, asGif() 得到 RequestBuilders 对象。
                // 3, asBitmap() 得到 RequestBuilders 对象。
                // 4, asFile() 得到 RequestBuilders 对象。

                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)

                // 加载gif
                Glide.with(context).asGif().load(URL_GIF_125_KB_1680_580).apply(options)
                    .into(imageView)

                // 加载bitmap
                // Glide.with(context).asBitmap().load(IMAGE_BIG).apply(options).into(imageView)

                // 加载drawable
                // Glide.with(context).asDrawable().load(IMAGE_BIG).apply(options).into(imageView)

                // 加载File（见下面6.3例子）
                // Glide.with(context).asFile().load(IMAGE_BIG)
            }),

            // 5、缩略图的使用
            // 加载的图片有高分辨率版本和低分辨率版本，高分辨加载耗时，所以 Glide 提供了 thumbnail() 优先加载缩略图。
            // thumbnail 方法的参数主要有以下两种：
            //      1， RequestBuilder thumbnailRequest
            //      2， float sizeMultiplier
            ButtonModel("缩略图RequestBuilder", View.OnClickListener {
                // 5.1 RequestBuilder 方式
                val highQualityImageUrl = URL_IMAGE_2_MB
                val lowQualityImageUrl = URL_IMAGE_172_KB_1000_1000

                // 先加载并显示 lowQualityImageUrl 图片，等到 highQualityImageUrl 图片加载完成之后，则显示 highQualityImageUrl 图片。
                Glide.with(context)
                    .load(highQualityImageUrl)
                    .thumbnail(Glide.with(context).load(lowQualityImageUrl))
                    .into(imageView)
            }),
            ButtonModel("缩略图float", View.OnClickListener {
                // 5.2 参数为 float 方式 TODO 效果不明显
                // 直接显示 highQualityImageUrl 图片的一般分辨率的图片，等到 highQualityImageUrl 图片完全加载之后，
                // 再显示 highQualityImageUrl 指向的完整图片。
                Glide.with(context)
                    .load(URL_IMAGE_2_MB)
                    .thumbnail(0.1f) // 传入 0 到 1 之间的 float 值
                    .into(imageView)
            }),

            // 6, Glide 预加载、缓存到硬盘、以及加载监听
            ButtonModel("换一种方式加载图片", View.OnClickListener {
                // 6.1 换一种方式加载图片
                Glide.with(context)
                    .load(URL_IMAGE_2_MB)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable, transition: Transition<in Drawable>?
                        ) {
                            imageView.setImageDrawable(resource)
                        }
                    })

                //  所有into() 最终调用下面的into()
                //  private <Y extends Target<TranscodeType>> Y into(
                //      @NonNull Y target,
                //      @Nullable RequestListener<TranscodeType> targetListener, // TranscodeType 指的是 设置的 asBitmap（）、asDrawable（）等之后设置的类型。
                //      BaseRequestOptions<?> options,
                //      Executor callbackExecutor
                //   ) { }

            }),
            ButtonModel("预加载图片", View.OnClickListener {
                // 6.2 预加载图片到缓存
                // 如果有两个页面A（当前页面） 和 B（跳转页面），在 B 页面中要使用 Glide 显示一个很大的图片，
                // 那么在 A 页面的时候就可以先把 B 页面中要加载的图片缓存下来，等到 B 页面的时候，就可直接读取显示。
                Glide.with(context)
                    .load(URL_IMAGE_2_MB)
                    // 下载监听
                    .listener(object : RequestListener<Drawable> {
                        // onResourceReady() 方法和 onLoadFailed() 方法都有一个布尔值的返回值，
                        //  1， 返回 false 就表示这个事件没有被处理，还会继续向下传递，
                        //  2， 返回 true 就表示这个事件已经被处理掉了，从而不会再继续向下传递。
                        // 举个简单例子：如果在 RequestListener 的 onResourceReady() 中返回了 true，
                        // 那么就不会再回调 Target的onResourceReady() 方法了。
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
                            showMsg("加载成功，下个页面从缓存中取了。")

                            // 不写下个页面了，就在这里取吧
                            Glide.with(context).load(URL_IMAGE_2_MB).into(imageView);
                            return false
                        }
                    })
                    // 图片预加载
                    .preload()

            }),
            ButtonModel("下载图片", View.OnClickListener {
                // 6.3 下载图片到指定地址
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
            }),

            // 7， 图片变换
            // 在Glide中，Transformations 可以获取资源并修改它，然后返回被修改后的资源。
            // 通常变换操作是用来完成剪裁或对位图应用过滤器，但它也可以用于转换GIF动画，甚至自定义的资源类型。
            // Glide 内置了几种变换，比如 ：
            //   (1), CenterCrop  圆形  TODO Logan 几种类型区别， 参考，README.MD "2，Glide各种Transformation案例"
            //   (2), FitCenter
            //   (3), CircleCrop 圆形
            ButtonModel("图片变换(单次)", View.OnClickListener {
                // 7.1 单次变换
                val options = RequestOptions().circleCrop()
                Glide.with(context).load(IMAGE_SMALL).apply(options).into(imageView)

                // 与上一句效果一样
//                Glide.with(context).load(IMAGE_SMALL)
//                    .apply(RequestOptions.circleCropTransform()).into(imageView)

                // 需要注意，如果下面的写法，是有些问题的。
//                Glide.with(context).load(IMAGE_SMALL)
//                    .apply(RequestOptions.centerCropTransform()) // 忽略该效果
//                    .apply(RequestOptions.circleCropTransform()) // 仅执行该效果
//                    .into(imageView)
                // 如果想一次加载中变换多次，那么需要使用 MultiTransformation。
            }),
            ButtonModel("图片变换(多次)", View.OnClickListener {
                // 7.2 多次变换
                val multiTransform = MultiTransformation<Bitmap>(CenterCrop(), CircleCrop())
                val options = RequestOptions().transform(multiTransform)
                Glide.with(context).load(IMAGE_SMALL).apply(options).into(imageView)
            }),
            // 8, 禁用磁盘和内存的缓
            ButtonModel("禁用磁盘和内存的缓存图片", View.OnClickListener {
                val skipMemoryAndDiskCacheOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                Glide.with(context).load(IMAGE_BIG).apply(skipMemoryAndDiskCacheOptions)
                    .into(imageView)
            })
        )
    }

    @WorkerThread
    private fun createBitmapFromNetImage(context: Context): Bitmap? {
        // 也可以先把图片下载到硬盘上，得到一个 File文件，这个时候要用到 submit()。
        val target: FutureTarget<File> = Glide.with(context)
            .asFile()
            .load(URL_IMAGE_2_MB)
            .submit()

        var bitmap: Bitmap? = null

        try {
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

fun createButtons(
    context: Context, buttonsContainer: ViewGroup, vararg buttons: ButtonModel
) {
    buttons.forEach {
        buttonsContainer.addView(createButton(context, it))
    }
}

private fun createButton(
    context: Context, buttonModel: ButtonModel
): Button {
    return Button(context).apply {
        text = buttonModel.buttonText

        layoutParams = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        ).also { it.marginStart = dp2px(context, 5f) }

        setOnClickListener(buttonModel.onClickListener)
    }
}

