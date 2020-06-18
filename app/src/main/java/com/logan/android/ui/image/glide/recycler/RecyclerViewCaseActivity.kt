package com.logan.android.ui.image.glide.recycler

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.image.glide.consts.GlideConsts.*
import kotlinx.android.synthetic.main.activity_glide_recycler.*

/**
 * desc: Glide 和 RecyclerView 结合使用，解决卡顿问题。 <br/>
 * time: 2020/6/5 5:28 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class RecyclerViewCaseActivity : BaseActivity() {

    // 是否正在滑动，true：是
    private var isScrolling = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_recycler)

        rv_images.apply {
            val context = this@RecyclerViewCaseActivity
            layoutManager = LinearLayoutManager(context)
            this.adapter =
                AdapterGlideImages(
                    context,
                    getImageUrls()
                )

            // 关键代码1：添加滑动监听
            addOnScrollListener(onScrollListener)
        }
    }

    override fun onPause() {
        super.onPause()

        // 关键代码2：删除滑动监听
        rv_images.removeOnScrollListener(onScrollListener)
    }

    private val onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // 关键代码3
                when (newState) {
                    // RecyclerView 正在滚动 或者 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
                    RecyclerView.SCROLL_STATE_DRAGGING, RecyclerView.SCROLL_STATE_SETTLING -> {
                        isScrolling = true

                        // 停止请求 TODO Logan 只是停止RecyclerViewCaseActivity页面的吗？
                        Glide.with(this@RecyclerViewCaseActivity).pauseRequests()
                    }

                    // RecyclerView 停止滚动
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (isScrolling) {
                            Glide.with(this@RecyclerViewCaseActivity).resumeRequests()
                        }
                    }
                }
            }
        }

    private fun getImageUrls(): MutableList<String> {
        return mutableListOf(
            URL_IMAGE_WATCH_172KB_1000_100,
            URL_IMAGE_BG_PINK_172KB_1680_580,
            URL_IMAGE_MAN_SHOE_75KB_800_800,
            URL_IMAGE_DUST_DOG_8KB_144_144,
            URL_IMAGE_DOG_3MB_5295_3355,
            URL_IMAGE_MOUNTAIN_2MB_2048_1367,
            URL_IMAGE_SEASCAPE_900KB_2048_1360,
            URL_GIF_JC_AD_125KB_1680_580,
            //==
            URL_IMAGE_WATCH_172KB_1000_100,
            URL_IMAGE_BG_PINK_172KB_1680_580,
            URL_IMAGE_MAN_SHOE_75KB_800_800,
            URL_IMAGE_DUST_DOG_8KB_144_144,
            URL_IMAGE_DOG_3MB_5295_3355,
            URL_IMAGE_MOUNTAIN_2MB_2048_1367,
            URL_IMAGE_SEASCAPE_900KB_2048_1360,
            URL_GIF_JC_AD_125KB_1680_580
        )
    }

}