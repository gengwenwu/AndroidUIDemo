package com.logan.android.ui.image.glide.target

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.logan.android.ui.tool.log
import java.io.File

/**
 * desc: 自定义target <br/>
 * time: 2020/6/12 3:05 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class DownloadImageTarget : Target<File> {
    
    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
        log("====== onResourceReady() -> ${resource}")
    }

    override fun getSize(cb: SizeReadyCallback) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
    }

    override fun onLoadStarted(placeholder: Drawable?) {

    }

    override fun onLoadFailed(errorDrawable: Drawable?) {

    }

    override fun getRequest(): Request? {
        return null
    }

    override fun onStop() {

    }

    override fun setRequest(request: Request?) {

    }

    override fun removeCallback(cb: SizeReadyCallback) {

    }

    override fun onLoadCleared(placeholder: Drawable?) {

    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }


}