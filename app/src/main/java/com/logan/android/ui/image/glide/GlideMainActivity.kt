package com.logan.android.ui.image.glide

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.tool.dp2px

/**
 * desc: glide 案例主页面 <br/>
 * time: 2020/5/20 5:38 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class GlideMainActivity : BaseActivity() {

    companion object {
        // 错误image地址
        val ERROR_URL = "http://tiebapic.baidu.com/09fac7db.jpg"

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
        val IMAGE_BIG = URL_IMAGE_2_MB
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
            })

        )
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

