package com.logan.android.ui.image.glide

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.image.glide.consts.GlideConsts.*
import com.logan.android.ui.image.glide.ext.okhttp.ProgressInterceptor
import com.logan.android.ui.image.glide.ext.okhttp.ProgressListener
import com.logan.android.ui.image.glide.target.DownloadImageTarget
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
 * desc: glide Target案例 <br/>
 * time: 2020/5/20 5:38 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class GlideTargetActivity : BaseActivity() {

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

            // 1, 重载 into()
            ButtonModel("重载into()", View.OnClickListener {
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

            // 2, preload - 预加载图片到缓存中
            ButtonModel("预加载 - preload", View.OnClickListener {
                // 如果有两个页面A（当前页面） 和 B（跳转页面），在 B 页面中要使用 Glide 显示一个很大的图片，
                // 那么在 A 页面的时候就可以先把 B 页面中要加载的图片缓存下来，等到 B 页面的时候，就可直接读取显示。

                val imageUrl = URL_IMAGE_MOUNTAIN_2MB_2048_1367
                Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    // 下载监听
                    .listener(object : RequestListener<Drawable> {
                        // onResourceReady() 和 onLoadFailed() 都有一个布尔值的返回值，
                        //  (1)， 返回 false 就表示这个事件没有被处理，还会继续向下传递，
                        //  (2)， 返回 true 就表示这个事件已经被处理掉了，不会再继续向下传递。

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
                                .load(imageUrl)
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
                                .into(imageView);
                            return false
                        }
                    })
                    .preload() // 图片预加载。加载图片的原始尺寸
                // .preload(720, 1280) // 指定加载图片的宽和高
            }),

            // 3, 下载图片到指定地址 asFile()，submit()
            ButtonModel("下载图片 - submit()", View.OnClickListener {
                Observable.timer(10, TimeUnit.MICROSECONDS)
                    .subscribeOn(Schedulers.io())
                    .map { createBitmapFromNetImage() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it == null) {
                            showMsg("图片下载失败，再尝试一下！")
                        } else {
                            imageView.setImageBitmap(it)
                        }
                    }
            }),

            // 4, 自定义Target处理资源
            ButtonModel("自定义Target", View.OnClickListener {
                Glide.with(context)
                    .load(URL_IMAGE_MOUNTAIN_2MB_2048_1367)
                    .downloadOnly(DownloadImageTarget())
            }),

            // 5，下载进度条
            ButtonModel("下载进度条", View.OnClickListener {
                // dialog
                val progressDialog = ProgressDialog(this).apply {
                    setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    setMessage("加载中")
                }

                // 设置监听
                val imageUrl = URL_IMAGE_SEASCAPE_900KB_2048_1360
                ProgressInterceptor.addListener(imageUrl, object : ProgressListener {
                    override fun onProgress(progress: Int) {
                        progressDialog.progress = progress
                    }
                })

                Glide.with(context)
                    .load(imageUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(object : SimpleTarget<Drawable>() {

                        override fun onLoadStarted(placeholder: Drawable?) {
                            // 显示dialog
                            progressDialog.show()
                        }

                        override fun onResourceReady(
                            resource: Drawable, transition: Transition<in Drawable>?
                        ) {
                            imageView.setImageDrawable(resource)
                            // 关闭Dialog
                            progressDialog.dismiss()
                            // 删除监听
                            ProgressInterceptor.removeListener(imageUrl)
                        }
                    })
            })
        )
    }

    @WorkerThread
    private fun createBitmapFromNetImage(): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            // submit()方法是对应的 Glide3 中的 downloadOnly()，这个方法只会下载资源，而不会对资源进行加载。
            // 当资源下载完成之后，我们可以得到资源的存储路径，以便后续进行操作。
            // submit() 和 preload() 方法类似，它也是可以替换 into() 方法。

            // asFile() 得到一个 File文件，这个时候要用到 submit() 下载。 submit()必修在子线程中执行
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
