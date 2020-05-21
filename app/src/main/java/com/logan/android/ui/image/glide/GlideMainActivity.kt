package com.logan.android.ui.image.glide

import android.content.Context
import android.os.Bundle
import android.util.Log
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

        val URL_IMAGE_2_MB =
            "http://tiebapic.baidu.com/forum/abpic/item/55cbecec8a13632785695253868fa0ec09fac7db.jpg"
        val URL_IMAGE_901_KB = "https://farm6.staticflickr.com/5344/9109117816_4de9739e71_k.jpg"
        val URL_IMAGE_132_KB = "http://tiebapic.baidu.com/forum/w%3D580/sign=49ef71fcdcef76093c0b99971edda301/4e6f47087bf40ad127c2b282402c11dfa9ecce45.jpg"

        val IMAGE_SMALL = URL_IMAGE_132_KB
        val IMAGE_MIDDLE = URL_IMAGE_901_KB
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
                val options: RequestOptions =
                    RequestOptions().placeholder(R.drawable.ic_default)
                Glide.with(context).load(IMAGE_SMALL).apply(options).into(imageView)
            }),
            ButtonModel("错误占位符", View.OnClickListener {
                // 2.2  错误占位符
                // 如果图片不能成功加载，就显示特定的图片
                val optionsError: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default).error(R.drawable.ic_error)
                Glide.with(context).load(IMAGE_SMALL).apply(optionsError).into(imageView)
            }),
            ButtonModel("错误占位符(errorUrl)", View.OnClickListener {
                // 从 Glide 4.3.0 开始，可以使用 error API 来指定一个 RequestBuilder，
                // 在主请求失败的时候开始一次新的加载 (即：加载 errorUrl 地址)。
                val errorUrl = "失败要加载的地址"
                Glide.with(context)
                    .load(IMAGE_SMALL)
                    .error(Glide.with(context).load(errorUrl))
                    .into(imageView)
            }),
            ButtonModel("显示fallback地址(url==null)", View.OnClickListener {
                // 2.3 load() 方法 url 应该不是 null ，但是如果有可能为 null 的情况，
                //     你可以通过设置 fallback() 方法来显示 url 为 null 的图片，
                val optionsFallback: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_error)
                    .fallback(R.drawable.ic_fallback)
                    .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                Glide.with(context).load(IMAGE_SMALL).apply(optionsFallback).into(imageView)

                // 上面 2.3 例子 图片显示流程为：
                //      1. 正在加载 url 的时候，显示图片 ic_default。
                //      2. 加载成功显示 url 指向的图片。
                //      3. 加载失败显示 ic_error。
                //      4. 如果 url 为 null（一定得是 null ），则显示图片 ic_fallback。
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

