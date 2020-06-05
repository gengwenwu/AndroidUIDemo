package com.logan.android.ui.image.glide

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * desc: Glide 定制 <br/>
 * time: 2020/6/3 4:47 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */

@GlideExtension
class MyGlideExtension private constructor() {

    companion object {

        @GlideOption
        fun useTransition(options: RequestOptions): BaseRequestOptions<*> {
            options.transform(BlurTransformation(10))
            return options
        }

// TODO Logan 没有效果
//        GlideApp.with(context).load(url)
//        .useTransition() //使用过渡动画效果
//        .into(imageView);

    }

}