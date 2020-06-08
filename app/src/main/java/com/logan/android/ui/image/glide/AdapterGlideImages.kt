package com.logan.android.ui.image.glide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.logan.android.ui.R
import com.logan.android.ui.base.adapter.AdapterRecyclerBase
import com.logan.android.ui.base.adapter.BaseViewHolder
import com.logan.android.ui.tool.log
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_glide_main.*

/**
 * desc: glide 图片列表 <br/>
 * time: 2020/6/8 10:28 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class AdapterGlideImages(
    context: Context, list: MutableList<String>
) : AdapterRecyclerBase<AdapterGlideImages.ViewHolder, String>(context, list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(getContext()).inflate(
                R.layout.item_glide_recycler, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (getList().isNullOrEmpty()) {
            return
        }

        //holder.iv_image
        // TODO: 2020/6/8 Logan
        log("========> url: ${getList()[position]}")
        Glide.with(getContext()).load(getList()[position]).into(holder.iv_image)
    }

    inner class ViewHolder(override val containerView: View) : BaseViewHolder(containerView),
        LayoutContainer

}