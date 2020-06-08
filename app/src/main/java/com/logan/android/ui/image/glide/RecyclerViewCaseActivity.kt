package com.logan.android.ui.image.glide

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.logan.android.ui.R
import com.logan.android.ui.base.BaseActivity
import com.logan.android.ui.image.glide.GlideConsts.*
import kotlinx.android.synthetic.main.activity_glide_recycler.*

/**
 * desc: Glide 和 RecyclerView 结合使用出现卡顿问题。 <br/>
 * time: 2020/6/5 5:28 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class RecyclerViewCaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide_recycler)

        rv_images.apply {
            val context = this@RecyclerViewCaseActivity
            layoutManager = LinearLayoutManager(context)
            this.adapter = AdapterGlideImages(context, getData())
        }
    }

    private fun getData(): MutableList<String> {
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