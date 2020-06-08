package com.logan.android.ui.base.adapter

import android.view.View
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * desc: Base ViewHolder <br/>
 * time: 2020/6/5 6:06 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
open class BaseViewHolder(itemView: View) :
    ViewHolder(itemView), View.OnClickListener, OnLongClickListener {

    //是否设置点击监听
    private var mHasClick = false

    //是否设置长按监听
    private var mHasLongClick = false

    // 容器view
    private val mContainerHolderView: View = itemView

    // item点击事件
    private var mOnRecyclerItemClickListener:
            AdapterRecyclerBase.OnRecyclerItemClickListener? = null

    // 长按事件
    private var mOnRecyclerItemLongClickListener:
            AdapterRecyclerBase.OnRecyclerItemLongClickListener? = null


    init {
        // 类ViewHolder未初始化完成，这个时候使用this很有危险性
        mContainerHolderView.setOnClickListener(this)
        mContainerHolderView.setOnLongClickListener(this)
    }


    override fun onClick(v: View) {
        mOnRecyclerItemClickListener?.onItemClick(v, layoutPosition)
    }

    override fun onLongClick(v: View?): Boolean {
        mOnRecyclerItemLongClickListener?.onItemLongClick(v, layoutPosition)
        return false
    }

    fun setOnRecyclerItemLongClickListener(onRecyclerItemLongClickListener: AdapterRecyclerBase.OnRecyclerItemLongClickListener?) {
        mHasLongClick = true
        mOnRecyclerItemLongClickListener = onRecyclerItemLongClickListener
    }

    fun setOnRecyclerItemClickListener(onRecyclerItemClickListener: AdapterRecyclerBase.OnRecyclerItemClickListener?) {
        mHasClick = true
        mOnRecyclerItemClickListener = onRecyclerItemClickListener
    }

    fun hasClick() = mHasClick

    fun hasLongClick() = mHasLongClick

    fun getHolderContainerView() = mContainerHolderView

}